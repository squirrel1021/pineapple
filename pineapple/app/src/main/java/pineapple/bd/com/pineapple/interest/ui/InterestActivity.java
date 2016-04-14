package pineapple.bd.com.pineapple.interest.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import pineapple.bd.com.pineapple.PineApplication;
import pineapple.bd.com.pineapple.R;
import pineapple.bd.com.pineapple.interest.entity.Interest;

public class InterestActivity extends AppCompatActivity {

    private GridView mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    private void setupInterestGridView() {
        mGrid = (GridView) findViewById(R.id.grid);

        ArrayList<Interest> interests = new ArrayList<>();

        for(int i=0;i<6;i++){
            interests.add(new Interest());
        }

        mGrid.setAdapter(new GridAdapter(interests));

    }


    class GridAdapter extends BaseAdapter{

        private List<Interest> interests;

        public GridAdapter(List<Interest> interests){
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

            View layout = View.inflate(PineApplication.mContext, R.layout.intertest_grid_item, null);

            return layout;
        }
    }

}
