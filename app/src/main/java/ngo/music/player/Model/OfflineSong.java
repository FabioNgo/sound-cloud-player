package ngo.music.player.Model;


import org.json.JSONObject;

public class OfflineSong extends Song {


    public OfflineSong(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getName() {
        return getAttribute("title");
    }

    @Override
    public String getArtist() {
        return getAttribute("artist");
    }

    @Override
    public String getAlbum() {
        return getAttribute("album");
    }

    @Override
    public String getLink() {
        return getAttribute("link");
    }

}
