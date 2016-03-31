package pineapple.bd.com.pineapple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    Button btn_login;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
<<<<<<< HEAD
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

=======
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
>>>>>>> 4251da9e0e405744c8be64b39832e2aada299a5a
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try{
                        Request request = new Request.Builder()
                                .url("http://publicobject.com/helloworld.txt")
                                .build();
                        final OkHttpClient client = new OkHttpClient();
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(response.body().string());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
<<<<<<< HEAD

=======


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
>>>>>>> 4251da9e0e405744c8be64b39832e2aada299a5a
    }


}
