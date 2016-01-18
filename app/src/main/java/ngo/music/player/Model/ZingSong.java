package ngo.music.player.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Override;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Luis Ngo on 6/1/2016.
 */
public class ZingSong extends Song {


    public ZingSong(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public String getName() {

        return getAttribute("title");
    }
    @Override
    public int getType() {
        return ZING;
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
        JSONObject source = null;
        try {
            source = getJSONObject().getJSONObject("source");
            return source.getString("128");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getDuration() {
        return Integer.parseInt(getAttribute("duration")+"000");
    }

}
