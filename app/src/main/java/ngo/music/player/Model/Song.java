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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song) {
            if (Objects.equals(this.getAttribute("song_id"), ((Model) obj).getAttribute("song_id"))) return true;
        }
        return false;
    }
}
