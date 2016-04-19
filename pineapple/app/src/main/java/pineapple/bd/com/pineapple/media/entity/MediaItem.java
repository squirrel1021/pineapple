package pineapple.bd.com.pineapple.media.entity;

import com.devbrackets.android.exomedia.manager.EMPlaylistManager;

/**
 * A custom {@link com.devbrackets.android.exomedia.manager.EMPlaylistManager.PlaylistItem}
 * to hold the information pertaining to the audio and video items
 */
public class MediaItem implements EMPlaylistManager.PlaylistItem {

    private String artworkUrl;
    private String mediaUrl;
    private String title;
    boolean isAudio;
    private String album;
    private String artist;

    public MediaItem(AudioItems.AudioItem audioItem) {
        artworkUrl = audioItem.getArtworkUrl();
        mediaUrl = audioItem.getMediaUrl();
        title = audioItem.getTitle();
        album = audioItem.getAlbum();
        artist = audioItem.getArtist();
        isAudio = true;
    }

    public MediaItem(VideoItems.VideoItem videoItem) {
        artworkUrl = null;
        mediaUrl = videoItem.getMediaUrl();
        title = videoItem.getTitle();
        isAudio = false;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public long getPlaylistId() {
        return 0;
    }

    @Override
    public EMPlaylistManager.MediaType getMediaType() {
        return isAudio ? EMPlaylistManager.MediaType.AUDIO : EMPlaylistManager.MediaType.VIDEO;
    }

    @Override
    public String getMediaUrl() {
        return mediaUrl;
    }

    @Override
    public String getDownloadedMediaUri() {
        return null;
    }

    @Override
    public String getThumbnailUrl() {
        return artworkUrl;
    }

    @Override
    public String getArtworkUrl() {
        return artworkUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public String getArtist() {
        return artist;
    }
}