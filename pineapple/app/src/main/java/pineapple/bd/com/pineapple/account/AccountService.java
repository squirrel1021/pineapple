package pineapple.bd.com.pineapple.account;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import pineapple.bd.com.pineapple.db.UserDao;
import pineapple.bd.com.pineapple.entity.OnLineType;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.db.GreenDaoUtils;
import pineapple.bd.com.pineapple.db.User;
import pineapple.bd.com.pineapple.db.UserAuth;
import pineapple.bd.com.pineapple.db.UserAuthDao;
import pineapple.bd.com.pineapple.utils.FileUtils;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 15:02
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 15:02
 * @ModifyDescription : 提供和用户账户相关的操作
 */
public class AccountService {
    public static final String TAG = AccountService.class.getSimpleName();
    private static AccountService instance;
    private static PineApplication mContext;

    private AccountService() {
    }

    /**
     * 单例模式
     *
     * @return
     */
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
     * 如果注册类型为自己平台，则：
     *
     * @param identify_unique_id Username
     * @param credential         password --- encode
     */
    public void register(final Context context, final int accountTypeValue, final String identify_unique_id, final String credential, final Callback callback) {

        BmobQuery<UserAuth> query = new BmobQuery<>();
        query.addWhereEqualTo("identify_unique_id", identify_unique_id);
        query.findObjects(mContext, new FindListener<UserAuth>() {
            @Override
            public void onSuccess(List<UserAuth> list) {
                Logs.e(TAG, "onSuccess " + list);
                if (null == list || list.size() == 0) {
                    doRegister(callback);
                }else {
                    if (null != callback)
                        callback.onFailure();
                    Toast.makeText(context, R.string.username_exist_tip, Toast.LENGTH_LONG).show();}
            }

            private void doRegister(final Callback callback) {
                final UserAuth userAuth = new UserAuth();
                userAuth.setIdentity_type(accountTypeValue);
                userAuth.setCredential(credential);
                userAuth.setIdentify_unique_id(identify_unique_id);
                userAuth.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Logs.e(TAG, "onSuccess register " + userAuth.getObjectId());
                        createUser(userAuth, callback);

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Logs.e(TAG, "onFailure register " + s);
                        if (null != callback)
                            callback.onFailure();
                    }
                });
            }

