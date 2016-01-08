package ngo.music.player.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

/**
 * Created by fabiongo on 1/8/2016.
 */
public class HeadSetController extends BroadcastReceiver implements Constants.MusicService {
    private static HeadSetController instance = null;
    public static HeadSetController getInstance(){
        if(instance == null){
            instance = new HeadSetController();
        }
        return instance;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        int state = intent.getIntExtra("state", 0);
        switch (state) {
            //unplug
            case 0:
                if (States.musicPlayerState == MUSIC_PLAYING) {

                    MusicPlayerService.getInstance().pause();
                    States.musicPlayerState = MUSIC_HEADSET_UNPLUG;
                }
                break;
            //plug
            case 1:
                if (States.musicPlayerState == MUSIC_HEADSET_UNPLUG) {

                    MusicPlayerService.getInstance().playCurrentSong();
                    States.musicPlayerState = MUSIC_PLAYING;
                }
                break;
            default:
                return;
        }
    }

}
