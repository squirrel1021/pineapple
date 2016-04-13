package pineapple.bd.com.pineapple;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import pineapple.bd.com.pineapple.db.Music;
import pineapple.bd.com.pineapple.utils.FileUtils;
import pineapple.bd.com.pineapple.utils.logUtils.Logs;

public class GenerateTablesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_tables);
        createMusicTable();
    }

    public void createMusicTable() {
        try {

            String json = readFromfile("tables.json", this);

            Logs.e("music:" + json);
            List<Music> musics = parseList(json, new TypeToken<List<Music>>() {
            });

            Logs.e("music:" + musics.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Logs.e(e);
        }
    }

    public String readFromfile(String fileName, Context context) {
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
