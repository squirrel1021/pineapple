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
public enum OnLineType {


    OFFLINE(0),
    ONLINE(1);

    public int value;

    OnLineType(int value) {
        this.value = value;
    }

}
