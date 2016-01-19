package ngo.music.player.View.objects;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.R;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;

/**
 * Created by fabiongo on 1/20/2016.
 */
public class PlayerModeButton extends FloatingActionButton implements Constants.MusicService {
    int playerMode = -1;


    Context context;
    public PlayerModeButton(Context context) {
        super(context);
        this.context = context;
        initialize();
    }


    private void initialize() {
        int size = (int) getResources().getDimension(R.dimen.fab_mini_size);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(size,size);
        int margin = (int) getResources().getDimension(R.dimen.fab_margin_sub);
        layoutParams.setMargins(margin, margin, margin, margin);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.setLayoutParams(layoutParams);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayerServiceController.getInstance().setPlayerMode(playerMode);

            }
        });

        this.hide();
    }

    public void setPlayerMode(int playerMode) {
        this.playerMode = playerMode;
        switch (this.playerMode){
            case MODE_SHUFFLE:

                this.setImageResource(R.drawable.ic_shuffle);
                break;
            case MODE_LOOP_ONE:
                this.setImageResource(R.drawable.ic_repeat_1);
                break;
            case MODE_IN_ORDER:
                this.setImageResource(R.drawable.ic_repeat);
                break;
            default:
                return;
        }
    }
}
