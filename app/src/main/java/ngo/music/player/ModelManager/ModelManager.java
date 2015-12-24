package ngo.music.player.ModelManager;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import ngo.music.player.Model.Album;
import ngo.music.player.Model.Artist;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.ModelInterface;
import ngo.music.player.Model.OfflineSong;
import ngo.music.player.Model.Playlist;
import ngo.music.player.Model.Queue;
import ngo.music.player.helper.Constants;

/**
 * Created by fabiongo on 12/24/2015.
 */
public abstract class ModelManager implements ModelManagerInterface {
    private static ModelManager[] managers;
    /**
     * models to control
     */
    protected ArrayList<Model> models;
    /**
     * type of each controller,
     *
     * @see {@link Constants
     */
    private int type;
    /**
     * Auto increment suffix of id to ensure all ids are different
     */
    private int currentIDSuffix;
    /**
     * file path where store json files
     */
    private String filePath = Environment.getExternalStorageDirectory().getPath() + "/Music Player";
    /**
     * json file name which store data
     */
    private String filename;

    /**
     * In constructor, {@code type}, {@code filename} will be initialized, {@code models} will be loaded from json file
     */
    protected ModelManager() {
        type = setType();
        filename = filePath + "/" + setFilename();
        models = new ArrayList<>();
        currentIDSuffix = 0;
        loadData();
        managers = new ModelManager[Constants.Models.SIZE];
    }

    /**
     * Create new Controller instance to implement singleton pattern
     *
     * @param type - type of Controller
     * @return null if the type does not match declared types in {@link Constants}
     */
    private static ModelManager createNewInstance(int type) {
        // TODO Auto-generated method stub

        switch (type) {
            case Constants.Models.OFFLINE:
                return new OfflineSongManager();
            case Constants.Models.PLAYLIST:
                return new PlaylistManager();
            case Constants.Models.ALBUM:
                return new AlbumManager();
            case Constants.Models.ARTIST:
                return new ArtistManager();
            case Constants.Models.QUEUE:
//                System.out.println(type);
                return new QueueManager();
            default:
                return null;
        }

    }

    /**
     * Get Controller instance depending on the type, to implement singleton pattern
     *
     * @param type - type of Controller
     * @return null if no matched type is passed in
     * @see {@link Constants
     */
    public static ModelManager getInstance(int type) {
        if (managers == null) {
            managers = new ModelManager[Constants.Models.SIZE];
        }
        if (managers[type] == null) {
            ModelManager a = createNewInstance(type);
            managers[type] = a;
        }

        return managers[type];
    }

    /**
     * Read json file as one string
     *
     * @return the generated string from json file
     */
    private String fileContentToString() {
        String result = "";
        BufferedReader br = null;

        try {

            String sCurrentLine;
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File yourFile = new File(filename);
            if (!yourFile.exists()) {
                yourFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(filename);
                printWriter.write("[]");
                printWriter.close();

            }
            br = new BufferedReader(new FileReader(filename));

            while ((sCurrentLine = br.readLine()) != null) {
                result += sCurrentLine + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;

    }

    @Override
    public void clearModels() {
        models.clear();
    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(fileContentToString());

            for (int i = 0; i < jsonArray.length(); i++) {
                Model model = createModel(jsonArray.getJSONObject(i));
                String[] strings = new String[0];
                if (model != null) {
                    strings = model.getId().split("_");
                }
                int value = Integer.valueOf(strings[1]);
                if (value > currentIDSuffix) {
                    currentIDSuffix = value;
                }
                models.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeData() {
        // TODO Auto-generated method stub
        JSONArray jsonArray = new JSONArray();
        for (Model model : models) {
            jsonArray.put(model.getJSONObject());
        }
        try {
            PrintWriter printWriter = new PrintWriter(filename);
            printWriter.write(jsonArray.toString());
            printWriter.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void remove(String id) {
        // TODO Auto-generated method stub
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getId().equals(id)) {
                models.remove(i);
            }
        }
    }

    /**
     * Create Model with JSON Object
     *
     * @param object - JSON Object
     * @return the model containing {@code object}. The type of model depends on the type of controller
     * @see {@link Constants
     */
    private Model createModel(JSONObject object) {
        switch (type) {
            case Constants.Models.OFFLINE:
                return new OfflineSong(object);
            case Constants.Models.PLAYLIST:
                return new Playlist(object);
            case Constants.Models.ALBUM:
                return new Album(object);
            case Constants.Models.ARTIST:
                return new Artist(object);
            case Constants.Models.QUEUE:
                return new Queue(object);
            default:
                return null;
        }
    }

    /**
     * Set the {@code type} for each Controller
     *
     * @return the type users want to set
     */
    protected abstract int setType();

    /**
     * set the  {@code filename} for each Controller
     *
     * @return the filename users want to set
     */
    protected abstract String setFilename();

    @Override
    public void listModels(ModelInterface[] models) {
        // TODO Auto-generated method stub
        if (models.length == 0) {
            System.out.println("Empty List");
        }
        for (int i = 0; i < models.length; i++) {
            System.out.println(String.format("%d. %s", i, modelToString(models[i])));
        }
    }

    @Override
    public void listModels() {
        // TODO Auto-generated method stub

        Model[] models = new Model[this.models.size()];
        this.models.toArray(models);
        listModels(models);
    }

    @Override
    public void remove(int index) {
        // TODO Auto-generated method stub
        models.remove(index);
        storeData();
    }

    @Override
    public void update(String id, JSONObject object) {
        // TODO Auto-generated method stub

        try {
            object.put("id", id);

            Model model = (Model) get(id);
            model.setJSONObject(object);
            storeData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Model get(int index) {
        // TODO Auto-generated method stub
        return models.get(index);
    }

    @Override
    public ModelInterface get(String id) {
        for (Model model : models) {
            if (model.getId().compareTo(id) == 0) {
                return model;
            }
        }
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return models.size();
    }

    @Override
    public String generateID() {
        // TODO Auto-generated method stub
        currentIDSuffix++;
        String id;
        id = String.format("%d_%d", type, currentIDSuffix);
        return id;
    }

    @Override
    public ModelInterface generate(JSONObject object) {
        // TODO Auto-generated method stub

        String id = generateID();
        object.remove("id");
        try {
            object.put("id", id);
        } catch (JSONException e) {
            return null;
        }


        Model model = null;
        switch (type) {
            case 1:
                break;
            default:
                return null;
        }
        models.add(model);
//        storeData();

        return model;
    }

    @Override
    public ModelInterface[] getAll() {
        Model[] models = new Model[this.models.size()];
        return this.models.toArray(models);
    }

}
