package ngo.music.player.ModelManager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.player.Model.Category;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.helper.Constants;

public abstract class CategoryManager extends ModelManager implements Constants.Data, Constants,
        Constants.Models {



    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    //return array of jsonObject of songs
    public JSONObject[] getSongsFromCategory(String id) {
        Category category = null;
        for (Model model : models) {
            if (model.getId().equals(id)) {
                category = (Category) model;
                break;
            }
        }
        if (category == null) {
            Log.e("category", "null");
            return new JSONObject[0];
        }
        JSONArray array = category.getJSONObject().optJSONArray("songs");

        JSONObject[] songs = new JSONObject[array.length()];
        for (int i = 0; i < array.length(); i++) {
            try {
                songs[i] = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return songs;
    }


    public void newCategory(String title) {
        JSONObject object = new JSONObject();
        try {
            object.put("title", title);
            JSONArray songArray = new JSONArray();
            object.put("songs",songArray);
            generate(object);
            storeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected JSONObject createSongJSONObject(String songID){
        JSONObject songObject = new JSONObject();
        try {
            songObject.put("id", songID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songObject;
    }
    public void addSongToCategory(String songID, String categoryID){
        Category category = (Category) get(categoryID);

        JSONArray array = category.getJSONObject().optJSONArray("songs");
        JSONObject songObject = createSongJSONObject(songID);
        array.put(songObject);
        try {
            category.getJSONObject().put("songs", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }
    public void removeSongFromAllCategories(String songID){
        for (Model model:models) {
            Category category = (Category)model;
            JSONArray array = category.getJSONObject().optJSONArray("songs");
            for(int i=0;i<array.length();i++){
                try {
                    if(array.getJSONObject(i).getString("id").equals(songID)){
                        array.remove(i);
                        break;
                    }
                } catch (JSONException e) {
                    continue;
                }
            }
            try {
                category.getJSONObject().put("songs",array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        storeData();

    }
    public void removeAllSongFromCategory(String categoryID) {
        Category category = (Category) get(categoryID);

        JSONArray array = new JSONArray();
        try {
            category.getJSONObject().put("songs", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }
    public void removeSongFromCategory(String songID, String categoryID){
        Category category = (Category) get(categoryID);

        JSONArray array = category.getJSONObject().optJSONArray("songs");
        for(int i=0;i<array.length();i++){
            try {
                if(array.getJSONObject(i).getString("id").equals(songID)){
                    array.remove(i);
                    break;
                }
            } catch (JSONException e) {
                continue;
            }
        }
        try {
            category.getJSONObject().put("songs",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }
    public void updateTitle(String newtitle, String categoryId){

    }

}
