package pineapple.bd.com.pineapple;

import android.test.AndroidTestCase;
import android.util.Log;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import pineapple.bd.com.pineapple.db.UserAuth;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 16:00
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 16:00
 * @ModifyDescription : <Content>
 */
public class AccountServiceTest extends AndroidTestCase{

    private static final String TAG = AccountServiceTest.class.getSimpleName();

    public void testSaveAuth(){
        Bmob.initialize(PineApplication.mContext, "56d23db34c49ab0334c427c034956b15");
        UserAuth auth = new UserAuth();
        auth.setIdentify_unique_id("kevin2");
        auth.setIdentity_type(0);
        auth.setCredential("123456");
        auth.setVerified(true);
        auth.save(PineApplication.mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "save auth onSuccess ");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, "save auth onFailure " + s);
            }
        });
    }
}
