package pineapple.bd.com.pineapple.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import pineapple.bd.com.pineapple.ActionType;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.utils.AESUtil;
import pineapple.bd.com.pineapple.utils.BasicUtils;
import pineapple.bd.com.pineapple.utils.OnFragmentInteractionListener;
import pineapple.bd.com.pineapple.utils.StringUtils;

public class AuthActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private static final String TAG = AuthActivity.class.getSimpleName();
    public static final String ACTION_TYPE = "actionType";
    private EditText mUsername;
    private EditText mPassword;

    private ActionType actionType = ActionType.LOGIN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionType = (ActionType)getIntent().getSerializableExtra(ACTION_TYPE);
        setContentView(R.layout.activity_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    private void setupViews() {
        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        if (actionType == ActionType.LOGIN) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.newInstance()).commit();
        } else if (actionType == ActionType.REGISTER) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, RegisterFragment.newInstance()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    private static final String key = "kevin";

    public UserAuth getAuth() {
        if (StringUtils.isBlank(mUsername.getText().toString().trim()) || StringUtils.isBlank(mPassword.getText().toString().trim())) {
            Toast.makeText(AuthActivity.this, getString(R.string.auth_null), Toast.LENGTH_SHORT).show();
        } else {
            try {
                UserAuth userAuth = new UserAuth();
                userAuth.setIdentity_type(AccountType.PINEAPPLE.value);
                userAuth.setIdentify_unique_id(mUsername.getText().toString().trim());
                userAuth.setCredential(AESUtil.encrypt(key, mPassword.getText().toString().trim()));
                return userAuth;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AuthActivity.this, getString(R.string.auth_null), Toast.LENGTH_SHORT).show();
            }

        }
        return null;
    }


    @Override
    public void onFragmentInteraction(Bundle intentBundle) {

    }
}
