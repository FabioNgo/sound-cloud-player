package ngo.music.player.Model;

import org.json.JSONObject;

public class Queue extends Category {


    public Queue(JSONObject object) {
        super(object);
    }

    @Override
    public int getType() {
        return QUEUE;
    }
}
