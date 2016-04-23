package pineapple.bd.com.pineapple.interest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.interest.entity.Interest;
import pineapple.bd.com.pineapple.media.ui.AudioSelectionActivity;
import pineapple.bd.com.pineapple.utils.BasicUtils;

public class InterestActivity extends AppCompatActivity {

    private GridView mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupInterestGridView();

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


    private void setupInterestGridView() {
        mGrid = (GridView) findViewById(R.id.grid);
        List<Interest> interests = new ArrayList<>();
        Interest interestMusic = new Interest();
        interestMusic.setName("欧美儿歌");
        interestMusic.setImageResId(R.mipmap.children_music);
        interestMusic.setPlateColor(getResources().getColor(R.color.blue_03a9f4));
        interestMusic.setActionUrl(AudioSelectionActivity.class.getName());
        interests.add(interestMusic);

        Interest interestGame = new Interest();
        interestGame.setName("益智游戏");
        interestGame.setImageResId(R.mipmap.children_game);
        interestGame.setPlateColor(getResources().getColor(R.color.yellow_ff9800));
        interests.add(interestGame);

        Interest interestHourseKeeper = new Interest();
        interestHourseKeeper.setName("家庭管家");
        interestHourseKeeper.setImageResId(R.mipmap.hoursekeeper);
        interestHourseKeeper.setPlateColor(getResources().getColor(R.color.green_009688));
        interests.add(interestHourseKeeper);


        mGrid.setAdapter(new GridAdapter(interests));

    }


    class GridAdapter extends BaseAdapter {

        private List<Interest> interests;

        public GridAdapter(List<Interest> interests) {
            this.interests = interests;
        }


        @Override
        public int getCount() {
            return interests.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Interest interest = interests.get(position);
            View layout = View.inflate(PineApplication.mContext, R.layout.intertest_grid_item, null);
            ImageView mPlateImage = (ImageView) layout.findViewById(R.id.iv_plate);
            TextView mPlateText = (TextView) layout.findViewById(R.id.tv_plate);
            View mPlateColor = layout.findViewById(R.id.color_plate);
            mPlateImage.setImageResource(interest.getImageResId());
            mPlateColor.setBackgroundColor(interest.getPlateColor());
            mPlateText.setText(interest.getName());
            if(!TextUtils.isEmpty(interest.getActionUrl())){
                layout.setOnClickListener(getMusicRepoListener(interest));
            }
            return layout;
        }

        @NonNull
        private View.OnClickListener getMusicRepoListener(final Interest interest) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BasicUtils.startActivity(InterestActivity.this,interest.getActionUrl(),new Intent());
                }
            };
        }
    }

}
