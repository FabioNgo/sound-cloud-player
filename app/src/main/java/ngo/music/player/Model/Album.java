package ngo.music.player.Model;

import org.json.JSONObject;

public class Album extends Category {


    public Album(JSONObject object) {
        super(object);
    }

    @Override
    public int getType() {
        return ALBUM;
    }
}
