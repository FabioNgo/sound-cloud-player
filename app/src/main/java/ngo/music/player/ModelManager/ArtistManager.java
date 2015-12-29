package ngo.music.player.ModelManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Artist;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Song;

public class ArtistManager extends ReadOnlyOfflineCategoryManager implements Observer{


    @Override
    protected int setType() {
        // TODO Auto-generated method stub
        return ARTIST;
    }

    @Override
    protected String setFilename() {
        return "artists.json";
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Artist[this.models.size()];

        return this.models.toArray(models);
    }
    @Override
    public void loadData() {
        super.loadData();
        if(this.models.size() == 0){
            loadArtistsFromSongs((Song[]) ModelManager.getInstance(OFFLINE).getAll());
        }
    }
    private void loadArtistsFromSongs(Song[] songs){

//            createSongJSONObject()

        for(int i=0;i<songs.length;i++){
            Artist currentArtist = null;
            for (Model model: this.models ) {
                Artist artist = (Artist) model;
                if(songs[i].getAttribute("artist").equals(artist.getAttribute("title"))){
                    currentArtist = artist;
                    break;
                }
            }
            if(currentArtist == null){
                currentArtist = (Artist) newCategory(songs[i].getAttribute("artist"));
            }
            JSONObject object = createSongJSONObject(songs[i].getAttribute("song_id"));
            try {
                currentArtist.getJSONObject().getJSONArray("songs").put(object);
            } catch (JSONException e) {
                continue;
            }
        }
        storeData();
    }
    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof OfflineSongManager){
            Song[] songs = (Song[]) ((OfflineSongManager) observable).getAll();
            loadArtistsFromSongs(songs);
        }
    }
}
