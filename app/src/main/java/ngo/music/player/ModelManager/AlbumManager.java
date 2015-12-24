package ngo.music.player.ModelManager;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;

public class AlbumManager extends ReadOnlyOfflineCategoryManager {


    @Override
    protected int setType() {
        return ALBUM;
    }

    @Override
    protected String setFilename() {
        return "album.json";
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Album[this.models.size()];

        return this.models.toArray(models);
    }
}
