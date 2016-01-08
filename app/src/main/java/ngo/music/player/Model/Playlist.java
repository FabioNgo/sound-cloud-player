package ngo.music.player.Model;

import org.json.JSONObject;

public class Playlist extends Category {


    public Playlist(JSONObject object) {
        super(object);
    }

    @Override
    public int getType() {
        return PLAYLIST;
    }
}
