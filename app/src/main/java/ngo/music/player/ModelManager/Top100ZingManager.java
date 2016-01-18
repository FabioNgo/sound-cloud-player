package ngo.music.player.ModelManager;

/**
 * Created by Luis Ngo on 8/1/2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.Playlist;
import ngo.music.player.helper.Helper;

public class Top100ZingManager extends ZingPlayListManager {
    private static final String LINK_TOP_100_MP3 = "http://mp3.zing.vn/xhr/song?id=IWZ9Z088&op=get-top";

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
     * Load data from json file
     * ID from Zing MP3 will be add 'Z' in the first of its id in Zing.
     * eg. ID in Zing is 1234 -> ID in model: Z1234
     */
    @Override
    public void loadData() {

        try {
            JSONArray jsonArray = getJson();
            //jsonArray = new JSONArray(Helper.fileContentToString(filename));

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String data = jsonObject.toString();
                //data.replace("name","title");
                Model model = createModel(new JSONObject(data));

               // JSONObject jsonObject = jsonArray.get(i);
               // jsonObject.
                models.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * GET JSON FROM MP3 TOP 100
     */
    public JSONArray getJson(){
        JSONArray jsonArray = new JSONArray();
        BufferedReader reader = null;
        try {
            URL url = new URL(LINK_TOP_100_MP3);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            JSONObject jsonObject = new JSONObject(buffer.toString());
            jsonArray = jsonObject.getJSONArray("data");
            reader.close();
           // return jsonObject.getJSONArray("data");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }
        return jsonArray;
    }







}
