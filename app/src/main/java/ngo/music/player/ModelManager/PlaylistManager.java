package ngo.music.player.ModelManager;

public class PlaylistManager extends OfflineCategoryManager {


    @Override
    protected int setType() {
        return PLAYLIST;
    }

    @Override
    protected String setFilename() {
        return "playlists.json";
    }
}
