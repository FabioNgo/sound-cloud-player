package ngo.music.player.ModelManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;

/**
 * Created by Luis Ngo on 16/1/2016.
 */
public class ZingManager extends ZingPlayListManager {
    private static final String LINK_TOP_100_MP3 = "http://mp3.zing.vn/xhr/song?op=get-top&start=0&length=20&id=IWZ9Z088";


    @Override
    public ModelInterface get(String id) {
        return super.get(id);
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

    @Override
    public String generateID(JSONObject object) {
        try {
            return object.getString("song_id_encode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
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
                String id = jsonObject.getString("id");
                JSONObject songJson = getSongInfo(id);
                //data.replace("name","title");
                if (songJson != null) {
                    Model model = (Model) generate(songJson);
                }

                // JSONObject jsonObject = jsonArray.get(i);
                // jsonObject.

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getSongInfo(String id) {
        JSONArray jsonArray = new JSONArray();
        BufferedReader reader = null;
        try {
            String urlStr = "http://api.mp3.zing.vn/api/mobile/song/getsonginfo?requestdata={\"id\":\"" + id + "\"}";
            //System.out.println (urlStr);
            URL url = new URL(urlStr);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            JSONObject jsonObject;
            if (buffer.toString().contains("This content has been removed.")){
                try {
                    jsonObject = getSongInfoExtra(id);
                    System.out.println (jsonObject);
                    return jsonObject;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
               // return null;
            }
            jsonObject = new JSONObject(buffer.toString());
            //System.out.println (jsonObject);

            return jsonObject;
            // return jsonObject.getJSONArray("data");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }
        return new JSONObject();
    }

    /**
     * Get Song Info when shortID is not work
     * Use Long ID to work
     */
    private JSONObject getSongInfoExtra(String id) throws IOException, JSONException {
        String urlStr = "http://mp3.zing.vn/bai-hat/a/" + id + ".html";
        JSONObject jsonObject = new  JSONObject();
        Document doc = Jsoup.connect(urlStr).get();
        Element body = doc.body();
        Element html5Player = body.getElementById("html5player");
        String relHref = html5Player.attr("data-xml"); // == "/"
        String longID = relHref.substring(relHref.lastIndexOf("/")+1, relHref.length());

		/*
		 * Get Link mp3
		 */
        String apiLInk = "http://mp3.zing.vn/html5xml/song-xml/" + longID;
        URL url = new URL(apiLInk);
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer buffer = new StringBuffer();
        int read;
        char[] chars = new char[1024];
        while ((read = reader.read(chars)) != -1)
            buffer.append(chars, 0, read);
        JSONObject dataObject = new JSONObject(buffer.toString()).getJSONArray("data").getJSONObject(0);
        String prefixUrl = "http://mp3.zing.vn/xml/load-song/";

        String linkStream = prefixUrl + dataObject.getJSONArray("source_list").getString(0);
        //System.out.println (linkStream);
		/*
		 * Get Title
		 */
        String title = dataObject.getString("name");
		/*
		 * Get Artist
		 */
        String artist = dataObject.getString("artist");

		/*
		 * Get Albumn
		 */
//		System.out.println (html5Player);
        Element tempElt = body.getElementsByClass("info-song-top").get(0);
        String albumnName = tempElt.getElementsByTag("a").get(1).text();

		/*
		 * Create JSON OBJECT
		 *
		 */
        jsonObject.put("song_id_encode", id);
        jsonObject.put("title", title );
        jsonObject.put("album", albumnName);
        jsonObject.put("duration", 0);
        jsonObject.put("artist", artist);
        JSONObject sourceJsonObject = new JSONObject();
        sourceJsonObject.put("128", linkStream);
        jsonObject.put("source", sourceJsonObject);

        return jsonObject;
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
