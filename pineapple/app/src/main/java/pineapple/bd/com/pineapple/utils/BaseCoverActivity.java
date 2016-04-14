package pineapple.bd.com.pineapple.utils;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Description : <Content><br>
 * CreateTime : 2016/4/5 9:55
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/4/5 9:55
 * @ModifyDescription : <Content>
 */
public class BaseCoverActivity extends AppCompatActivity {
    protected ViewGroup mCover;

    public ViewGroup getCover() {
        return this.mCover;
    }

    public void setCover(ViewGroup mCover) {
        this.mCover = mCover;
    }

    public ViewGroup showProgress() {
        if (null == getCover()) {
            LinearLayout mCover = new LinearLayout(this);
            mCover.setGravity(Gravity.CENTER);
            ProgressBar pb = new ProgressBar(this);
            mCover.addView(pb);
            this.addContentView(mCover,
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            this.setCover(mCover);
        }
        this.getCover().setVisibility(View.VISIBLE);
        return this.getCover();
    }

    public ViewGroup showProgress( ProgressBar pb) {
        if (null == this.getCover()) {
            LinearLayout mCover = new LinearLayout(this);
            mCover.setGravity(Gravity.CENTER);
            mCover.addView(pb);
            this.addContentView(mCover,
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            this.setCover(mCover);
        }
        this.getCover().setVisibility(View.VISIBLE);
        return this.getCover();
    }

    public void hideProgress() {
        if (null == this.mCover)
            return;

        if (this.mCover.getVisibility() == View.VISIBLE) {
            this.mCover.setVisibility(View.GONE);
        }
    }
}
