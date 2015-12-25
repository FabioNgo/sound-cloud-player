package ngo.music.player.Controller;

import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import ngo.music.player.Model.Queue;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.helper.Constants;

/**
 * Created by fabiongo on 12/25/2015.
 */
public class MusicPlayerServiceController extends Observable implements Constants.Models {
    private static MusicPlayerServiceController instance = null;

    private Song currentSong;
    private int loopState = 0;
    private Song nextSong;
    private Stack<Song> stackSongplayed;
    private QueueManager queueManager;
    private String playingSongId;
    private int stoppedTime;
    private boolean isShuffle = true;
    MusicPlayerServiceController(){
        this.queueManager = (QueueManager) ModelManager.getInstance(QUEUE);
    }
    public static MusicPlayerServiceController getInstance() {
        if (instance == null) {
            instance = new MusicPlayerServiceController();
        }

        return instance;
    }
    /**
     * Get current song is playing
     *
     * @return
     */
    public Song getCurrentSong() {
        if(currentSong == null){
            if(nextSong == null){
                computeNextSong();
            }
            return nextSong;
        }
        return currentSong;

    }
    /**
     * Get next song
     *
     * @return
     */
    public Song getNextSong() {

        return nextSong;

    }

    /**
     * get previous played song
     * @return Song
     */
    public Song getPreviousSong(){
        stackSongplayed.pop();

        if (stackSongplayed.isEmpty()) {

            computeNextSong();
            return nextSong;

        } else {
            return stackSongplayed.peek();
        }
    }
    public void setPlayingSongID(String songID){
        this.playingSongId = songID;
    }
    public void setStoppedTime(int time){
        this.stoppedTime = time;
    }
    public void computeNextSong() {
        int nextPosition;
        Song[] queue = this.queueManager.getAllSong();
        int currentSongPosition = 0;
        int size = queue.length;
        for(int i=0;i<size;i++){
            if(queue[i].equals(currentSong)){
                currentSongPosition = i;
                break;
            }
        }
        if (size > 2) {
            if (isShuffle) {
                // TODO Auto-generated method stub
                Random random = new Random(System.currentTimeMillis());
                nextPosition = currentSongPosition
                        + (Math.abs(random.nextInt()) % (size - 2)) + 1;
                nextPosition = nextPosition % size;

            } else {
                nextPosition = currentSongPosition + 1;

                nextPosition = nextPosition % size;

            }

            nextSong = queue[nextPosition];
        } else {
            if (size == 2) {
                nextSong = queue[(currentSongPosition + 1) % 2];
            }
            if (size == 1) {
                nextSong = currentSong;
            }
            if (size == 0) {
                nextSong = null;
            }
        }
    }
    /**
     * Get Duration of playingSong
     *
     * @return
     */
    public long getDuration() {
        Song song = getCurrentSong();
        if (song != null) {
            return Long.parseLong(getCurrentSong().getAttribute("duration"));
        } else {
            return 0;
        }
    }
    /**
     * set playing suffle when play a list
     */
    public void setShuffle() {
        // TODO Auto-generated method stub
        isShuffle = !isShuffle;
        this.notifyObservers(isShuffle);

    }
    public boolean isShuffle() {
        return isShuffle;
    }

    public int getLoopState() {
        return loopState;
    }
    /**
     * change Loop State
     */
    public void changeLoopState() {
        loopState++;
        loopState = loopState % 2;
//        UIController.getInstance().updateUiWhilePlayingMusic(-1);
    }
}
