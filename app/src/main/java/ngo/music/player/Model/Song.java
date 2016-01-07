package ngo.music.player.Model;

import org.json.JSONObject;

import java.util.Objects;

public abstract class Song extends Model {

    public Song(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.object.optString("title");
    }

    public abstract String getName();
    public abstract String getArtist();
    public abstract String getAlbum();
    public abstract String getLink();

}
