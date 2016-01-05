package ngo.music.player.ModelManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Artist;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Song;

public class ArtistManager extends CategoryManager implements Observer{


    @Override
    protected void initialize() {
        ModelManager.getInstance(OFFLINE).addObserver(this);
    }

    @Override
    protected int setType() {
        // TODO Auto-generated method stub
        return ARTIST;
    }

    @Override
    public void loadData() {
        ArrayList<Model> songs = ModelManager.getInstance(OFFLINE).getAll();
        for (Model model:songs) {
            Song song = (Song) model;
            Artist currentArtist = null;
            for (Model model2: this.models ) {
                Artist artist = (Artist) model2;
                if(song.getAttribute("artist").equals(artist.getAttribute("title"))){
                    currentArtist = artist;
                    break;
                }
            }
            if(currentArtist == null){
                currentArtist = (Artist) newCategory(song.getAttribute("artist"));
            }
            JSONObject object = createSongJSONObject(song.getAttribute("song_id"));
            try {
                currentArtist.getJSONObject().getJSONArray("songs").put(object);
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
        if(observable instanceof OfflineSongManager){
            loadData();
        }
    }
}
