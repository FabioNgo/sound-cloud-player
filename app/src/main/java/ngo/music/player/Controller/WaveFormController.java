package ngo.music.player.Controller;

import android.media.audiofx.Visualizer;
import android.util.Log;

import java.util.Observable;

import ngo.music.player.helper.Statistics;
import ngo.music.player.service.MusicPlayerService;

/**
 * Created by fabiongo on 1/8/2016.
 */
public class WaveFormController extends Observable {
    public Visualizer visualizer;
    private static WaveFormController instance;
    private int duration;

    public static WaveFormController getInstance(){
        if(instance == null){
            instance = new WaveFormController();
        }
        return instance;
    }
    public int[] waveformValues = new int[0];
    public int length = 0;
    private int computeNewValue(byte[] bytes){
        Statistics statistics = new Statistics(bytes);
        return (int) statistics.getStdDev();
    }
    public void setUpVisualizer(){
        int sessionID = MusicPlayerService.getInstance().mediaPlayer.getAudioSessionId();
        visualizer = new Visualizer(sessionID);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
//                        System.out.println("asd");
                        int newValue = computeNewValue(bytes);
//                        Log.e("value", String.valueOf(newValue));
                        if(length<waveformValues.length) {
                            waveformValues[length] = newValue;
                            length++;
                        }
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, 1000, true, false);
//        visualizer.setEnabled(true);
    }


    public void setDuration(int duration) {
        this.duration = duration;
        waveformValues = new int[duration/1000];
        length = 0;
    }
}
