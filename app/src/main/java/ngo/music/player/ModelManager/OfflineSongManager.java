package ngo.music.player.ModelManager;

import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import ngo.music.player.Controller.MediaStoreObserver;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.Model.Song;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;

/**
 * Created by fabiongo on 12/24/2015.
 */
public class OfflineSongManager extends SongManager {

    Handler handler;
    MediaStoreObserver observer;

    public OfflineSongManager() {
        super();
        handler= new Handler();
        observer = MediaStoreObserver.getInstance(MusicPlayerMainActivity.getActivity().getContentResolver(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new MediaStoreObserver.OnChangeListener() {
            @Override
            public void onChange() {
                loadData();
            }
        });

    }

    @Override
    protected int setType() {
        return OFFLINE;
    }


    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    public JSONObject getJSONObjectFromCursor(Cursor c) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)));
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

    @Override
    public String generateID(JSONObject object) {
        try {
            return object.getString("id");
        } catch (JSONException e) {
            return "";
        }
    }

    @Override
    public void loadData() {
        clearModels();
        MusicPlayerMainActivity activity = MusicPlayerMainActivity.getActivity();
        try {
            Cursor c = activity
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
                c.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.setChanged();
        this.notifyObservers(this.models);

    }

    @Override
    public void storeData() {

    }

    @Override
    protected void initialize() {

    }

    @Override
    public void remove(String id) {

        Song song = (Song) get(id);
//        Files
        File file = new File(song.getLink());

        boolean deleted = false;

            deleted = file.delete();

        if(!deleted){
            return;
        }
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri itemUri = ContentUris.withAppendedId(contentUri, Long.parseLong(id));
        MusicPlayerMainActivity.getActivity().getContentResolver().delete(itemUri,null,null);
        super.remove(id);
        for(int i=0;i< Models.SIZE;i++){
            ModelManager temp = ModelManager.getInstance(i);
            if(temp instanceof CategoryManager){
                ((CategoryManager)temp).removeSongFromAllCategories(song.getId());
            }
        }
        setChanged();
        notifyObservers(this.models);
        super.remove(id);
    }
}
