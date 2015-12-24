package ngo.music.player.ModelManager;

public class ArtistManager extends ReadOnlyOfflineCategoryManager {


    @Override
    protected int setType() {
        // TODO Auto-generated method stub
        return ARTIST;
    }

    @Override
    protected String setFilename() {
        return "artists.json";
    }


}
