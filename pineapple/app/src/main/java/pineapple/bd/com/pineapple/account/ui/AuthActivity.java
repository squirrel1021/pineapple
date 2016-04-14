package pineapple.bd.com.pineapple.account.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.account.entity.AccountType;
import pineapple.bd.com.pineapple.entity.ActionType;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.utils.AESUtil;
import pineapple.bd.com.pineapple.utils.BaseCoverActivity;
import pineapple.bd.com.pineapple.utils.OnFragmentInteractionListener;
import pineapple.bd.com.pineapple.utils.StringUtils;

public class AuthActivity extends BaseCoverActivity implements OnFragmentInteractionListener {
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
        PineApplication.mContext.setActiveContext(this,AuthActivity.class.getName());
        setupToolbar();
        setupViews();
    }

    /**
     * 初始化导航栏
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupViews() {
        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //根据ActionType切换页面
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

    /**
     * 获取输入框用户名密码，封装成用户认证的实体
     * @return
     */
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

    /**
     * 用来自动登录和记住密码功能
     * @param uAuth
     */
    public void setAuth(UserAuth uAuth){
        mUsername.setText(uAuth.getIdentify_unique_id());
        try {
            mPassword.setText(AESUtil.decrypt(key,uAuth.getCredential()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onFragmentInteraction(Bundle intentBundle) {

    }
}
