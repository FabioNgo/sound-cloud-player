package ngo.music.soundcloudplayer.boundary;

import java.util.ArrayList;
import java.util.Random;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.adapters.MySCTabAdapter;
import ngo.music.soundcloudplayer.adapters.OfflineTabsAdapter;
import ngo.music.soundcloudplayer.adapters.SCExploreTabAdater;
import ngo.music.soundcloudplayer.adapters.SCSearchTabAdater;
import ngo.music.soundcloudplayer.api.Token;
import ngo.music.soundcloudplayer.boundary.fragments.UserDisplayFragment;
import ngo.music.soundcloudplayer.controller.SongController;
import ngo.music.soundcloudplayer.controller.SCUserController;
import ngo.music.soundcloudplayer.controller.UIController;
import ngo.music.soundcloudplayer.entity.Song;
import ngo.music.soundcloudplayer.general.BasicFunctions;
import ngo.music.soundcloudplayer.general.Constants;
import ngo.music.soundcloudplayer.general.States;
import ngo.music.soundcloudplayer.service.MusicPlayerService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

public class MusicPlayerMainActivity extends SlidingFragmentActivity implements
		Constants.UIContant, Constants.UserContant, Constants.MusicService,
		Constants.Appplication {

	protected Fragment mFrag;
	private SlidingUpPanelLayout mLayout;
	private static MusicPlayerMainActivity activity;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	/*
	 * If true : Display Fragment with tab : Trending Music, Audio...... If
	 * False: Display Fragment with tab : My music ......
	 */
	public static int type = 0;
	
	public static final int OFFLINE = 0;
	public static final int SOUNDCLOUD_EXPLORE = 1;
	public static final int MY_SOUNDCLOUD = 2;
	public static final int SOUNDCLOUD_SEARCH = 3;
	//private int type;

	private int defaultTabPosition = 0;
	protected Object mService;
	protected boolean mBound;

	/**
	 * Search Query
	 */
	public static String query;
	/**
	 * Screen's Size
	 */
	public static int screenHeight;
	public static int screenWidth;

	public static MusicPlayerMainActivity getActivity() {
		return activity;
	}
	
	Menu menu;

//	public MusicPlayerMainActivity() {
//		// TODO Auto-generated constructor stub
//		activity = this;
//
//	}
	
	
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = this;
		States.appState = APP_STOPPED;
		super.onCreate(savedInstanceState);

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
//		getDataFromOtherActivity();
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

//	/**
//	 * Get data which transfered from other activity
//	 */
//	private void getDataFromOtherActivity() {
//		try {
//			SoundCloudUserController soundCloudUserController = SoundCloudUserController
//					.getInstance();
//			
//			
//				
//
//			Bundle bundle = getIntent().getExtras();
//			defaultTabPosition = bundle.getInt(Constants.TabContant.DEFAULT_ID);
//
//		} catch (NullPointerException e) {
//
//		}
//	}

	/**
	 * Tab Sliding
	 */
	private void configTabSliding() {

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		FragmentPagerAdapter adapter;
		switch (type){
		case OFFLINE:
			adapter = new OfflineTabsAdapter(getSupportFragmentManager());
			break;
		case SOUNDCLOUD_EXPLORE:
			adapter = new SCExploreTabAdater(getSupportFragmentManager());
			break;
			
		case MY_SOUNDCLOUD:
			adapter =  new MySCTabAdapter(getSupportFragmentManager());
			break;
		case SOUNDCLOUD_SEARCH:
			adapter =  new SCSearchTabAdater(getSupportFragmentManager());
			break;
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
				+ BasicFunctions.dpToPx(68, this);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.lite_player_container, new LitePlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.full_player_container, new FullPlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.play_queue_container, new QueueSongUI())
				.commit();
	}

	/**
	 * Music Player Service
	 */
	private void configMusicPlayerService() {
		// if (!isMyServiceRunning()) {
		Intent musicPlayerServiceIntent = new Intent(this,
				MusicPlayerService.class);
		// bindService(musicPlayerServiceIntent, mConnection,
		// Context.BIND_AUTO_CREATE);
		startService(musicPlayerServiceIntent);
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
		sm.setShadowDrawable(R.drawable.shadow);

		sm.setBehindOffset((int) (BasicFunctions.pxTodp(screenWidth, this)));
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setLogo(R.drawable.logo);
		setSupportActionBar(toolbar);

		if (savedInstanceState == null) {

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

//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		
//		//getMenuInflater().inflate(R.menu.main_menu, menu);
//		System.out.println ("TYPE = " + type);
//		if (type == SOUNDCLOUD_EXPLORE){
//			View v = (View) menu.findItem(R.id.search);
//			//v.setVisibility(View.VISIBLE);
//		}
//		return true;
//	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
//		if (type == SOUNDCLOUD_EXPLORE){
//			View v = (View) menu.findItem(R.id.search);
//			v.setVisibility(View.VISIBLE);
//		}
		/** Get the action view of the menu item whose id is search */
//        View v = (View) menu.findItem(R.id.search).getActionView();
//        
// 
//        /** Get the edit text from the action view */
//        EditText txtSearch = ( EditText ) v.findViewById(R.id.txt_search);
// 
//        /** Setting an action listener */
//        txtSearch.setOnEditorActionListener(new OnEditorActionListener() {
// 
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Toast.makeText(getBaseContext(), "Search : " + v.getText(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//			
//        });
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_shuffle_all:
			ArrayList<Song> songs = SongController.getInstance()
					.getOfflineSongs(false);
			Random random = new Random(System.currentTimeMillis());
			int position = 0;
			try {
				position = Math.abs(random.nextInt())%MusicPlayerService.getInstance().getQueueSize();	
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
			MusicPlayerService.getInstance().playNewSong(position,songs);
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
		super.onStart();

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
