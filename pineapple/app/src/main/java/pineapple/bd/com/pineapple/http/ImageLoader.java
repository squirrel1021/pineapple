package pineapple.bd.com.pineapple.http;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.utils.FileUtils;

/**
 * Created by kevin on 16-4-9.
 */
public class ImageLoader implements Callable<Boolean>{

    private String url;

    private Response response;

    private String cachedImagePath;

    public ImageLoader(String url,String cachedImagePath){
        this.url = url;
        this.cachedImagePath = cachedImagePath;
    }



    public void display(ImageView imageView){
        imageView.setImageURI(Uri.parse("file:///" + cachedImagePath));
    }

    @Override
    public Boolean call() throws Exception {
        Request request = new Request.Builder().url(url).build();
            response = PineApplication.mHttpClient.newCall(request).execute();
            final ResponseBody responseBody = response.body();
            if(TextUtils.isEmpty(cachedImagePath)){
                cachedImagePath = FileUtils.getGeneratedImagePath();
            }
            FileUtils.writeFile(cachedImagePath, responseBody.byteStream());
            responseBody.close();
        return true;
    }
}
