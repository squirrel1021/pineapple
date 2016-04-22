package pineapple.bd.com.pineapple.media.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.db.Music;
import pineapple.bd.com.pineapple.media.adapter.AudioSelectionListAdapter;
import pineapple.bd.com.pineapple.media.entity.AudioItems;

/**
 * A simple activity that allows the user to select a
 * chapter form "The Count of Monte Cristo" to play
 * (limited to chapters 1 - 4).
 */
public class AudioSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private AudioSelectionListAdapter audioSelectionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_selection_activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.music_repo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        ListView exampleList = (ListView) findViewById(R.id.selection_activity_list);
        audioSelectionListAdapter = new AudioSelectionListAdapter(this);
        exampleList.setAdapter(audioSelectionListAdapter);
        exampleList.setOnItemClickListener(this);
        setupData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupData() {
        BmobQuery<Music> bq = new BmobQuery<Music>();
        bq.findObjects(this, new FindListener<Music>(){
            @Override
            public void onSuccess(List<Music> list) {
                for (Music music: list) {
                    AudioItems.addAudioItem(new AudioItems.AudioItem(music.getName(),music.getUrl(),music.getPoster_url(),music.getAlbums(),music.getSinger()));
                }
                audioSelectionListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startAudioPlayerActivity(position);
    }

    private void startAudioPlayerActivity(int selectedIndex) {
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra(AudioPlayerActivity.EXTRA_INDEX, selectedIndex);
        startActivity(intent);
    }
}