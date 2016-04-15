package pineapple.bd.com.pineapple.entity;

/**
 * Description : <Content><br>
 * CreateTime : 2016/4/1 17:30
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/4/1 17:30
 * @ModifyDescription : <Content>
 */
public enum MediaType {


    MP3(".mp3"),
    MP4(".mp4"),
    AVI(".avi"),
    RMVB(".rmvb");

    public String value;

    MediaType(String value) {
        this.value = value;
    }

}
