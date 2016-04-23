package pineapple.bd.com.pineapple.media.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.event.EMMediaProgressEvent;
import com.devbrackets.android.exomedia.event.EMPlaylistItemChangedEvent;
import com.devbrackets.android.exomedia.listener.EMPlaylistServiceCallback;
import com.devbrackets.android.exomedia.listener.EMProgressCallback;
import com.devbrackets.android.exomedia.manager.EMPlaylistManager;
import com.devbrackets.android.exomedia.service.EMPlaylistService;
import com.devbrackets.android.exomedia.util.TimeFormatUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.media.PlaylistManager;
import pineapple.bd.com.pineapple.media.entity.AudioItems;
import pineapple.bd.com.pineapple.media.entity.MediaItem;
import pineapple.bd.com.pineapple.utils.AESUtil;
import pineapple.bd.com.pineapple.utils.BitmapUtil;
import pineapple.bd.com.pineapple.utils.FileUtils;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;


/**
 * An example activity to show how to implement and audio UI
 * that interacts with the {@link EMPlaylistService} and {@link EMPlaylistManager}
 * classes.
 */
@RuntimePermissions
public class AudioPlayerActivity extends AppCompatActivity implements EMPlaylistServiceCallback, EMProgressCallback {
    public static final String EXTRA_INDEX = "EXTRA_INDEX";
    public static final int PLAYLIST_ID = 4; //Arbitrary, for the example

    private ProgressBar loadingBar;
    private ImageView artworkView;

    private TextView currentPositionView;
    private TextView durationView;

    private SeekBar seekBar;
    private boolean shouldSetDuration;
    private boolean userInteracting;

    private ImageButton previousButton;
    private ImageButton playPauseButton;
    private ImageButton nextButton;

    private PlaylistManager playlistManager;
    private int selectedIndex = 0;

