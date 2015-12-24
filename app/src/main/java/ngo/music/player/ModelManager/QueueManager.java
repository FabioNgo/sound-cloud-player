package ngo.music.player.ModelManager;


import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Song;
import ngo.music.player.helper.Constants;

public class QueueManager extends CategoryManager implements Constants, Constants.SongConstants,
        Constants.SoundCloudExploreConstant, Constants.Models,
        Constants.MusicService, Constants.Data {


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

    public Song getAllSong() {
        return null;
    }
}
