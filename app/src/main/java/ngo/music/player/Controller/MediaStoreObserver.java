package ngo.music.player.Controller;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Observer Content Resolver to update offline song
 * Created by fabiongo on 1/7/2016.
 */
public class MediaStoreObserver {

    private final Cursor mCursor;
    private final ContentObserver mObserver;
    private boolean mRunning = true;

    private class ObserverWithListener extends ContentObserver
    {
        private final OnChangeListener mListener;

        public ObserverWithListener(OnChangeListener listener)
        {
            super(new Handler());

            mListener = listener;
        }

        @Override
        public void onChange(boolean selfChange)
        {
            if (mRunning)
            {
                mListener.onChange();
            }
        }
    };

    public static MediaStoreObserver getInstance(ContentResolver contentResolver, Uri uri, OnChangeListener listener)
    {
        Cursor c = contentResolver.query(uri, new String[] { "*" }, null, null, null);

        if (!c.moveToFirst())
        {
//            Log.e("Cannot start observer for uri: " + uri);
            return null;
        }

        return new MediaStoreObserver(c, listener);
    }

    public MediaStoreObserver(Cursor c, final OnChangeListener listener)
    {
        mCursor = c;
        mObserver = new ObserverWithListener(listener);
        mCursor.registerContentObserver(mObserver);
    }

    public void stop()
    {
        mCursor.unregisterContentObserver(mObserver);
        mCursor.close();
        mRunning = false;
    }

    public interface OnChangeListener
    {
        public void onChange();
    }
}
