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
public enum MusicType {

    CHILDREN("儿歌"),
    ZH("中文"),
    EN("英文"),
    ROCK("摇滚"),
    LYRIC("抒情");

    public String value;

    MusicType(String value) {
        this.value = value;
    }

}
