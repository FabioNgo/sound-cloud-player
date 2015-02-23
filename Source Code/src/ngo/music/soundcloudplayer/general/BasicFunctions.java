package ngo.music.soundcloudplayer.general;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import ngo.music.soundcloudplayer.boundary.MusicPlayerMainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		LinearLayout layout = (LinearLayout) toast.getView();
		
		if (layout.getChildCount() > 0) {
		  TextView tv = (TextView) layout.getChildAt(0);
		  tv.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		toast.show();
	}

	

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
	/**
	 * From mSec to second in format hh:mm:ss
	 */
	@SuppressLint("DefaultLocale")
	public static String milisecondToSecondOffset(long mSec) {
		return String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(mSec)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(mSec)));
	}
	/**
	 * From mSec to second in format hh:mm:ss
	 */
	@SuppressLint("DefaultLocale")
	public static String milisecondToMinuteOffset(long mSec) {
		return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(mSec));
		
	}
}
