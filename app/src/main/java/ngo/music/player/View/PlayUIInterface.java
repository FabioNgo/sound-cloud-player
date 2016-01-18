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

    void pause();

    void play();

    /**
     * Update Ui when stop music
     */
    void stop();
}
