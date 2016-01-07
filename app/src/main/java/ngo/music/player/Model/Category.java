package ngo.music.player.Model;

import org.json.JSONObject;

public abstract class Category extends Model {

    public Category(JSONObject object) {
        super(object);

    }
    public String getTitle(){
        return getAttribute("title");
    }

}
