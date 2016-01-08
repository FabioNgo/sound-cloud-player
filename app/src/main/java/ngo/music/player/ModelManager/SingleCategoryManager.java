package ngo.music.player.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.Song;
import ngo.music.player.helper.Helper;

/**
 * Created by fabiongo on 1/3/2016
 * Manage Category that contain only one object ( Queue, Favorite ...).
 */
public abstract class SingleCategoryManager extends CategoryManager {
    /**
     * json file name which store data
     */
    private String filename;
    protected abstract String setTitle();
    private String title;
    @Override
    protected void initialize() {
        title = setTitle();
        filename = title+".json";
    }


    public void addSongsToCategory(String songID){
        Category category = (Category) this.models.get(0);
        addSongToCategory(songID,category.getId());
        notifyObservers(this.models);
        storeData();
    }
    public ArrayList<Song> getAllSong() {
        String id = this.models.get(0).getId();
        return  getSongsFromCategory(id);
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
        if(this.models.size() == 0 ){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title",title);
                jsonObject.put("songs", new JSONArray());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            generate(jsonObject);
            storeData();
        }
    }

    @Override
    public void storeData() {
        JSONArray jsonArray = new JSONArray();
        for (Model model : models) {
            jsonArray.put(model.getJSONObject());
        }
        Helper.storeJsonFile(filename,jsonArray);
        setChanged();
        notifyObservers(this.models);
    }
    public void removeSongFromCategory(Song song) {
        // TODO Auto-generated method stub
        String id = song.getId();
        String cateId = this.models.get(0).getId();
        removeSongFromCategory(id, cateId);
    }
    public void addSongsToCategory(ArrayList<Song> songs) {
        try {
            for (Song song :songs) {
                JSONArray array = this.models.get(0).getJSONObject().getJSONArray("songs");
                for(int i=0;i<array.length();i++){
                    try {
                        if(array.getJSONObject(i).getString("id").equals(song.getId())){
                            return;
                        }
                    } catch (JSONException e) {
                        continue;
                    }
                }
                JSONObject object = new JSONObject();
                object.put("id", song.getId());
                array.put(object);
                this.models.get(0).getJSONObject().put("songs", array);
            }
            setChanged();
            notifyObservers(this.models);
            storeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
