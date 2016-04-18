package pineapple.bd.com.pineapple.http;

import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;

/**
 * Description : <Content><br>
 * CreateTime : 2016/4/18 11:06
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/4/18 11:06
 * @ModifyDescription : <Content>
 */
public class FileUploader implements Callable<Boolean> {

    private String mFilePath;

    private File mFile;

    private UploadListener mUploadListener;

    private static UploadListener defaultUploadListener = new UploadListener() {
        @Override
        public void onSuccess(File file,String url) {

        }

        @Override
        public void onFailure(int i, String s) {

        }
    };

    public FileUploader(String filePath) {
        this(filePath,defaultUploadListener);
    }

    public FileUploader(File file) {
        this(file,defaultUploadListener);
    }

    public FileUploader(String filePath,UploadListener uploadListener) {
        this.mFilePath = filePath;
        this.mUploadListener = uploadListener;
    }

    public FileUploader(File file,UploadListener uploadListener) {
        this.mFile = file;
        this.mUploadListener = uploadListener;
    }

    @Override
    public Boolean call() throws Exception {

        if(!TextUtils.isEmpty(mFilePath)){
            mFile = new File(mFilePath);
        }
        if(null==mFile||!mFile.exists()){
            throw new FileNotFoundException();
        }

        final BmobFile bmobFile = new BmobFile(mFile);
        bmobFile.uploadblock(PineApplication.mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                Logs.e("bmobFile upload onSuccess: " + bmobFile.getFileUrl(PineApplication.mContext));
                FileUploader.this.mUploadListener.onSuccess(mFile,bmobFile.getFileUrl(PineApplication.mContext));
            }

            @Override
            public void onFailure(int i, String s) {
                Logs.e("bmobFile upload onFailure: " + s);
                FileUploader.this.mUploadListener.onFailure(i,s);
            }
        });

        return true;
    }

    public static interface UploadListener{
        void onSuccess(File file,String url);
        void onFailure(int i, String s);
    }
}
