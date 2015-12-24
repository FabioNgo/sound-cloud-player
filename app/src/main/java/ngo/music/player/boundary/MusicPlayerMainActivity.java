package ngo.music.player.boundary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import ngo.music.player.Controller.UIController;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.OfflineSongManager;
import ngo.music.player.R;
import ngo.music.player.adapters.OfflineTabsAdapter;
import ngo.music.player.boundary.fragment.abstracts.ListContentFragment;
import ngo.music.player.boundary.fragment.real.QueueFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public class MusicPlayerMainActivity extends SlidingFragmentActivity implements
		Constants.UIContant, Constants.UserContant, Constants.MusicService, Constants.Models,
		Constants.Appplication {


	/*
	 * If true : Display Fragment with tab : Trending Music, Audio...... If
	 * False: Display Fragment with tab : My music ......
	 */
	public static int type = 0;
	/**
	 * Search Query
	 */
	public static String query;
	/**
	 * Screen's Size
	 */
	public static int screenHeight;
	public static int screenWidth;
	public static int notiHeight;
	private static MusicPlayerMainActivity activity;
	// private int type;
	protected Fragment mFrag;
	protected Object mService;
	protected boolean mBound;
	Menu menu;
	private SlidingUpPanelLayout mLayout;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private int defaultTabPosition = 0;
	// public MusicPlayerMainActivity() {
	// // TODO Auto-generated constructor stub
	// activity = this;
	//
	// }
	private FileObserver fileObserver = new FileObserver(Environment.getExternalStorageDirectory().getPath()) {
		@Override
		public void onEvent(int event, String path) {
			if (event == FileObserver.ALL_EVENTS) {
				((OfflineSongManager) ModelManager.getInstance(OFFLINE)).getSongsFromSDCard();
			}
		}
	};

	public static MusicPlayerMainActivity getActivity() {
		return activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = this;
		States.appState = APP_STOPPED;
		super.onCreate(savedInstanceState);
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 9) {
			try {
				// StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
				Class<?> strictModeClass = Class.forName(
						"android.os.StrictMode", true, Thread.currentThread()
								.getContextClassLoader());
				Class<?> threadPolicyClass = Class.forName(
						"android.os.StrictMode$ThreadPolicy", true, Thread
								.currentThread().getContextClassLoader());
				Field laxField = threadPolicyClass.getField("LAX");
				Method setThreadPolicyMethod = strictModeClass.getMethod(
						"setThreadPolicy", threadPolicyClass);
				setThreadPolicyMethod.invoke(strictModeClass,
						laxField.get(null));
			} catch (Exception e) {
			}
		}
		View decorView = getWindow().getDecorView();
		decorView
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		if ("CallFromNoti".equals(intent.getAction())) {

			UIController.getInstance().updateUiAppChanged(APP_RUNNING);
		}

		/*
		 * Get data from other activity
		 */
		// getDataFromOtherActivity();
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
		 * Music Player Service must be at last
		 */
		configMusicPlayerService();

	}

	// /**
	// * Get data which transfered from other activity
	// */
	// private void getDataFromOtherActivity() {
	// try {
	// SoundCloudUserController soundCloudUserController =
	// SoundCloudUserController
	// .getInstance();
	//
	//
	//
	//
	// Bundle bundle = getIntent().getExtras();
	// defaultTabPosition = bundle.getInt(Constants.TabContant.DEFAULT_ID);
	//
	// } catch (NullPointerException e) {
	//
	// }
	// }

	/**
	 * Tab Sliding
	 */
	private void configTabSliding() {

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(10);
		FragmentPagerAdapter adapter;
		switch (type) {


			default:
			adapter = new OfflineTabsAdapter(getSupportFragmentManager());
			break;

		}

		pager.setAdapter(adapter);
		pager.setCurrentItem(defaultTabPosition, true);
		pager.setOffscreenPageLimit(10);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		type = 0;
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
				+ Helper.dpToPx(68, this);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.lite_player_container, new LitePlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.full_player_container, new FullPlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.play_queue_container, new QueueFragment())
				.commit();
	}

	/**
	 * Music Player Service
	 */
	private void configMusicPlayerService() {
		// if (!isMyServiceRunning()) {
		while (ListContentFragment.numFragmentsLoading != 0
				|| PlayerUI.numberPlayerLoading != 0)
			;
		// ProgressDialog dialog = new ProgressDialog(this);
		// dialog.setTitle("Loading...");
		// SystemClock.sleep(1000);
		if (!MusicPlayerService.isLoaded) {
			Intent musicPlayerServiceIntent = new Intent(this,
					MusicPlayerService.class);
			// bindService(musicPlayerServiceIntent, mConnection,
			// Context.BIND_AUTO_CREATE);
			startService(musicPlayerServiceIntent);
		}
		// dialog.dismiss();
		// } else {

		// }
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
//		sm.setShadowDrawable(R.drawable.shadow);

		sm.setBehindOffset(Helper.pxTodp(screenWidth, this));
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setLogo(R.drawable.logo);
//		setSupportActionBar(toolbar);

//		if (savedInstanceState == null) {
//
//			FragmentTransaction t = this.getSupportFragmentManager()
//					.beginTransaction();
//			mFrag = new UserDisplayFragment();
//
//			t.replace(R.id.menu_frame, mFrag);
//			t.commit();
//		} else {
//			mFrag = (Fragment) this.getSupportFragmentManager()
//					.findFragmentById(R.id.menu_frame);
//		}
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

		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			notiHeight = getResources().getDimensionPixelSize(resourceId);
		}

	}

	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // TODO Auto-generated method stub
	//
	// //getMenuInflater().inflate(R.menu.main_menu, menu);
	// System.out.println ("TYPE = " + type);
	// if (type == SOUNDCLOUD_EXPLORE){
	// View v = (View) menu.findItem(R.id.search);
	// //v.setVisibility(View.VISIBLE);
	// }
	// return true;
	// }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		// if (type == SOUNDCLOUD_EXPLORE){
		// View v = (View) menu.findItem(R.id.search);
		// v.setVisibility(View.VISIBLE);
		// }
		/** Get the action view of the menu item whose id is search */
		// View v = (View) menu.findItem(R.id.search).getActionView();
		//
		//
		// /** Get the edit text from the action view */
		// EditText txtSearch = ( EditText ) v.findViewById(R.id.txt_search);
		//
		// /** Setting an action listener */
		// txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v, int actionId, KeyEvent
		// event) {
		// Toast.makeText(getBaseContext(), "Search : " + v.getText(),
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }
		//
		//
		// });

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_shuffle_all:
			Song[] songs = (Song[]) ModelManager.getInstance(OFFLINE).getAll();
			Random random = new Random(System.currentTimeMillis());
			int position = 0;
			try {
				position = Math.abs(random.nextInt())
						% MusicPlayerService.getInstance().getQueueSize();
			} catch (Exception e) {
				// TODO: handle exception
			}

			MusicPlayerService.getInstance().playNewSong(position, songs);
			if (!MusicPlayerService.getInstance().isShuffle()) {
				MusicPlayerService.getInstance().setShuffle();
			}
			break;

		case R.id.main_sort_by:
			break;

		case R.id.main_settings:
			break;
		default:
			break;
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		UIController.getInstance().updateUiAppChanged(APP_STOPPED);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UIController.getInstance().updateUiAppChanged(APP_RUNNING);
		((OfflineSongManager) ModelManager.getInstance(OFFLINE)).getSongsFromSDCard();
		fileObserver.startWatching();
		// if (isMyServiceRunning()) {
		// UpdateUiFromServiceController.getInstance().updateUI(APP_START);
		// }
		// musicPlayerService = MusicPlayerService.getInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();
		UIController.getInstance().updateUiAppChanged(APP_RUNNING);
		// Bind to LocalService

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
		fileObserver.stopWatching();
		UIController.getInstance().updateUiAppChanged(APP_STOPPED);
	}

	/**
	 * Switch tabs
	 * 
	 * @param tab
	 */
	public void switchTab(int tab) {
		pager.setCurrentItem(tab);
		configTabSliding();
	}
}
