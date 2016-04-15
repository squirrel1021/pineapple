package pineapple.bd.com.pineapple.media;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.devbrackets.android.exomedia.listener.EMAudioFocusCallback;
import com.devbrackets.android.exomedia.service.EMPlaylistService;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.account.ui.SplashActivity;
import pineapple.bd.com.pineapple.media.entity.MediaItem;

/**
 * A simple service that extends {@link EMPlaylistService} in order to provide
 * the application specific information required.
 */
public class AudioService extends EMPlaylistService<MediaItem, PlaylistManager> implements EMAudioFocusCallback {
    private static final int NOTIFICATION_ID = 1564; //Arbitrary
    private static final int FOREGROUND_REQUEST_CODE = 332; //Arbitrary
    private static final float AUDIO_DUCK_VOLUME = 0.1f;

    private Bitmap defaultLargeNotificationImage;
    private Bitmap largeNotificationImage;
    private Bitmap lockScreenArtwork;

    private NotificationTarget notificationImageTarget = new NotificationTarget();
    private LockScreenTarget lockScreenImageTarget = new LockScreenTarget();

    //Picasso is an image loading library (NOTE: google now recommends using glide for image loading)
    private Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        picasso = Picasso.with(getApplicationContext());
    }

    @Override
    protected int getNotificationId() {
        return NOTIFICATION_ID;
    }

    @Override
    protected float getAudioDuckVolume() {
        return AUDIO_DUCK_VOLUME;
    }

    @Override
    protected PlaylistManager getMediaPlaylistManager() {
        return PineApplication.mContext.getPlaylistManager();
    }

    @Override
    protected PendingIntent getNotificationClickPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        return PendingIntent.getActivity(getApplicationContext(), FOREGROUND_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected Bitmap getDefaultLargeNotificationImage() {
        if (defaultLargeNotificationImage == null) {
            defaultLargeNotificationImage  = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }

        return defaultLargeNotificationImage;
    }

    @Nullable
    @Override
    protected Bitmap getDefaultLargeNotificationSecondaryImage() {
        return null;
    }

    @Override
    protected int getNotificationIconRes() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected int getLockScreenIconRes() {
        return R.mipmap.ic_launcher;
    }

    @Override
    protected void updateLargeNotificationImage(int size, MediaItem playlistItem) {
        picasso.load(playlistItem.getThumbnailUrl()).into(notificationImageTarget);
    }

    @Override
    protected void updateLockScreenArtwork(MediaItem playlistItem) {
        picasso.load(playlistItem.getArtworkUrl()).into(lockScreenImageTarget);
    }

    @Nullable
    @Override
    protected Bitmap getLockScreenArtwork() {
        return lockScreenArtwork;
    }

    @Nullable
    @Override
    protected Bitmap getLargeNotificationImage() {
        return largeNotificationImage;
    }

    /**
     * A class used to listen to the loading of the large notification images and perform
     * the correct functionality to update the notification once it is loaded.
     *
     * <b>NOTE:</b> This is a Picasso Image loader class
     */
    private class NotificationTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            largeNotificationImage = bitmap;
            onLargeNotificationImageUpdated();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            largeNotificationImage = null;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            //Purposefully left blank
        }
    }

    /**
     * A class used to listen to the loading of the large lock screen images and perform
     * the correct functionality to update the artwork once it is loaded.
     *
     * <b>NOTE:</b> This is a Picasso Image loader class
     */
    private class LockScreenTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            lockScreenArtwork = bitmap;
            onLockScreenArtworkUpdated();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            lockScreenArtwork = null;
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            //Purposefully left blank
        }
    }
}