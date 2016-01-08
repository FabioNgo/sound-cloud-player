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


    private int[] values;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();
    private int length;

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
        values = null;
        length = 0;
        mForePaint.setStrokeWidth(3f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.BLUE);
        mForePaint.setShadowLayer(3f,3f,3f,Color.GRAY);

    }

    /**
     *
     * @param values : values of waveform
     * @param length : length of meaning values ( different from values.length)
     */
    public void updateVisualizer(int[] values, int length) {
       this.values = values;
        this.length = length;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values == null) {
            return;
        }

        mPoints = new float[values.length * 4];

        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < this.length - 1; i++) {

            mPoints[i * 4] = mRect.width() * i / (values.length - 1);
            mPoints[i * 4 + 1] = mRect.height() / 2
                    + ((byte) (values[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (values.length - 1);
            mPoints[i * 4 + 3] = mRect.height() / 2
                    + ((byte) (values[i + 1] + 128)) * (mRect.height() / 2)
                    / 128;
        }
        canvas.drawLines(mPoints, mForePaint);
    }


}
