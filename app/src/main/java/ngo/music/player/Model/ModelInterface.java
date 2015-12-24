package ngo.music.player.Model;

import org.json.JSONObject;

/**
 * Created by fabiongo on 12/24/2015.
 */
public interface ModelInterface {
    String getId();

    JSONObject getJSONObject();

    void setJSONObject(JSONObject jsonObject);

    String getAttribute(String attribute);

    void setAttribute(String attribute, String value);
}
