package ngo.music.player.Controller;

import android.media.audiofx.Visualizer;

import java.util.Observable;

import ngo.music.player.R;
import ngo.music.player.View.MusicPlayerMainActivity;
import ngo.music.player.View.WaveFormView;
import ngo.music.player.service.MusicPlayerService;

/**
 * Created by fabiongo on 1/8/2016.
 */
public class WaveFormController extends Observable {
    public Visualizer visualizer;
    private static WaveFormController instance;
    public static WaveFormController getInstance(){
        if(instance == null){
            instance = new WaveFormController();
        }
        return instance;
    }
    public void setUpVisualizer(){
        int sessionID = MusicPlayerService.getInstance().mediaPlayer.getAudioSessionId();
        visualizer = new Visualizer(sessionID);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        System.out.println("asd");
                        notifyObservers(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }
}
