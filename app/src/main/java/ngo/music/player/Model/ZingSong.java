package ngo.music.player.Model;

import org.json.JSONObject;

/**
 * Created by Luis Ngo on 6/1/2016.
 */
public class ZingSong extends Song {


    public ZingSong(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getArtist() {
        return null;
    }

    @Override
    public String getAlbum() {
        return null;
    }

    @Override
    public String getLink() {
        return null;
    }
}
