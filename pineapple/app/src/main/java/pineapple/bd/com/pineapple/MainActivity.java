package pineapple.bd.com.pineapple;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import pineapple.bd.com.pineapple.adapter.CardRecyleViewAdapter;
import pineapple.bd.com.pineapple.adapter.ItemVerticalOffsetDecoration;
import pineapple.bd.com.pineapple.utils.BaseCoverActivity;

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
        mListView.addItemDecoration(new ItemVerticalOffsetDecoration(this,R.dimen.item_offset));
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
                PineApplication.mContext.onClose();

            } else {
                lastClickTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, R.string.exit_tips, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
