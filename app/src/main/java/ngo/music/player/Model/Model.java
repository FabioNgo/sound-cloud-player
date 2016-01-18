package ngo.music.player.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ngo.music.player.helper.Constants;

/**
 * Created by fabiongo on 12/24/2015.
 */
public abstract class Model implements ModelInterface, Comparable<Model>,Constants.Models {
    JSONObject object;

    public Model(JSONObject object) {
        // TODO Auto-generated constructor stub
        this.object = object;
    }
    public abstract int getType();
    @Override
    public String getId() {
        try {
            return this.object.getString("id");
        } catch (JSONException e) {
            return "";
        }
    }

    @Override
    public JSONObject getJSONObject() {
        return object;
    }

    @Override
    public void setJSONObject(JSONObject jsonObject) {
        this.object = jsonObject;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if (obj instanceof Model) {
            if (Objects.equals(this.getId(), ((Model) obj).getId())) return true;
        }
        return false;
    }

    @Override
    public int compareTo(Model another) {
        return getId().compareTo(another.getId());
    }

    @Override
    public String getAttribute(String attribute) {
        return this.object.optString(attribute);
    }

    @Override
    public void setAttribute(String attribute, String value) {
        try {
            this.object.put(attribute, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
