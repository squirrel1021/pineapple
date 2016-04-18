package pineapple.bd.com.pineapple;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.annotation.Suppress;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import pineapple.bd.com.pineapple.db.GreenDaoUtils;
import pineapple.bd.com.pineapple.db.Music;
import pineapple.bd.com.pineapple.entity.MediaType;
import pineapple.bd.com.pineapple.http.FileUploader;
import pineapple.bd.com.pineapple.utils.BasicUtils;
import pineapple.bd.com.pineapple.utils.FileUtils;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;
@RuntimePermissions
public class GenerateTablesActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GenerateTablesActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_tables);
       GenerateTablesActivityPermissionsDispatcher.uploadFileWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void uploadFile() {

        String json = readFromAssets("tables.json", this);
        List<Music> musics = parseList(json, new TypeToken<List<Music>>() {
        });
        Logs.e("music:" + json);

        // GreenDaoUtils.insert(GreenDaoUtils.getSession().getMusicDao(), musics);
//            List<Music> ret = GreenDaoUtils.getList(GreenDaoUtils.getSession().getMusicDao());
//
            for (int i=0;i<musics.size();i++) {
                upload(posters[i],musics.get(i));
            }
    }

    String[] posters = {"spring.jpg","love_story.jpg"};


    private void upload(final String file,final Music music) {
        final StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(FileUtils.getCurrentDataPath(PineApplication.mContext)).append('/').append(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Callable<Boolean> wirteCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                    InputStream fIn = getResources().getAssets()
                            .open(file, Context.MODE_WORLD_READABLE);
                    FileUtils.copyFileFromInputStream(fIn,buffer.toString());
                return true;
            }
        };
        Future<Boolean> writerFuture = Executors.newSingleThreadExecutor().submit(wirteCallable);
        try {
            if(writerFuture.get()){
                final FileUploader fileUploader = new FileUploader(buffer.toString(), new FileUploader.UploadListener() {
                    @Override
                    public void onSuccess(File file,String url) {
                        createMusic(music,file.length(),url);
                    }
                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
                Executors.newSingleThreadExecutor().submit(fileUploader);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void createMusic(Music music,long music_size,String poster_url) {
        try {
            music.setPoster_url(poster_url);
            music.setSize(music_size);
            music.setMediaType(MediaType.MP3.value);
            music.save(GenerateTablesActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            Logs.e(e);
        }
    }


    public String readFromAssets(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public static <T> T parseList(String result, TypeToken<T> typeToken) {
        return new Gson().fromJson(result, typeToken.getType());
    }

    public static <T> T parseObject(String result, Class<T> clazz) {
        return new Gson().fromJson(result, clazz);
    }

}
