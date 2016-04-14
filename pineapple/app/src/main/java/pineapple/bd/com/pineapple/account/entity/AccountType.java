package pineapple.bd.com.pineapple.account.entity;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 15:15
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 15:15
 * @ModifyDescription : <Content>
 */
public enum AccountType {

    PINEAPPLE(0),
    QQ(1),
    SINA(2),
    WEIXIN(3),
    GITHUB(4);

   public int value;
    String identify_unique_id;//平台唯一标识, PINEAPPLE 平台即为用户名
    String nickname;//平台昵称

    AccountType(int value) {
        this.value = value;
    }

    public void setUniqueId(String identify_unique_id) {
        this.identify_unique_id = identify_unique_id;
    }

    public String getUniqueId(){
        return this.identify_unique_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
