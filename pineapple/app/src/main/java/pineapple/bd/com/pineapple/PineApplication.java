package pineapple.bd.com.pineapple;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 14:39
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 14:39
 * @ModifyDescription : <Content>
 */
public class PineApplication extends Application{

    public static PineApplication mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // 初始化 Bmob SDK
        Bmob.initialize(this, "56d23db34c49ab0334c427c034956b15");
    }
}
