package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.TabsAdapter;
import ngo.music.soundcloudplayer.general.BasicFunctions;

import com.astuetz.PagerSlidingTabStrip;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class MainActivity extends SlidingFragmentActivity {
	private static final String TAG = "DemoActivity";
	private int mTitleRes;
	protected Fragment mFrag;
	private SlidingUpPanelLayout mLayout;
	private MainActivity activity;
	/**
	 * Screen's Size
	 */
	public static int screenHeight;
	public static int screenWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		decorView
				.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

		setContentView(R.layout.activity_main);
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

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setLogo(R.drawable.ic_action_github);
		setSupportActionBar(toolbar);
		
		
		/**
		 * Sliding Up Panel
		 */
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mLayout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				Log.i(TAG, "onPanelSlide, offset " + slideOffset);
			}

			@Override
			public void onPanelExpanded(View panel) {
				Log.i(TAG, "onPanelExpanded");

			}

			@Override
			public void onPanelCollapsed(View panel) {
				Log.i(TAG, "onPanelCollapsed");

			}

			@Override
			public void onPanelAnchored(View panel) {
				Log.i(TAG, "onPanelAnchored");
			}

			@Override
			public void onPanelHidden(View panel) {
				Log.i(TAG, "onPanelHidden");
			}
		});
		mLayout.setAnchorPoint((float) 0.5);
		RelativeLayout dragview = (RelativeLayout) findViewById(R.id.dragView);
		
		FrameLayout lite_player_container = (FrameLayout)findViewById(R.id.lite_player_container);
		lite_player_container.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLayout.expandPanel((float) 0.5);
				
			}
		});

		
		FrameLayout full_player_container = (FrameLayout)findViewById(R.id.full_player_container);
		full_player_container.getLayoutParams().height = screenHeight;
		FrameLayout play_queue_container = (FrameLayout)findViewById(R.id.play_queue_container);
		play_queue_container.getLayoutParams().height = screenHeight;
		
		dragview.getLayoutParams().height = screenHeight*2
				+ BasicFunctions.dpToPx(68, this);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.lite_player_container, new LitePlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.full_player_container, new FullPlayerUI())
				.commit();
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.play_queue_container, new PlayQueueUI())
		.commit();
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
	/**
	 * Tab Sliding
	 */

}
