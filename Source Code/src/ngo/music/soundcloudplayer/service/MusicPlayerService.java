package ngo.music.soundcloudplayer.service;

import java.io.IOException;

import ngo.music.soundcloudplayer.entity.Song;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;

public class MusicPlayerService extends Service implements OnPreparedListener,
		OnErrorListener, OnCompletionListener, OnSeekCompleteListener,
		OnInfoListener, OnBufferingUpdateListener {

	MediaPlayer mediaPlayer = null;

	public MusicPlayerService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.reset();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		String songLink = intent.getExtras().getString("LINK");
		mediaPlayer.reset();
		if(!mediaPlayer.isPlaying()) {
			try {
				mediaPlayer.setDataSource(songLink);
				mediaPlayer.prepareAsync();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return START_STICKY;

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaPlayer != null) {
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		playMedia();
	}

	private void playMedia() {
		// TODO Auto-generated method stub
		if(!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		stopMedia();
	}

	private void stopMedia() {
		// TODO Auto-generated method stub
		if(!mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

}
