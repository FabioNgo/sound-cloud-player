package ngo.music.player.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.Playlist;
import ngo.music.player.helper.Helper;

public class PlaylistManager extends CategoryManager {

    /**
     * json file name which store data
     */
    private String filename;

    @Override
    protected void initialize() {
        filename = "playlists.json";
    }

    @Override
    protected int setType() {
        return PLAYLIST;
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
    @Override
    public void loadData() {
        // TODO Auto-generated method stub
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(Helper.fileContentToString(filename));

            for (int i = 0; i < jsonArray.length(); i++) {
                Model model = createModel(jsonArray.getJSONObject(i));
                int value = Integer.parseInt(model.getId());
                if (value > currentIDSuffix) {
                    currentIDSuffix = value;
                }
                models.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void storeData() {
        // TODO Auto-generated method stub
        JSONArray jsonArray = new JSONArray();
        for (Model model : models) {
            jsonArray.put(model.getJSONObject());
        }
        Helper.storeJsonFile(filename,jsonArray);
        setChanged();
        notifyObservers(this.models);
    }
}
