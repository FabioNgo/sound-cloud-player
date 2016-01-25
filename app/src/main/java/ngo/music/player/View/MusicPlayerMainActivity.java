package ngo.music.player.View;

import android.content.Intent;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import ngo.music.player.Controller.MusicPlayerServiceController;
import ngo.music.player.Model.Model;
import ngo.music.player.Model.Song;
import ngo.music.player.ModelManager.ModelManager;
import ngo.music.player.ModelManager.OfflineSongManager;
import ngo.music.player.R;
import ngo.music.player.adapters.OfflineTabsAdapter;
import ngo.music.player.View.fragment.real.QueueFragment;
import ngo.music.player.helper.Constants;
import ngo.music.player.helper.Helper;
import ngo.music.player.helper.States;
import ngo.music.player.service.MusicPlayerService;

public class MusicPlayerMainActivity extends AppCompatActivity implements
		Constants.UIContant, Constants.UserContant, Constants.MusicService, Constants.Models,
		Constants.Appplication, NavigationView.OnNavigationItemSelectedListener {



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
//	private FileObserver fileObserver = new FileObserver(Environment.getExternalStorageDirectory().getPath()) {
//		@Override
//		public void onEvent(int event, String path) {
//			if (event == FileObserver.ALL_EVENTS) {
//				((OfflineSongManager) ModelManager.getInstance(OFFLINE)).getSongsFromSDCard();
//			}
//		}
//	};
	public void disableSliding(){
		this.mLayout.setSlidingEnabled(false);
//		this.setSlidingActionBarEnabled(false);
	}
	public void enableSliding(){
		this.mLayout.setSlidingEnabled(true);
//		this.setSlidingActionBarEnabled(true);
	}
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
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		if ("CallFromNoti".equals(intent.getAction())) {

//			UIController.getInstance().updateUiAppChanged(APP_RUNNING);
		}
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setTitle(getResources().getString(R.string.app_name));
		/*
		 * get Screen size
		 */
		getScreesize();

		activity = this;

		// bindService(musicPlayerServiceIntent, mConnection, 0);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		/*
		 * Sliding Up Panel
		 */
		configSlidingUpPanel();

		/*
		 * Tab Sliding
		 */
		configTabSliding();
		/**
		 *
		 */
		configMusicPlayerService();

	}

	/**
	 * Tab Sliding
	 */
	private void configTabSliding() {

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setTextColor(getResources().getColor(R.color.icons));
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
//		while (ListContentFragment.numFragmentsLoading != 0
//				|| PlayerUI.numberPlayerLoading != 0)
//			;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_shuffle_all:
			ArrayList<Model> models = ModelManager.getInstance(OFFLINE).getAll();
			ArrayList<Song> songs = new ArrayList<>();
			for (Model model:models) {
				songs.add((Song) model);
			}
			int position = 0;


			MusicPlayerService.getInstance().playNewSong(position, songs);
			MusicPlayerServiceController.getInstance().setPlayerMode(MODE_SHUFFLE);
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
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {

			if (mLayout != null && mLayout.isPanelExpanded()
					|| mLayout.isPanelAnchored()) {
				mLayout.collapsePanel();
			} else {
				super.onBackPressed();
			}
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		UIController.getInstance().updateUiAppChanged(APP_STOPPED);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		UIController.getInstance().updateUiAppChanged(APP_RUNNING);
//		((OfflineSongManager) ModelManager.getInstance(OFFLINE)).getSongsFromSDCard();
//		fileObserver.startWatching();
		// if (isMyServiceRunning()) {
		// UpdateUiFromServiceController.getInstance().updateUI(APP_START);
		// }
		// musicPlayerService = MusicPlayerService.getInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();
		/*
		 * Music Player Service must be at last
		 */

//		UIController.getInstance().updateUiAppChanged(APP_RUNNING);
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
//		fileObserver.stopWatching();
//		UIController.getInstance().updateUiAppChanged(APP_STOPPED);

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
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
