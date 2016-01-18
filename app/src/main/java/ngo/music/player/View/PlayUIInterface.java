package ngo.music.player.View;

import ngo.music.player.Model.Song;

/**
 * Created by fabiongo on 12/24/2015.
 */
public interface PlayUIInterface {
    /**
     * update info of song: Artist, Image, Description....
     *
     * @param song
     */
    void updateSongInfo(Song song);

    void updateMusicProgress();

    /**
     * call when pause playing song
     */
    void pause();

    /**
     * call when play new song
     */
    void play();

    /**
     * resume player from pause
     */
    void resume();
    /**
     * Update Ui when stop music
     */
    void stop();
}
