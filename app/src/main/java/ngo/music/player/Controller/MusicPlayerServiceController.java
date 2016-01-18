package ngo.music.player.Controller;

import android.os.CountDownTimer;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Stack;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.QueueManager;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.service.MusicPlayerService;

/**
 * Created by fabiongo on 12/25/2015.
 */
public class MusicPlayerServiceController extends Observable implements Constants.Models, Constants.MusicService {
    private static MusicPlayerServiceController instance = null;
    private int stoppedTime=0;

    private Song currentSong;
    private int playerMode = 0;
    private Song nextSong;
    private Stack<Song> stackSongplayed;
    private QueueManager queueManager;
    boolean canAnnounceNextSong;
    private boolean isShuffle = true;
    /**
     * file path where store json files
     */
    private String filePath = Environment.getExternalStorageDirectory().getPath() + "/Music Player";
    /**
     * json file name which store data
     */
    private String filename = "playing.json";
    private CountDownTimer timer;

    public MusicPlayerServiceController(){
        this.queueManager = (QueueManager) ModelManager.getInstance(QUEUE);
        stackSongplayed = new Stack<>();
        try {
            filename = filePath+"/"+filename;
            JSONObject object = new JSONObject(fileContentToString());
            currentSong = (Song) ModelManager.getInstance(OFFLINE).get(object.getString("song_id"));
            try {
                WaveFormController.getInstance().ReadFile(new File(currentSong.getLink()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            notifyObservers(currentSong);
            computeNextSong();
//            stoppedTime = object.getInt("stopped_time");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        if(currentSong == null) {
            JSONObject nullSong = null;

            try {
                nullSong = new JSONObject("{\"id\":\"-1\"}");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new Song(nullSong) {
                @Override
                public int getType() {
                    return OFFLINE;
                }

                @Override
                public String getName() {
                    return "";
                }

                @Override
                public String getArtist() {
                    return "";
                }

                @Override
                public String getAlbum() {
                    return "";
                }

                @Override
                public String getLink() {
                    return "";
                }

                @Override
                public int getDuration() {
                    return 1;
                }
            };
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


        if (stackSongplayed.isEmpty()) {

            computeNextSong();
            return nextSong;

        } else {
            return stackSongplayed.pop();
        }
    }
    public void setStoppedTime(int time){
        this.stoppedTime = time;
    }
    public void computeNextSong() {
        int nextPosition;
        ArrayList<Song> queue = this.queueManager.getAllSong();
        int currentSongPosition = 0;
        int size = queue.size();

        for(int i=0;i<size;i++){
            if(queue.get(i).equals(currentSong)){
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

            nextSong = queue.get(nextPosition);
        } else {
            if (size == 2) {
                nextSong = queue.get((currentSongPosition + 1) % 2);
            }
            if (size == 1) {
                nextSong = currentSong;
            }
            if (size == 0) {
                ArrayList<Model> songs = ModelManager.getInstance(OFFLINE).getAll();
                queue.clear();
                for (Model model:songs) {
                    queue.add((Song) model);
                }
                if(queue.size() == 0){
                    nextSong = null;
                }else{
                    ((QueueManager)ModelManager.getInstance(QUEUE)).replaceQueue(queue);
                    nextSong = queue.get(0);
                }

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
            return getCurrentSong().getDuration();
        } else {
            return 0;
        }
    }

    /**
     * Read json file as one string
     *
     * @return the generated string from json file
     */
    private String fileContentToString() {
        String result = "";
        BufferedReader br = null;

        try {

            String sCurrentLine;
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File yourFile = new File(filename);
            if (!yourFile.exists()) {
                yourFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(filename);
                printWriter.write("{}");
                printWriter.close();

            }
            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {
                result += sCurrentLine + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;

    }
    public void storeData() {
        // TODO Auto-generated method stub
        JSONObject object = new JSONObject();
        try {
            object.put("song_id",currentSong.getId());
            object.put("stopped_time", MusicPlayerService.getInstance().getCurrentTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter printWriter = new PrintWriter(filename);
            printWriter.write(object.toString());
            printWriter.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void pushStackPlayed(Song song){
        if(song!=null){
            this.stackSongplayed.push(song);
        }

    }
    public void clearStack(){
        this.stackSongplayed.clear();
    }
    public void setCurrentSong(final Song currentSong) {

        this.currentSong = currentSong;
        Thread readfileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WaveFormController.getInstance().ReadFile(new File(currentSong.getLink()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readfileThread.start();
        computeNextSong();
        storeData();
        setChanged();
        notifyObservers(currentSong);
        String format = String
                .format("Song playing: %s \nNext song: %s",
                        currentSong.getName(), nextSong.getName());
        Helper.makeToastText(format,
                MusicPlayerService.getInstance());
        canAnnounceNextSong = true;
    }
    /**
     * Start timer, to update info from service after 1 second
     */
    public void startTimer() {

        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                long currentTime = MusicPlayerService.getInstance()
                        .getCurrentTime();
                long duration = MusicPlayerServiceController.getInstance().getDuration();
                if(duration!=0) {
                    if ((currentTime * 100) / duration == 50 && canAnnounceNextSong) {
                        String format = String.format("Next song: %s",
                                MusicPlayerServiceController.getInstance().getNextSong()
                                        .getName());
                        Helper.makeToastText(format,
                                MusicPlayerService.getInstance());
                        canAnnounceNextSong = false;
                    }
                    MusicPlayerServiceController.getInstance().setChanged();
                    MusicPlayerServiceController.getInstance().notifyObservers(MUSIC_PROGRESS);
                }
            }

            @Override
            public void onFinish() {
                this.start();
            }
        };
        timer.start();
    }
    public int getStoppedTime() {
        return stoppedTime;
    }
    /**
     * Set song be the next song to be played
     *
     * @param song
     */
    public void addToNext(Song song) {
        // TODO Auto-generated method stub
        nextSong = song;
    }
    /**
     * Stop updating info from service
     */
    public void stopTimer() {
        // TODO Auto-generated method stub
        if (timer != null) {
            timer.cancel();
        }
        storeData();
    }

    public void notifyObservers(Object data, boolean forceChanged) {
        if(forceChanged){
            this.setChanged();
        }
        super.notifyObservers(data);
    }
    //shuffle, loop one, in order
    public void setPlayerMode() {
        playerMode = (playerMode+1)%3;
    }
    //shuffle, loop one, in order
    public void setPlayerMode(int playerMode) {
        this.playerMode = playerMode;
    }
    public int getPlayerMode() {
        return playerMode;
    }
}
