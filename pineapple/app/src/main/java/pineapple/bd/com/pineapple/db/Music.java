package pineapple.bd.com.pineapple.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MUSIC".
 */
public class Music extends cn.bmob.v3.BmobObject  {

    private Long id;
    private Integer mediaType;
    private Integer musicType;
    private String url;
    private String singer;
    private String author;
    private String poster_url;
    private String lyrics_url;
    private String name;
    private Long size;
    private String albums;
    private Integer quality;

    public Music() {
    }

    public Music(Long id) {
        this.id = id;
    }

    public Music(Long id, Integer mediaType, Integer musicType, String url, String singer, String author, String poster_url, String lyrics_url, String name, Long size, String albums, Integer quality) {
        this.id = id;
        this.mediaType = mediaType;
        this.musicType = musicType;
        this.url = url;
        this.singer = singer;
        this.author = author;
        this.poster_url = poster_url;
        this.lyrics_url = lyrics_url;
        this.name = name;
        this.size = size;
        this.albums = albums;
        this.quality = quality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getMusicType() {
        return musicType;
    }

    public void setMusicType(Integer musicType) {
        this.musicType = musicType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getLyrics_url() {
        return lyrics_url;
    }

    public void setLyrics_url(String lyrics_url) {
        this.lyrics_url = lyrics_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

}
