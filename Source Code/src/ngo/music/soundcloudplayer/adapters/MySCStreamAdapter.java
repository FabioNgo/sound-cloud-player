package ngo.music.soundcloudplayer.adapters;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.volley.api.AppController;



















import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.api.ApiWrapper;
import ngo.music.soundcloudplayer.api.Http;
import ngo.music.soundcloudplayer.api.Request;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.UserLoginActivity;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.entity.SCSong;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.CircularImageView;
import ngo.music.soundcloudplayer.general.Constants;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MySCStreamAdapter extends ListSongAdapter implements Constants {
	
	
	
	
	public static MySCStreamAdapter instance = null;
	
	
	public MySCStreamAdapter(Context context, int resource, ArrayList<Song> onlineSongs, ApiWrapper wrapper) {
		super(context, resource, onlineSongs, wrapper);
		
		
		// TODO Auto-generated constructor stub
	}

	

}
