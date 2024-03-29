package pineapple.bd.com.pineapple;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import pineapple.bd.com.pineapple.account.AccountService;
import pineapple.bd.com.pineapple.account.ui.SettingsActivity;
import pineapple.bd.com.pineapple.adapter.CardRecyleViewAdapter;
import pineapple.bd.com.pineapple.adapter.ItemVerticalOffsetDecoration;
import pineapple.bd.com.pineapple.http.ImageLoader;
import pineapple.bd.com.pineapple.interest.ui.InterestActivity;
import pineapple.bd.com.pineapple.utils.BaseCoverActivity;
import pineapple.bd.com.pineapple.utils.BasicUtils;
import pineapple.bd.com.pineapple.utils.BitmapUtil;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;
import pineapple.bd.com.pineapple.widget.CircleImageView;

@RuntimePermissions
public class MainActivity extends BaseCoverActivity implements View.OnClickListener {

    private CircleImageView mHeaderView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private AccountService mAccountService;
    private Dialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PineApplication.mContext.setActiveContext(this,MainActivity.class.getName());
        mAccountService = AccountService.getInstance();
        setupToolbarAndDrawer();
        setupList();
    }

    /**
     * 初始化导航栏和抽屉
     */
    private void setupToolbarAndDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true); // 改变item选中状态
                mDrawerLayout.closeDrawers(); // 关闭导航菜单
                if(menuItem.getItemId()==R.id.settings){
                    BasicUtils.sendIntent(MainActivity.this, SettingsActivity.class);
                }else if(menuItem.getItemId()==R.id.interest_tribe){
                    BasicUtils.sendIntent(MainActivity.this, InterestActivity.class);
                }
                return true;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        View headerLayout = mNavigationView.getHeaderView(0);
        mHeaderView = (CircleImageView)headerLayout.findViewById(R.id.header);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示上传头像的对话框
                showUploadHeaderDialog();
            }
        });

        setHeaderView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu menu = mNavigationView.getMenu();
        int size = menu.size();
        for (int index=0;index<size;index++) {
            menu.getItem(index).setChecked(false);
        }
        menu.getItem(0).setChecked(true);

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void loadLocalHeader(Uri savedUri){
        mHeaderView.setImageURI(savedUri);
    }

    private void setHeaderView() {
       final String localHeaderPath = mAccountService.getLocalHeaderPath(this,PineApplication.mCurrentUserAuth.getIdentify_unique_id());
        if(!TextUtils.isEmpty(localHeaderPath)){
            Uri headerUri = Uri.parse("file:///" + localHeaderPath);
            if(new File(URI.create(headerUri.toString())).exists()){
                MainActivityPermissionsDispatcher.loadLocalHeaderWithCheck(this,headerUri);

            }else if(!TextUtils.isEmpty(PineApplication.mCurrentUser.getAvatar())){
                String imageUrl = PineApplication.mCurrentUser.getAvatar();
                if(!TextUtils.isEmpty(imageUrl)){
                    ImageLoader imageLoader = new ImageLoader(imageUrl, localHeaderPath);
                    Future<Boolean> future = Executors.newSingleThreadExecutor().submit(imageLoader);
                    try {
                        if(future.get()){
                            imageLoader.display(mHeaderView);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }


            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                //检查权限，如果允许调用takePhoto方法
                MainActivityPermissionsDispatcher.takePhotoWithCheck(this);
                break;
            case R.id.choose_albums:
                choosePicture();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;

            default:
                break;
        }
    }

    private void setupList() {
        RecyclerView mListView = (RecyclerView) findViewById(R.id.listView);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.addItemDecoration(new ItemVerticalOffsetDecoration(this, R.dimen.item_offset));
        mListView.setAdapter(new CardRecyleViewAdapter(this));
    }

    public Dialog showUploadHeaderDialog() {
        mDialog = new Dialog(this, R.style.dialog_style);
        mDialog.setContentView(R.layout.dialog_upload_header);
        mDialog.findViewById(R.id.take_photo).setOnClickListener(this);
        mDialog.findViewById(R.id.choose_albums).setOnClickListener(this);
        mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        mDialog.show();
        return mDialog;
    }

    public static final int RESULT_CODE_KITKAT_PHOTO = 321;
    protected static final int RESULT_CODE_PHOTO = 322;
    protected static final int RESULT_CODE_CAMERA = 323;
    protected static final int RESULT_CODE_CLIP = 324;


    /**
     * showRationaleForCamera(final PermissionRequest request) 方法调用后，
     * request.proceed()则takePhoto()被调用
     */
    @NeedsPermission(Manifest.permission.CAMERA)
    protected void takePhoto() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mAccountService.getLocalHeaderTmpPath(this, PineApplication.mCurrentUserAuth.getIdentify_unique_id()))));
        startActivityForResult(intent, RESULT_CODE_CAMERA);
        mDialog.dismiss();
    }


    /**
     * 若未被授权时，申请运行时权限，弹出选择框供选择
     * request.proceed(); 继续
     * request.cancel();  取消
     *
     * @param request
     */
    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request.cancel();
            }
        }).show();
    }

    /**
     * button_deny ,request.cancel();
     */
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    /**
     * 勾选禁止询问被调用
     */
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show();
    }

    /**
     * 从相册选择相片
     */
    protected void choosePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, RESULT_CODE_KITKAT_PHOTO); // 4.4版本
        } else {
            startActivityForResult(intent, RESULT_CODE_PHOTO);
        }
        mDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Uri cropUri = Uri.fromFile(new File(mAccountService.getLocalHeaderTmpPath(this, PineApplication.mCurrentUserAuth.getIdentify_unique_id())));
        final Uri saveUri = Uri.fromFile(new File(mAccountService.getLocalHeaderPath(this, PineApplication.mCurrentUserAuth.getIdentify_unique_id())));

        Logs.e("cropUri:" + cropUri + "\n" + "saveUri:" + saveUri);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CODE_CAMERA:
                    clipPicture(cropUri, saveUri);
                    break;
                case RESULT_CODE_PHOTO:
                    if (data.getData() != null) {
                        clipPicture(data.getData(), saveUri);
                    }

                    break;
                case RESULT_CODE_KITKAT_PHOTO:
                    if (data.getData() != null) {
                        String imagePath = BitmapUtil.getPath(this, data.getData());
                        clipPicture(Uri.parse("file:///" + imagePath), saveUri);
                    }

                    break;

                case RESULT_CODE_CLIP:
                    MainActivityPermissionsDispatcher.uploadHeaderWithCheck(this, saveUri);

                    break;
                default:
                    break;
            }

        }

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    protected void uploadHeader(final Uri saveUri) {
        final BmobFile bmobFile = new BmobFile(new File(URI.create(saveUri.toString())));
        mHeaderView.setImageURI(saveUri);
        bmobFile.uploadblock(MainActivity.this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
                Logs.e("fileUrl:" + bmobFile.getFileUrl(MainActivity.this));
                PineApplication.mCurrentUser.setAvatar(bmobFile.getFileUrl(MainActivity.this));
                mAccountService.updateServerUser(MainActivity.this, PineApplication.mCurrentUser);
            }

            @Override
            public void onProgress(Integer value) {
                // TODO Auto-generated method stub
                // 返回的上传进度（百分比）
                Logs.e("Progress: " + value + "%");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Logs.e("上传文件失败：" + msg);

            }
        });
    }

    /**
     * 裁剪图片
     *
     * @param inData
     * @param outData
     */
    private void clipPicture(Uri inData, Uri outData) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(inData, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outData);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        startActivityForResult(intent, RESULT_CODE_CLIP);
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
