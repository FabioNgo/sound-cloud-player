package ngo.music.player.ModelManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Song;

public class AlbumManager extends CategoryManager implements Observer {


    @Override
    protected void initialize() {
        ModelManager.getInstance(OFFLINE).addObserver(this);
    }

    @Override
    protected int setType() {
        return ALBUM;
    }

    @Override
    public void loadData() {

        ArrayList<Model> songs = ModelManager.getInstance(OFFLINE).getAll();
        for (Model model:songs) {
            Song song = (Song) model;
            Album currentAlbum = null;
            for (Model model2: this.models ) {
                Album album = (Album) model2;
                if(song.getAlbum().equals(album.getTitle())){
                    currentAlbum = album;
                    break;
                }
            }
            if(currentAlbum == null){
                currentAlbum = (Album) newCategory(song.getAlbum());
            }
            JSONObject object = createSongJSONObject(song.getId());
            try {
                currentAlbum.getJSONObject().getJSONArray("songs").put(object);
            } catch (JSONException e) {
                continue;
            }
        }
        setChanged();
        notifyObservers(this.models);
    }

    @Override
    public void storeData() {

    }
    @Override
    public void update(Observable observable, Object data) {
        loadData();
    }
}
