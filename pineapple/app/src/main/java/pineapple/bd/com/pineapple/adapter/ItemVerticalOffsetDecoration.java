package pineapple.bd.com.pineapple.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description : <Content><br>
 * CreateTime : 2016/4/6 9:54
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/4/6 9:54
 * @ModifyDescription : <Content>
 */
public class ItemVerticalOffsetDecoration extends RecyclerView.ItemDecoration{
    private int mItemOffset;

    public ItemVerticalOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemVerticalOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, mItemOffset, 0, mItemOffset);
    }
}
