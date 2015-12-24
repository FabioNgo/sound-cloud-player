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

    public String[] getCategoryNames() {
        String[] names = new String[this.models.size()];
        for (int i = 0; i < models.size(); i++) {
            Category category = (Category) models.get(i);
            names[i] = category.getAttribute("title");
        }
        return names;
    }

    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    //return array of jsonObject of songs
    public JSONObject[] getSongsFromCategory(String categoryTitle) {
        Category category = null;
        for (Model model : models) {
            if (model.getAttribute("title").equals(categoryTitle)) {
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
            generate(object);
            storeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
