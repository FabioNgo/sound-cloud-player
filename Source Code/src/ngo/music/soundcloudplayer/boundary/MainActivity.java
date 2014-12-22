package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.TabsAdapter;
import ngo.music.soundcloudplayer.controller.MusicPlayerController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import ngo.music.soundcloudplayer.service.MusicPlayerService.MusicPlayerServiceBinder;

import com.astuetz.PagerSlidingTabStrip;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity implements
		Constants.UIContant, Constants.UserContant {

	private int mTitleRes;
	protected Fragment mFrag;
	private SlidingUpPanelLayout mLayout;
	private static MainActivity activity;
	private  MusicPlayerService musicPlayerService;
	private Intent intent;
	private boolean mBound = false;
	private BroadcastReceiver receiver;

	/**
	 * Screen's Size
	 */
	public static int screenHeight;
	public static int screenWidth;

	public static MainActivity getActivity() {
		return activity;
	}
	

	public MainActivity() {
		// TODO Auto-generated constructor stub
		activity = this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View decorView = getWindow().getDecorView();
		decorView
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

		setContentView(R.layout.activity_main);
		receiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if(intent.getStringExtra(MusicPlayerService.NEW_SONG)!= "") {
	            	MusicPlayerController.getInstance().updateNewSong();
	            }
	            // do something here.
	        }
	    };
		/**
		 * get screen's size;
		 */

		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
		activity = this;
		/**
		 * Sliding Menu (Left2Right)
		 */
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);

		sm.setBehindOffset((int) (BasicFunctions.pxTodp(screenWidth, this)));
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setLogo(R.drawable.ic_action_github);
		setSupportActionBar(toolbar);

		if (savedInstanceState == null) {

			FrameLayout frame = (FrameLayout) findViewById(R.id.menu_frame);
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new UserDisplayFragment();

			// Bundle bundle = new Bundle();
			// bundle = getIntent().getBundleExtra(USER);
			// bundle.putInt(LAYOUT_WIDTH, frame.getLayoutParams().width);
			// bundle.putInt(LAYOUT_HEIGHT,screenHeight);

			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (Fragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		/**
		 * Sliding Up Panel
		 */
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {

			}

			@Override
			public void onPanelExpanded(View panel) {

			}

			@Override
			public void onPanelCollapsed(View panel) {

			}

			@Override
			public void onPanelAnchored(View panel) {
			}

			@Override
			public void onPanelHidden(View panel) {
			}
		});
		mLayout.setAnchorPoint((float) 0.5);
		RelativeLayout dragview = (RelativeLayout) findViewById(R.id.dragView);

		FrameLayout full_player_container = (FrameLayout) findViewById(R.id.full_player_container);
		full_player_container.getLayoutParams().height = screenHeight;
		FrameLayout play_queue_container = (FrameLayout) findViewById(R.id.play_queue_container);
		play_queue_container.getLayoutParams().height = screenHeight;

		dragview.getLayoutParams().height = screenHeight * 2
				+ BasicFunctions.dpToPx(68, this);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.lite_player_container, new LitePlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.full_player_container, new FullPlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.play_queue_container, new PlayQueueUI()).commit();
		/**
		 * Tab Sliding
		 */
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		/**
		 * Music Player Service
		 */
		musicPlayerService = MusicPlayerService.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.demo, menu);
		MenuItem item = menu.findItem(R.id.action_toggle);
		if (mLayout != null) {
			if (mLayout.isPanelHidden()) {
				item.setTitle("Action Show");
			} else {
				item.setTitle("Action Hide");
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_toggle: {
			if (mLayout != null) {
				if (!mLayout.isPanelHidden()) {
					mLayout.hidePanel();
					item.setTitle("ACtion Show");
				} else {
					mLayout.showPanel();
					item.setTitle("Action Hide");
				}
			}
			return true;
		}
		case R.id.action_anchor: {
			if (mLayout != null) {
				if (mLayout.getAnchorPoint() == 1.0f) {
					mLayout.setAnchorPoint(0.7f);
					mLayout.expandPanel(0.7f);
					item.setTitle("ACtion Anchor Disable");
				} else {
					mLayout.setAnchorPoint(1.0f);
					mLayout.collapsePanel();
					item.setTitle("ACtion Anchor Enable");
				}
			}
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mLayout != null && mLayout.isPanelExpanded()
				|| mLayout.isPanelAnchored()) {
			mLayout.collapsePanel();
		} else {
			super.onBackPressed();
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			MusicPlayerServiceBinder binder = (MusicPlayerServiceBinder) service;
			musicPlayerService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		intent = new Intent(this, MusicPlayerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}
}
