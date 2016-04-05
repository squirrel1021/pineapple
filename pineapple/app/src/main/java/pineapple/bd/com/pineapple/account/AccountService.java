package pineapple.bd.com.pineapple.account;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pineapple.bd.com.pineapple.OnLineType;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.db.User;
import pineapple.bd.com.pineapple.db.UserAuth;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 15:02
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 15:02
 * @ModifyDescription : <Content>
 */
public class AccountService {
    public static final String TAG = AccountService.class.getSimpleName();
    private static AccountService instance;
    private static PineApplication mContext;

    private AccountService() {
    }

    public static AccountService getInstance() {
        AccountService.mContext = PineApplication.mContext;
        if (null == instance) {
            synchronized (AccountService.class) {
                if (null == instance) {
                    return instance = new AccountService();
                } else {
                    return instance;
                }
            }
        } else {
            return instance;
        }
    }

    /**
     * 如果注册类型为自己平台
     *
     * @param identify_unique_id Username
     * @param credential         password --- encode
     */
    public void register(final Context context, final int accountTypeValue, final String identify_unique_id, final String credential,final Callback callback) {

        BmobQuery<UserAuth> query = new BmobQuery<>();
        query.addWhereEqualTo("identify_unique_id", identify_unique_id);
        query.findObjects(mContext, new FindListener<UserAuth>() {
            @Override
            public void onSuccess(List<UserAuth> list) {
                Log.e(TAG, "onSuccess " + list);
                if (null == list || list.size() == 0)
                    doRegister(callback);
                else Toast.makeText(context, R.string.username_exist_tip, Toast.LENGTH_LONG).show();
            }

            private void doRegister(final Callback callback) {
                final UserAuth userAuth = new UserAuth();
                userAuth.setIdentity_type(accountTypeValue);
                userAuth.setCredential(credential);
                userAuth.setIdentify_unique_id(identify_unique_id);
                userAuth.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "onSuccess register " + userAuth.getObjectId());
                        createUser(userAuth,callback);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.e(TAG, "onFailure register " + s);
                        if(null!=callback)
                            callback.onFailure();
                    }
                });
            }

            private void createUser(final UserAuth userAuth,final Callback callback) {
                final User user = new User();
                user.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        userAuth.setUser_id(user.getObjectId());
                        userAuth.setVerified(true);
                        updateAuth(context, userAuth);
                        Toast.makeText(context, R.string.register_success_tip, Toast.LENGTH_LONG).show();
                        if(null!=callback)
                            callback.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if(null!=callback)
                            callback.onFailure();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "onError " + s);
                if(null!=callback)
                    callback.onFailure();
            }
        });
    }

    private void updateAuth(Context context, final UserAuth userAuth) {
        userAuth.update(context, userAuth.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess createUser " + userAuth.getUser_id());
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e(TAG, "onFailure createUser " + s);
            }
        });
    }

    public void login(final Context context, final int accountTypeValue, final String identify_unique_id, final String credential,final Callback callback) {
        BmobQuery<UserAuth> query = new BmobQuery<>();
        query.addWhereEqualTo("identify_unique_id", identify_unique_id);
        query.findObjects(mContext, new FindListener<UserAuth>() {
            @Override
            public void onSuccess(List<UserAuth> list) {
                Log.e(TAG, "onSuccess " + list);
                if (null == list || list.size() == 0) {
                    Toast.makeText(context, R.string.auth_error, Toast.LENGTH_SHORT).show();
                    if(null!=callback)
                        callback.onFailure();
                    return;
                } else {
                    UserAuth currentAuth = null;
                    for (UserAuth uAuth :
                            list) {
                        if (credential.equals(uAuth.getCredential()) && accountTypeValue == uAuth.getIdentity_type()) {
                            uAuth.setOnLineType(OnLineType.ONLINE.value);
                            updateAuth(context, uAuth);
                            currentAuth = uAuth;
                            break;
                        }
                    }
                    if (null == currentAuth) {
                        Toast.makeText(context, R.string.auth_error, Toast.LENGTH_SHORT).show();
                        if(null!=callback)
                            callback.onFailure();
                    }else{
                        //TODO local save Auth
                        Toast.makeText(context, R.string.login_success_tip, Toast.LENGTH_SHORT).show();
                        if(null!=callback)
                            callback.onSuccess();
                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                if(null!=callback)
                    callback.onFailure();
            }
        });

    }

    interface Callback{
        void onSuccess();
        void onFailure();
    }


}
