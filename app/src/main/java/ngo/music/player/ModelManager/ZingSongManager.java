package ngo.music.player.ModelManager;

import android.database.Cursor;
import android.provider.MediaStore;

import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.ZingSong;
import ngo.music.player.View.MusicPlayerMainActivity;

/**
 * Created by Luis Ngo on 6/1/2016.
 */
public class ZingSongManager extends SongManager {
    private String list_id;
    /**
     * The first function run in the constructor
     */
    @Override
    protected void initialize() {

    }

    /**
     * Set the {@code type} for each Controller
     *
     * @return the type users want to set
     */
    @Override
    protected int setType() {
        return 0;
    }

    /**
     * Generate string to show to user
     *
     * @param model needed to show
     * @return the string of each model
     */
    @Override
    public String modelToString(ModelInterface model) {
        return null;
    }

    /**
     * Load data from json file
     */
    @Override
    public void loadData() {
        clearModels();
        MusicPlayerMainActivity activity = MusicPlayerMainActivity.getActivity();
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
        }
        this.setChanged();
        this.notifyObservers(this.models);
        c.close();
    }

    /**
     * Store data from json file
     */
    @Override
    public void storeData() {

    }

    /**
     * Get a song from xml from mp3 zing
     * @param xml
     * @return
     */
    private ZingSong (String xml){

    }
}
