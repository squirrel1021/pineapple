package cn.bmob.v3;

import java.io.Serializable;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 15:53
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 15:53
 * @ModifyDescription : <Content>
 */
public class BmobObject implements Serializable {
    public String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
