package ngo.music.player.ModelManager;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Playlist;

public class PlaylistManager extends OfflineCategoryManager {


    @Override
    protected int setType() {
        return PLAYLIST;
    }

    @Override
    protected String setFilename() {
        return "playlists.json";
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Playlist[this.models.size()];

        return this.models.toArray(models);
    }
}