            /**
             * 创建用户，并和userAuth用户权限表绑定
             * @param userAuth
             * @param callback
             */
            private void createUser(final UserAuth userAuth, final Callback callback) {
                final User user = new User();
                user.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        userAuth.setUser_id(user.getObjectId());
                        userAuth.setOnLineType(OnLineType.ONLINE.value);
                        user.setUser_id(user.getObjectId());
                        //TODO 激活账户可以通过短信和邮件 后面再加 userAuth.setVerified(true);
                        updateAuth(context, userAuth, null);
                        updateLocalUser(user);
                        updateServerUser(context,user);
                        PineApplication.mCurrentUser = user;
                        PineApplication.mCurrentUserAuth = userAuth;
                        if (null != callback)
                            callback.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        if (null != callback)
                            callback.onFailure();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Logs.e(TAG, "onError " + s);
                if (null != callback)
                    callback.onFailure();
            }
        });
    }

    public void updateLocalUser(final User user) {
        UserDao dao = GreenDaoUtils.getSession().getUserDao();
        List<User> list = dao.queryBuilder().where(UserDao.Properties.User_id.eq(user.getUser_id())).list();
        if (null != list && list.size() != 0) {
            User u = list.get(0);
            //这里可以用反射进行属性注入
            u.setUser_id(user.getUser_id());
            u.setAvatar(user.getAvatar());
            u.setGender(user.getGender());
            u.setHobbies(user.getHobbies());
            u.setJobs(user.getJobs());
            u.setNickname(user.getNickname());
            dao.update(u);
        } else {
            dao.insert(user);
        }
    }

    public void updateServerUser(final Context context, final User user) {

        user.update(context, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Logs.e("onSuccess updateServerUser ");
            }

            @Override
            public void onFailure(int i, String s) {
                Logs.e("onFailure updateServerUser " + s);
            }
        });
    }


    /**
     * 标准登录接口
     *
     * @param context
     * @param accountTypeValue
     * @param identify_unique_id
     * @param credential
     * @param callback
     */
    public void login(final Context context, final int accountTypeValue, final String identify_unique_id, final String credential, final Callback callback) {
        BmobQuery<UserAuth> query = new BmobQuery<>();
        query.addWhereEqualTo("identify_unique_id", identify_unique_id);
        query.findObjects(mContext, new FindListener<UserAuth>() {
            @Override
            public void onSuccess(List<UserAuth> list) {
                Logs.e(TAG, "onSuccess " + list);
                if (null == list || list.size() == 0) {
                    Toast.makeText(context, R.string.auth_error, Toast.LENGTH_SHORT).show();
                    if (null != callback)
                        callback.onFailure();
                    return;
                } else {
                    UserAuth currentAuth = null;
                    for (UserAuth uAuth :
                            list) {
                        if (credential.equals(uAuth.getCredential()) && accountTypeValue == uAuth.getIdentity_type()) {
                            uAuth.setOnLineType(OnLineType.ONLINE.value);
                            updateAuth(context, uAuth,null);
                            currentAuth = uAuth;
                            break;
                        }
                    }
                    if (null == currentAuth) {
                        Toast.makeText(context, R.string.auth_error, Toast.LENGTH_SHORT).show();
                        if (null != callback)
                            callback.onFailure();
                    } else {
                        PineApplication.mCurrentUserAuth = currentAuth;
                        syncUser(context, currentAuth.getUser_id(),callback);

                    }
                }


            }

            @Override
            public void onError(int i, String s) {
                if (null != callback)
                    callback.onFailure();
            }
        });
    }

    public void logout(final Context context,Callback callback){
        PineApplication.mCurrentUserAuth.setOnLineType(OnLineType.OFFLINE.value);
        updateAuth(context, PineApplication.mCurrentUserAuth,callback);
    }

    public void syncUser(final Context context, final String user_id,final Callback callback) {
        BmobQuery<User> userQuery = new BmobQuery<User>();
        userQuery.getObject(context, user_id, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                Logs.e("syncUser onSuccess");
                PineApplication.mCurrentUser = user;
                updateLocalUser(user);
                if (null != callback)
                    callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                Logs.e("syncUser onFailure" + s);
            }
        });

    }

    /**
     * 更新用户权限表
     *
     * @param context
     * @param userAuth
     */
    public void updateAuth(Context context, final UserAuth userAuth, final Callback callback) {
        userAuth.setUpdate_time(System.currentTimeMillis());
        userAuth.update(context, userAuth.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Logs.e(TAG, "onSuccess updateAuth " );
                updateLocalAuth(userAuth);
                if(null!=callback){
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Logs.e(TAG, "onFailure updateAuth " + s);
                if(null!=callback){
                    callback.onFailure();
                }
            }
        });
    }

    public void updateLocalAuth(UserAuth userAuth) {
        Logs.e(TAG, "onSuccess updateAuth " + userAuth.getUser_id());
        //TODO local save Auth
        UserAuthDao dao = GreenDaoUtils.getSession().getUserAuthDao();
        List<UserAuth> list = dao.queryBuilder().where(UserAuthDao.Properties.Identify_unique_id.eq(userAuth.getIdentify_unique_id()), UserAuthDao.Properties.Credential.eq(userAuth.getCredential())).list();
        if (null != list && list.size() != 0) {
            UserAuth uAuth = list.get(0);
            uAuth.setUser_id(userAuth.getUser_id());
            uAuth.setVerified(userAuth.getVerified());
            uAuth.setOnLineType(userAuth.getOnLineType());
            uAuth.setIdentity_type(userAuth.getIdentity_type());
            uAuth.setUpdate_time(System.currentTimeMillis());
            dao.update(uAuth);
            //printDb();
        } else {
            dao.insert(userAuth);
        }
    }

    /**
     * 打印本地数据库存储数据
     */
    void printDb() {
        List<UserAuth> userAuths = GreenDaoUtils.getSession().getUserAuthDao().loadAll();
        for (UserAuth uAuth :
                userAuths) {
            Logs.e(" username: " + uAuth.getIdentify_unique_id() + " psw: " + uAuth.getCredential() + " onlineType: " + uAuth.getOnLineType() + " verified " + uAuth.getVerified());

        }
    }


    /**
     * 获取本地头像存储路径，本来打算按用户名建文件夹存储，因 6.0权限限制，暂未作此处理。
     *
     * @param context
     * @param username
     * @return
     */
    public String getLocalHeaderPath(Context context, String username) {
        return FileUtils.getAccountNamePortrait(context, username);
    }

    /**
     * 拍照输出 临时图片 和cropUri对应，用来解决部分机型不能正常裁剪
     *
     * @param context
     * @param username
     * @return
     */
    public String getLocalHeaderTmpPath(Context context, String username) {
        return FileUtils.getAccountNamePortrait(context, username)
                + ".tmp";
    }


    /**
     * 访问 BMob 后回调给界面刷新
     */
    public interface Callback {
        /**
         * 成功时回调
         */
        void onSuccess();

        /**
         * 失败时回调
         */
        void onFailure();
    }


}
