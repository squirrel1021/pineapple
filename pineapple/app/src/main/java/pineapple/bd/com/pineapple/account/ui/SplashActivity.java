package pineapple.bd.com.pineapple.account.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;

import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.entity.ActionType;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.utils.BasicUtils;

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
public class SplashActivity extends Activity {

    public static final String ACTION_TYPE = "actionType";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PineApplication.mContext.setActiveContext(SplashActivity.this,SplashActivity.class.getName());
        setContentView(R.layout.activity_splash);
        findViewById(R.id.btn_register).setOnClickListener(getListener(ActionType.REGISTER));
        findViewById(R.id.btn_login).setOnClickListener(getListener(ActionType.LOGIN));
        //test 时候用来初始化表
//        new User(1l,"kevin","123",1,"kill bx","code").save(this);
      //  new UserAuth(1l,"123",1,1,"123","123",true,System.currentTimeMillis()).save(this);
    }

    @NonNull
    private View.OnClickListener getListener(final ActionType actionType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUtils.sendIntent(SplashActivity.this, AuthActivity.class, ACTION_TYPE, actionType);
            }
        };
    }


}
