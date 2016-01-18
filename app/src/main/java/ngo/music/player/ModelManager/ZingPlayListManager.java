package ngo.music.player.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.Playlist;

/**
 * Created by Luis Ngo on 8/1/2016.
 */
public  abstract  class ZingPlayListManager extends SongManager {
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
        return ZING;
    }

    /**
     * Generate string to show to user
     *
     * @param model needed to show
     * @return the string of each model
     */
    @Override
    public String modelToString(ModelInterface model) {
        return "";
    }

    /**
     * Load data from json file
     */
    @Override
    public void loadData() {

    }

    /**
     * Store data from json file
     */
    @Override
    public void storeData() {

    }


    //    public Model newCategory(String title, JSONArray listSong) {
//        JSONObject object = new JSONObject();
//        Model output = null;
//        try {
//            object.put("title", title);
//
//            object.put("songs", listSong);
//            output = (Model) generate(object);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return output;
//    }



}
