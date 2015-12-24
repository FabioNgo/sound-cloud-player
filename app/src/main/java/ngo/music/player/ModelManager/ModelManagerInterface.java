package ngo.music.player.ModelManager;

import org.json.JSONObject;

import ngo.music.player.Model.ModelInterface;

/**
 * Created by fabiongo on 12/24/2015.
 */
public interface ModelManagerInterface {
    /**
     * Get Model with id
     *
     * @param id model's id
     * @return model having same id as input id
     */
    ModelInterface get(String id);

    /**
     * Get Model with index
     *
     * @param index model's index
     * @return model have the index same as input index in the array of models
     */
    ModelInterface get(int index);

    /**
     * Update JSONObject inside one specific model
     *
     * @param id     model's id
     * @param object json object
     */
    void update(String id, JSONObject object);

    /**
     * Remove model with index
     *
     * @param index model's index
     */
    void remove(int index);

    /**
     * Remove model with id
     *
     * @param id model's id
     */
    void remove(String id);

    /**
     * Get number of models
     *
     * @return the number of models
     */
    int size();

    /**
     * Generate model with JSON Object
     *
     * @param object json object
     * @return the generated model
     */
    ModelInterface generate(JSONObject object);

    /**
     * Generate ID for models
     *
     * @return the id depending the calling function moment
     */
    String generateID();

    /**
     * list out all models
     */
    void listModels();

    /**
     * list out all models
     *
     * @param models array of models need to show
     */
    void listModels(ModelInterface[] models);

    /**
     * Generate string to show to user
     *
     * @param model needed to show
     * @return the string of each model
     */
    String modelToString(ModelInterface model);

    /**
     * Load data from json file
     */
    void loadData();

    /**
     * Store data from json file
     */
    void storeData();

    void clearModels();

    ModelInterface[] getAll();
}
