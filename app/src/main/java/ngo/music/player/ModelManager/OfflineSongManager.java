package ngo.music.player.ModelManager;

import android.database.Cursor;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.View.MusicPlayerMainActivity;

/**
 * Created by fabiongo on 12/24/2015.
 */
public class OfflineSongManager extends SongManager {


    @Override
    protected int setType() {
        return OFFLINE;
    }

    @Override
    protected String setFilename() {
        return "offline_song.json";
    }

    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    public JSONObject getJSONObjectFromCursor(Cursor c) {
        JSONObject object = new JSONObject();
        try {
            object.put("song_id", c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
            object.put("title", c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            object.put("artist", c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            object.put("album", c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            object.put("link", c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
            object.put("duration", c.getString(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            return object;
        } catch (JSONException e) {
            return null;
        }


    }

    public void getSongsFromSDCard() {
        clearModels();
        Cursor c = MusicPlayerMainActivity
                .getActivity()
                .getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Audio.Media.IS_MUSIC + "!=0", null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String url = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                if (url.endsWith(".mp3")) {
                    generate(getJSONObjectFromCursor(c));
                }
            }
        }
        storeData();
        c.close();
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new OfflineSong[this.models.size()];

        return this.models.toArray(models);
    }
}
