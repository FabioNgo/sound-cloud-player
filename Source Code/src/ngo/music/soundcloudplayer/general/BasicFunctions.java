package ngo.music.soundcloudplayer.general;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

/**
 *
 * @author Fabio Ngo Class store basic functions ( most-used functions)
 */
public class BasicFunctions {
	/*
	 * Resize Image View to the size
	 */
	public static void ResizeImageView(int width, int height,
			ImageView imageView) {
		Drawable drawable = imageView.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height,
				true);
		imageView.setImageBitmap(scaledBitmap);
	}

	

	/**
	 * Scale image followed by new height
	 * 
	 * @param newHeight
	 * @param imageView
	 */
	public static void ScaleImageViewH(int newHeight, ImageView imageView) {

		Drawable drawable = imageView.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		double percentage = (double)newHeight / bitmap.getHeight();
		int newWidth = (int) (bitmap.getWidth() * percentage);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
				newHeight, true);
		imageView.setImageBitmap(scaledBitmap);
	}

	/**
	 * Scale image followed by new width
	 * 
	 * @param newHeight
	 * @param imageView
	 */
	public static void ScaleImageViewW(int newWidth, ImageView imageView) {

		Drawable drawable = imageView.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		double percentage = (double)newWidth / bitmap.getWidth();
		int newHeight = (int) (bitmap.getHeight() * percentage);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,
				newHeight, true);
		imageView.setImageBitmap(scaledBitmap);
	}

	/**
	 * Convert DP to PX
	 */
	public static int dpToPx(int dp, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return Math.round(dp * density);
	}

	/**
	 * Convert PX to DP
	 */
	public static int pxTodp(int px, Context context) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) Math.round(px / density);
	}

	public static void makeToastTake(String text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// public static void IniPullToRefresh(final Activity activity,
	// ViewGroup viewGroup, View view, final TimerTask timerTask,
	// final PullToRefreshLayout mPullToRefreshLayout) {
	//
	// ActionBarPullToRefresh.from(activity).insertLayoutInto(viewGroup)
	// // We need to insert the PullToRefreshLayout into the Fragment's
	// // ViewGroup
	// .theseChildrenArePullable(view)
	// // We need to mark the ListView and it's Empty View as pullable
	// // This is because they are not direct children of the
	// // ViewGroup
	// .options(
	// Options.create()
	// .refreshingText("Ä�ang láº¥y bÃ i má»›i...")
	// .pullText("KÃ©o xuá»‘ng Ä‘á»ƒ cáº­p nháº­t!")
	// .releaseText("Nháº£ ra Ä‘á»ƒ cáº­p nháº­t!!!")
	// .titleTextColor(android.R.color.black)
	// .progressBarColor(
	// android.R.color.holo_orange_light)
	// .headerBackgroundColor(
	// android.R.color.holo_blue_light)
	// .progressBarStyle(
	// Options.PROGRESS_BAR_STYLE_OUTSIDE)
	// .build()).listener(new OnRefreshListener() {
	// @Override
	// public void onRefreshStarted(View view) {
	// if (isConnectingToInternet(activity
	// .getApplicationContext())) {
	// Timer timer = new Timer();
	// TimerTask task = new TimerTask() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// timerTask.run();
	// }
	// };
	//
	// timer.schedule(task, 1000);
	// } else {
	//
	// Toast.makeText(
	// activity.getApplicationContext(),
	// "INTERNET IS NOT AVAILABLE. THE OLD DATA WILL BE USED ",
	// Toast.LENGTH_LONG).show();
	//
	// mPullToRefreshLayout.setRefreshComplete();
	// }
	// }
	// }).setup(mPullToRefreshLayout);
	// }

	// CHECK IF IS CONNECTION TO INTERNET OR NOT
	public static boolean isConnectingToInternet(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (NetworkInfo element : info) {
					if (element.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}

		}
		return false;
	}

}
