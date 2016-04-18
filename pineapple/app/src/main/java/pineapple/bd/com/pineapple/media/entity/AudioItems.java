package pineapple.bd.com.pineapple.media.entity;

import java.util.LinkedList;
import java.util.List;

public class AudioItems {
    private static final List<AudioItem> items = new LinkedList<>();

    public static List<AudioItem> addAudioItem(AudioItem audioItem){
        items.add(audioItem);
        return items;
    }

    public static List<AudioItem> getItems() {
        return items;
    }

    public static class AudioItem {
        String title;
        String mediaUrl;
        String artworkUrl;

        public AudioItem(String title, String mediaUrl, String artworkUrl) {
            this.title = title;
            this.mediaUrl = mediaUrl;
            this.artworkUrl = artworkUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public String getArtworkUrl() {
            return artworkUrl;
        }
    }
}