    private Picasso picasso;
    private ImageView blurArtworkView;
    private String mCurrentDataDir;
    private File blurArtworkImagePath;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AudioPlayerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        try {
            mCurrentDataDir = FileUtils.getCurrentDataPath(AudioPlayerActivity.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        retrieveExtras();
        init();
        updateTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        playlistManager.unRegisterServiceCallbacks(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        playlistManager = PineApplication.mContext.getPlaylistManager();
        resumeBlurArtworkImage();
        playlistManager.registerServiceCallbacks(this);


        //Makes sure to retrieve the current playback information
        updateCurrentPlaybackInformation();
    }

    private void resumeBlurArtworkImage() {
        try {
            blurArtworkImagePath = new File(mCurrentDataDir + AESUtil.encrypt("kevin", playlistManager.getCurrentItem().getArtworkUrl()) + ".jpg");
            if (blurArtworkImagePath.exists()) {
                picasso.load(blurArtworkImagePath).into(blurArtworkView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPlaylistItemChanged(EMPlaylistManager.PlaylistItem currentItem, boolean hasNext, boolean hasPrevious) {
        shouldSetDuration = true;
        //Updates the button states
        nextButton.setEnabled(hasNext);
        previousButton.setEnabled(hasPrevious);
        //Loads the new image
//        loadArtworkView(currentItem);
        AudioPlayerActivityPermissionsDispatcher.loadArtworkViewWithCheck(this, currentItem);
        updateTitle(currentItem.getTitle());

        return true;
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void loadArtworkView(final EMPlaylistManager.PlaylistItem currentItem) {
        picasso.load(currentItem.getArtworkUrl()).transform(new BlurTransformer(currentItem.getArtworkUrl())).into(artworkView, new Callback() {
            @Override
            public void onSuccess() {
                RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                ra.setRepeatMode(Animation.INFINITE);
                ra.setRepeatCount(Animation.INFINITE);
                ra.setDuration(12000l);
                ra.setInterpolator(new LinearInterpolator());
                artworkView.startAnimation(ra);
            }

            @Override
            public void onError() {

            }
        });

    }

    class BlurTransformer implements Transformation {

        private String uri;

        public BlurTransformer(String uri) {
            this.uri = uri;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            final Bitmap blur = BitmapUtil.doBlur(source, 25, false);
            setBlurArtworkView(blur);
            cacheBlurView(blur);
            return source;
        }

        @Override
        public String key() {
            return this.uri;
        }
    }

    private void cacheBlurView(Bitmap blur) {
        try {
            if (!blurArtworkImagePath.exists()) {
                FileOutputStream o = new FileOutputStream(blurArtworkImagePath);
                blur.compress(Bitmap.CompressFormat.JPEG, 100, o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBlurArtworkView(final Bitmap blur) {
        new Handler(AudioPlayerActivity.this.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                blurArtworkView.setImageBitmap(blur);
            }
        });
    }


    @Override
    public boolean onMediaStateChanged(EMPlaylistService.MediaState mediaState) {
        switch (mediaState) {
            case STOPPED:
                finish();
                break;

            case RETRIEVING:
            case PREPARING:
                restartLoading();
                break;

            case PLAYING:
                doneLoading(true);
                break;

            case PAUSED:
                doneLoading(false);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onProgressUpdated(EMMediaProgressEvent event) {
        if (shouldSetDuration && event.getDuration() > 0) {
            shouldSetDuration = false;
            setDuration(event.getDuration());
        }

        if (!userInteracting) {
            seekBar.setSecondaryProgress((int) (event.getDuration() * event.getBufferPercentFloat()));
            seekBar.setProgress((int) event.getPosition());
            currentPositionView.setText(TimeFormatUtil.formatMs(event.getPosition()));
        }

        return true;
    }

    /**
     * Makes sure to update the UI to the current playback item.
     */
    private void updateCurrentPlaybackInformation() {
        EMPlaylistItemChangedEvent itemChangedEvent = playlistManager.getCurrentItemChangedEvent();
        if (itemChangedEvent != null) {
            onPlaylistItemChanged(itemChangedEvent.getCurrentItem(), itemChangedEvent.hasNext(), itemChangedEvent.hasPrevious());
        }

        EMPlaylistService.MediaState currentMediaState = playlistManager.getCurrentMediaState();
        if (currentMediaState != EMPlaylistService.MediaState.STOPPED) {
            onMediaStateChanged(currentMediaState);
        }

        EMMediaProgressEvent progressEvent = playlistManager.getCurrentProgress();
        if (progressEvent != null) {
            onProgressUpdated(progressEvent);
        }
    }

    /**
     * Retrieves the extra associated with the selected playlist index
     * so that we can start playing the correct item.
     */
    private void retrieveExtras() {
        Bundle extras = getIntent().getExtras();
        selectedIndex = extras.getInt(EXTRA_INDEX, 0);
    }

    /**
     * Performs the initialization of the views and any other
     * general setup
     */
    private void init() {
        retrieveViews();
        setupListeners();

        picasso = Picasso.with(getApplicationContext());

        boolean generatedPlaylist = setupPlaylistManager();
        startPlayback(generatedPlaylist);
    }


    /**
     * Called when we receive a notification that the current item is
     * done loading.  This will then update the view visibilities and
     * states accordingly.
     *
     * @param isPlaying True if the audio item is currently playing
     */
    private void doneLoading(boolean isPlaying) {
        loadCompleted();
        updatePlayPauseImage(isPlaying);
    }

    /**
     * Updates the Play/Pause image to represent the correct playback state
     *
     * @param isPlaying True if the audio item is currently playing
     */
    private void updatePlayPauseImage(boolean isPlaying) {
        int resId = isPlaying ? R.drawable.exomedia_ic_pause_black : R.drawable.exomedia_ic_play_arrow_black;
        playPauseButton.setImageResource(resId);
    }

    /**
     * Used to inform the controls to finalize their setup.  This
     * means replacing the loading animation with the PlayPause button
     */
    public void loadCompleted() {
        playPauseButton.setVisibility(View.VISIBLE);
        previousButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        loadingBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Used to inform the controls to return to the loading stage.
     * This is the opposite of {@link #loadCompleted()}
     */
    public void restartLoading() {
        playPauseButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);

        loadingBar.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the {@link #seekBar}s max and updates the duration text
     *
     * @param duration The duration of the media item in milliseconds
     */
    private void setDuration(long duration) {
        seekBar.setMax((int) duration);
        durationView.setText(TimeFormatUtil.formatMs(duration));
    }

    /**
     * Retrieves the playlist instance and performs any generation
     * of content if it hasn't already been performed.
     *
     * @return True if the content was generated
     */
    private boolean setupPlaylistManager() {
        playlistManager = PineApplication.mContext.getPlaylistManager();

        //There is nothing to do if the currently playing values are the same
        if (playlistManager.getPlayListId() == PLAYLIST_ID) {
            return false;
        }

        List<MediaItem> mediaItems = new LinkedList<>();
        for (AudioItems.AudioItem item : AudioItems.getItems()) {
            MediaItem mediaItem = new MediaItem(item);
            mediaItems.add(mediaItem);
        }

        playlistManager.setParameters(mediaItems, selectedIndex);
        playlistManager.setPlaylistId(PLAYLIST_ID);

        return true;
    }

    /**
     * Populates the class variables with the views created from the
     * xml layout file.
     */
    private void retrieveViews() {
        loadingBar = (ProgressBar) findViewById(R.id.audio_player_loading);
        artworkView = (ImageView) findViewById(R.id.audio_player_image);
        blurArtworkView = (ImageView) findViewById(R.id.audio_player_blur);

        currentPositionView = (TextView) findViewById(R.id.audio_player_position);
        durationView = (TextView) findViewById(R.id.audio_player_duration);

        seekBar = (SeekBar) findViewById(R.id.audio_player_seek);

        previousButton = (ImageButton) findViewById(R.id.audio_player_previous);
        playPauseButton = (ImageButton) findViewById(R.id.audio_player_play_pause);
        nextButton = (ImageButton) findViewById(R.id.audio_player_next);


    }

    /**
     * Links the SeekBarChanged to the {@link #seekBar} and
     * onClickListeners to the media buttons that call the appropriate
     * invoke methods in the {@link #playlistManager}
     */
    private void setupListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBarChanged());

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokePrevious();
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokePausePlay();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistManager.invokeNext();
            }
        });
    }

    /**
     * Starts the audio playback if necessary.
     *
     * @param forceStart True if the audio should be started from the beginning even if it is currently playing
     */
    private void startPlayback(boolean forceStart) {
        //If we are changing audio files, or we haven't played before then start the playback
        if (forceStart || playlistManager.getCurrentIndex() != selectedIndex) {
            playlistManager.setCurrentIndex(selectedIndex);
            playlistManager.play(0, false);
        }
    }

    /**
     * Listens to the seek bar change events and correctly handles the changes
     */
    private class SeekBarChanged implements SeekBar.OnSeekBarChangeListener {
        private int seekPosition = -1;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            seekPosition = progress;
            currentPositionView.setText(TimeFormatUtil.formatMs(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            userInteracting = true;

            seekPosition = seekBar.getProgress();
            playlistManager.invokeSeekStarted();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            userInteracting = false;

            playlistManager.invokeSeekEnded(seekPosition);
            seekPosition = -1;
        }
    }
}
