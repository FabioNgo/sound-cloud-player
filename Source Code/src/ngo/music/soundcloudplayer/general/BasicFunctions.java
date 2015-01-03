package ngo.music.soundcloudplayer.general;

import ngo.music.soundcloudplayer.boundary.MainActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	/**
	 * Set Image size of ImageView
	 * @param width width of image. < 0 if not set
	 * @param height height of image. <0 if not set
	 * @param imageView image want to be set size
	 */
	public static void setImageViewSize(int width, int height,
			ImageView imageView) {
		if (width > 0){
			imageView.setMinimumWidth(width);
			imageView.setMaxWidth(width);
		}
		
		if (height > 0){
			imageView.setMinimumHeight(height);
			imageView.setMaxHeight(height);
		}
		
		
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
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		Resources resource = imageView.getResources();
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		String imageType = options.outMimeType;
		
		int resId = imageView.getId();
		Bitmap bitmap = decodeSampledBitmapFromResource(resource, resId, newWidth, newWidth);
//		Drawable drawable = imageView.getDrawable();
//		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//		double percentage = (double)newWidth / bitmap.getWidth();
//		int newHeight = (int) (bitmap.getHeight() * percentage);
//		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth,newHeight, true);
		imageView.setImageBitmap(bitmap);
	}

	public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
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
