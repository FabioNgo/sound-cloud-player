package ngo.music.player.ModelManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Song;
import ngo.music.player.helper.Constants;
import ngo.music.player.service.MusicPlayerService;

public class QueueManager extends SingleCategoryManager implements Constants, Constants.SongConstants,
        Constants.Models,
        Constants.MusicService, Constants.Data {
    @Override
    protected int setType() {
        return QUEUE;
    }

    @Override
    protected String setTitle() {
        return "queue";
    }


    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    public void replaceQueue(ArrayList<Song> songs){
        JSONArray array = new JSONArray();
        for (Song song:songs) {
            JSONObject songObject = createSongJSONObject(song.getId());
            array.put(songObject);
        }

        try {
            this.models.get(0).getJSONObject().put("songs",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }

    @Override
    public void removeSongFromCategory(Song song) {
        super.removeSongFromCategory(song);
        if(MusicPlayerServiceController.getInstance().getCurrentSong().equals(song)){
            MusicPlayerServiceController.getInstance().setCurrentSong(MusicPlayerServiceController.getInstance().getNextSong());

        }
    }
    public void clearQueue(){
        String queueId = this.models.get(0).getId();
        removeAllSongFromCategory(queueId);
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(MusicPlayerServiceController.getInstance().getCurrentSong());
        this.addSongsToCategory(songs);
        MusicPlayerServiceController.getInstance().computeNextSong();
    }
}
