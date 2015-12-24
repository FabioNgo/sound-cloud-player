package ngo.music.player.ModelManager;

import ngo.music.player.Model.Artist;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;

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

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Artist[this.models.size()];

        return this.models.toArray(models);
    }

}
