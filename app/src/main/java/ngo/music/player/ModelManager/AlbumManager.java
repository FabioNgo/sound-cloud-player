package ngo.music.player.ModelManager;

public class AlbumManager extends ReadOnlyOfflineCategoryManager {


    @Override
    protected int setType() {
        return ALBUM;
    }

    @Override
    protected String setFilename() {
        return "album.json";
    }
}
