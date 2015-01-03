package ngo.music.soundcloudplayer.boundary;

import java.io.IOException;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.SoundCloudExploreTabAdater;
import ngo.music.soundcloudplayer.Adapters.TabsAdapter;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.controller.OfflineSongController;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SoundCloudUserController;
import ngo.music.soundcloudplayer.controller.UpdateUiFromServiceController;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class MainActivity extends SlidingFragmentActivity implements
		Constants.UIContant, Constants.UserContant, Constants.MusicService {

	private int mTitleRes;
	protected Fragment mFrag;
	private SlidingUpPanelLayout mLayout;
	private static MainActivity activity;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	/*
	 * If true : Display Fragemnt with tab : Trending Music, Audio...... If
	 * flase: Display Fragment with tab : My music ......
	 */
	public static boolean isExplore = false;

	private int defaultTabPosition = 0;

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

		/*
		 * Get data from other activity
		 */
		getDataFromOtherActivity();
		/*
		 * get Screen size
		 */
		getScreesize();

		activity = this;
		
		// bindService(musicPlayerServiceIntent, mConnection, 0);

		/*
		 * Sliding Menu
		 */
		configSlidingMenu(savedInstanceState);

		/*
		 * Sliding Up Panel
		 */
		configSlidingUpPanel();

		/*
		 * Tab Sliding
		 */
		configTabSliding();
		/*
		 * Music Player Service
		 */
		configMusicPlayerService();
	}

	

	/**
	 * Get data which transfered from other activity
	 */
	private void getDataFromOtherActivity() {
		try {
			SoundCloudUserController soundCloudUserController = SoundCloudUserController
					.getInstance();
			Token token = soundCloudUserController.getToken();
			if (token == null)
				defaultTabPosition = 2;

			Bundle bundle = getIntent().getExtras();
			defaultTabPosition = bundle.getInt(Constants.TabContant.DEFAULT_ID);

		} catch (NullPointerException e) {

		}
	}

	/**
	 * Tab Sliding
	 */
	private void configTabSliding() {
		
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		FragmentPagerAdapter adapter;
		if (isExplore) {
			adapter = new SoundCloudExploreTabAdater(
					getSupportFragmentManager());
		} else {
			adapter = new TabsAdapter(getSupportFragmentManager());
		}

		pager.setAdapter(adapter);
		pager.setCurrentItem(defaultTabPosition, true);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		isExplore = false;
	}

	/**
	 * Sliding Up Panel
	 */
	private void configSlidingUpPanel() {
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
	}

	/**
	 * Music Player Service
	 */
	private void configMusicPlayerService() {
		if (!isMyServiceRunning()) {
			Intent musicPlayerServiceIntent = new Intent(this,
					MusicPlayerService.class);
			startService(musicPlayerServiceIntent);
		} else {
			UpdateUiFromServiceController.getInstance().updateUI(APP_START);
		}
	}

	/**
	 * @param savedInstanceState
	 */
	private void configSlidingMenu(Bundle savedInstanceState) {
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

			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (Fragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}
	}

	/**
	 * get screen's size;
	 */
	private void getScreesize() {
		// Get the width and length of the screen
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.demo, menu);
		// MenuItem item = menu.findItem(R.id.action_toggle);
		// if (mLayout != null) {
		// if (mLayout.isPanelHidden()) {
		// item.setTitle("Action Show");
		// } else {
		// item.setTitle("Action Hide");
		// }
		// }
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (isMyServiceRunning()) {
		// UpdateUiFromServiceController.getInstance().updateUI(APP_START);
		// }
		// musicPlayerService = MusicPlayerService.getInstance();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("ngo.music.soundcloudplayer.MusicPlayerService"
					.equals(service.service.getClassName())) {
				Log.i("service", "Service runing");
				return true;
			}
		}
		Log.i("service", "Service not runing");
		return false;
	}

	/**
	 * Switch tabs
	 * @param tab
	 */
	public void switchTab(int tab){
		pager.setCurrentItem(tab);
		configTabSliding();
	}
}
