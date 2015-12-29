package ngo.music.player.ModelManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Category;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Queue;
import ngo.music.player.Model.Song;
import ngo.music.player.helper.Constants;

public class QueueManager extends CategoryManager implements Constants, Constants.SongConstants,
        Constants.Models,
        Constants.MusicService, Constants.Data {

    private String playingSongId ="";
    private int stoppedTime = 0;
    @Override
    protected int setType() {
        return QUEUE;
    }

    @Override
    protected String setFilename() {
        return "queue.json";
    }

    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    public Song[] getAllSong() {
        String id = this.models.get(0).getId();
        Song[] output =  getSongsFromCategory(id);
        // if there is no queue, let queue contain all songs
//        if(output.length == 0){
//            output = (Song[]) ModelManager.getInstance(OFFLINE).getAll();
//            if(output.length != 0){
//                replaceQueue(output);
//            }
//
//
//        }
        return output;
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Queue[this.models.size()];

        return this.models.toArray(models);
    }

    @Override
    public void loadData() {
        super.loadData();
        if(this.models.size() == 0 ){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title","queue");
                jsonObject.put("songs", new JSONArray());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            generate(jsonObject);
            storeData();
        }
    }

    @Override
    public void addSongToCategory(String songID, String categoryID){
        Category category = (Category) get(categoryID);
        JSONObject songObject = new JSONObject();
        try {
            songObject.put("id",songID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = category.getJSONObject().optJSONArray("songs");

        array.put(songObject);
        try {
            category.getJSONObject().put("songs", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }


    public void replaceQueue(Song[] songs){
        JSONArray array = new JSONArray();
        for(int i=0;i<songs.length;i++){
            JSONObject songObject = createSongJSONObject(songs[i].getAttribute("song_id"));
            array.put(songObject);
        }

        try {
            this.models.get(0).getJSONObject().put("songs",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        storeData();
    }
    public void removeSongFromQueue(Song song) {
		// TODO Auto-generated method stub
		String id = song.getAttribute("song_id");
        String cateId = this.models.get(0).getId();
        removeSongFromCategory(id, cateId);
        if(MusicPlayerServiceController.getInstance().getCurrentSong().equals(song)){
            MusicPlayerServiceController.getInstance().setCurrentSong(MusicPlayerServiceController.getInstance().getNextSong());

        }

	}

    public void addSongToQueue(Song song) {
        try {
            JSONArray array = this.models.get(0).getJSONObject().getJSONArray("songs");
            for(int i=0;i<array.length();i++){
                try {
                    if(array.getJSONObject(i).getString("id").equals(song.getAttribute("song_id"))){
                        return;
                    }
                } catch (JSONException e) {
                    continue;
                }
            }
            JSONObject object = new JSONObject();
            object.put("id", song.getAttribute("song_id"));
            array.put(object);
            this.models.get(0).getJSONObject().put("songs", array);
            storeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void clearQueue(){
        String queueId = this.models.get(0).getId();
        removeAllSongFromCategory(queueId);
        this.addSongToQueue(MusicPlayerServiceController.getInstance().getCurrentSong());
        MusicPlayerServiceController.getInstance().computeNextSong();
    }
}
