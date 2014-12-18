package ngo.music.soundcloudplayer.boundary;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.Adapters.MyPagerAdapter;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends SlidingFragmentActivity {
	private static final String TAG = "DemoActivity";
	private int mTitleRes;
	protected ListFragment mFrag;
	private SlidingUpPanelLayout mLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
	              
	
		setContentView(R.layout.activity_main);
		/**
		 * Sliding Menu (Left2Right)
		 */
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new SampleListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
		Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
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

		

		TextView t = (TextView) findViewById(R.id.name);
		t.setText("Test");
		Button f = (Button) findViewById(R.id.follow);
		f.setText("Button");
		f.setMovementMethod(LinkMovementMethod.getInstance());
		f.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.twitter.com/umanoapp"));
				startActivity(i);
			}
		});
		/**
		 * Tab Sliding
		 */
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
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
