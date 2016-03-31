package pineapple.bd.com.pineapple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import cn.bmob.v3.listener.SaveListener;
import pineapple.bd.com.pineapple.db.UserAuth;

public class MainActivity extends AppCompatActivity {
    Button btn_login;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // used on register!
       // AccountService.getInstance().register(this,AccountType.PINEAPPLE,"kevin","123456");

    }

    public void testSaveAuth(){
//        Bmob.initialize(PineApplication.mContext, "56d23db34c49ab0334c427c034956b15");
        UserAuth auth = new UserAuth();
//        auth.setIdentify_unique_id("kevin2");
//        auth.setIdentity_type(0);
//        auth.setCredential("123456");
//        auth.setVerified(true);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }


}
