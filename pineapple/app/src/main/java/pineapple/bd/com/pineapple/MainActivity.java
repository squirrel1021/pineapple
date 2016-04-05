package pineapple.bd.com.pineapple;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import pineapple.bd.com.pineapple.adapter.CardRecyleViewAdapter;
import pineapple.bd.com.pineapple.db.GreenDaoUtils;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.db.UserAuthDao;
import pineapple.bd.com.pineapple.utils.BaseCoverActivity;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;

public class MainActivity extends BaseCoverActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupList();
    }

    private void setupList() {
        RecyclerView mListView = (RecyclerView) findViewById(R.id.listView);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(new CardRecyleViewAdapter(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    long lastClickTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastClickTime < 2000l) {
                lastClickTime = System.currentTimeMillis();
               //TODO close activities

            } else {
                lastClickTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, R.string.exit_tips, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
