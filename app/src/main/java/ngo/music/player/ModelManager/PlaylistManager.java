package ngo.music.player.ModelManager;

import org.json.JSONObject;

import ngo.music.player.Model.Category;
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

    @Override
    public void updateTitle(String newtitle, String categoryId) {
        super.updateTitle(newtitle, categoryId);
        get(categoryId).setAttribute("title",newtitle);
        this.setChanged();
        storeData();
    }

    @Override
    public Model newCategory(String title) {
        Playlist object = (Playlist) super.newCategory(title);
        storeData();
        return object;
    }
}
