package ngo.music.soundcloudplayer.Adapters.Fragments;

import ngo.music.soundcloudplayer.R;
import ngo.music.soundcloudplayer.boundary.FullPlayerUI;
import ngo.music.soundcloudplayer.general.Constants;
import android.R.fraction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;


public class SingleSwipePageFragment extends Fragment implements Constants.UIContant{

	private static final String ARG_POSITION = "position";

	private int position;

	public static SingleSwipePageFragment newInstance(int position) {
		SingleSwipePageFragment f = new SingleSwipePageFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout tabPage = new FrameLayout(getActivity());
		tabPage.setLayoutParams(params);
		tabPage.setId(TAB_PAGE_CONTAINER_ID);

		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());
		Fragment fragment = new FullPlayerUI();
		
//		TextView v = new TextView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
//		v.setLayoutParams(params);
//		v.setLayoutParams(params);
//		v.setGravity(Gravity.CENTER);
//		v.setBackgroundResource(R.drawable.ic_launcher);
//		v.setText("CARD " + (position + 1));

//		tabPage.addView(v);
		return tabPage;
	}

}
