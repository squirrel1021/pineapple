package pineapple.bd.com.pineapple;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;

import java.util.logging.Logger;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;
import pineapple.bd.com.pineapple.account.AccountService;
import pineapple.bd.com.pineapple.account.AccountType;
import pineapple.bd.com.pineapple.db.User;
import pineapple.bd.com.pineapple.db.UserAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        AccountService.getInstance().register(this,AccountType.PINEAPPLE,"kevin","123456");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
