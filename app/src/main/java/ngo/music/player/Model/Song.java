package ngo.music.player.Model;

import org.json.JSONObject;

public abstract class Song extends Model {

	public Song(JSONObject jsonObject) {
		super(jsonObject);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.object.optString("title");
	}





}
