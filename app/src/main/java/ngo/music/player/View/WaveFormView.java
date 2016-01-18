package ngo.music.player.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import ngo.music.player.Controller.WaveFormController;

/**
 * Created by fabiongo on 1/8/2016.
 */
public class WaveFormView extends View {
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();
    private double[] heights;

    public WaveFormView(Context context) {
        super(context);
        init();
    }

    public WaveFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mForePaint.setStrokeWidth(5f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.WHITE);

    }


    public void updateWaveForm() {
        computeDoublesForAllZoomLevels();
        invalidate();
    }
    /**
     *
     * Copy from ringroid
     */
    public void computeDoublesForAllZoomLevels() {
        int numFrames = WaveFormController.getInstance().getNumFrames();
        int[] frameGains = WaveFormController.getInstance().getFrameGains();
        double[] smoothedGains = new double[numFrames];
        if (numFrames == 1) {
            smoothedGains[0] = frameGains[0];
        } else if (numFrames == 2) {
            smoothedGains[0] = frameGains[0];
            smoothedGains[1] = frameGains[1];
        } else if (numFrames > 2) {
            smoothedGains[0] = (frameGains[0] / 2.0) +
                    (frameGains[1] / 2.0);
            for (int i = 1; i < numFrames - 1; i++) {
                smoothedGains[i] = (frameGains[i - 1] / 3.0) +
                        (frameGains[i    ] / 3.0) +
                        (frameGains[i + 1] / 3.0);
            }
            smoothedGains[numFrames - 1] = (frameGains[numFrames - 2] / 2.0) +
                    (frameGains[numFrames - 1] / 2.0);
        }

        // Make sure the range is no more than 0 - 255
        double maxGain = 1.0;
        for (int i = 0; i < numFrames; i++) {
            if (smoothedGains[i] > maxGain) {
                maxGain = smoothedGains[i];
            }
        }
        // Re-calibrate the min to be 5%
        double minGain = 0;


        // Compute the heights
//        int screenWidth = getWidth();
        heights = new double[numFrames];
        double range = maxGain - minGain;
        for (int i = 0; i < numFrames; i++) {
            double value = (smoothedGains[i]) / range;
            if (value < 0.0)
                value = 0.0;
            if (value > 1.0)
                value = 1.0;
            heights[i] = (value * value);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRect.set(0, 0, getWidth(), getHeight());
        if(heights == null){
            return;
        }

        int width = mRect.width();
        int ctr = mRect.height()/2;
        float step =(float)width/heights.length;
        mForePaint.setStrokeWidth(step - 2);
        for (int i = 0; i < heights.length; i++) {
            Paint paint = mForePaint;

            drawWaveformLine(
                    canvas, (int) (i*step),
                    ctr - (int) (heights[i]*ctr/2),
                    ctr + 1 +(int) (heights[i]*ctr/2),
                    paint);
        }
    }



    protected void drawWaveformLine(Canvas canvas,
                                    int x, int y0, int y1,
                                    Paint paint) {
        canvas.drawLine(x, y0, x, y1, paint);
    }
}
