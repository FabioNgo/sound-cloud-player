package ngo.music.player.ModelManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.Model.Song;

public class AlbumManager extends ReadOnlyOfflineCategoryManager implements Observer {


    @Override
    protected int setType() {
        return ALBUM;
    }

    @Override
    protected String setFilename() {
        return "album.json";
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Album[this.models.size()];

        return this.models.toArray(models);
    }

    @Override
    public void loadData() {
        super.loadData();
        if(this.models.size() == 0){
            loadAlbumsFromSongs((Song[]) ModelManager.getInstance(OFFLINE).getAll());
        }
    }

    private void loadAlbumsFromSongs(Song[] songs){

//            createSongJSONObject()

        for(int i=0;i<songs.length;i++){
            Album currentAlbum = null;
            for (Model model: this.models ) {
                Album album = (Album) model;
                if(songs[i].getAttribute("album").equals(album.getAttribute("title"))){
                    currentAlbum = album;
                    break;
                }
            }
            if(currentAlbum == null){
                currentAlbum = (Album) newCategory(songs[i].getAttribute("album"));
            }
            JSONObject object = createSongJSONObject(songs[i].getAttribute("song_id"));
            try {
                currentAlbum.getJSONObject().getJSONArray("songs").put(object);
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
            loadAlbumsFromSongs(songs);
        }
    }
}
