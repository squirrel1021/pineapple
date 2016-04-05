package pineapple.bd.com.pineapple.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import pineapple.bd.com.pineapple.R;

/**
 * Description : <Content><br>
 * CreateTime : 2016/4/5 14:51
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/4/5 14:51
 * @ModifyDescription : <Content>
 */
public class CardRecyleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater layoutInflater;

    private final int[] res = {R.mipmap.pic1,R.mipmap.pic2,R.mipmap.pic3,R.mipmap.pic1,R.mipmap.pic2};

    public CardRecyleViewAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    static class CardViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image);
        }
    }
    View convertView = null;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        convertView = getReusableView(convertView, R.layout.ide_common_card);
        return new CardViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        clearViewEvent(holder);
        ((CardViewHolder)holder).imageView.setImageResource(res[position]);
    }

    private void clearViewEvent(RecyclerView.ViewHolder holder) {
        //TODO 清除绑定的事件
        ((CardViewHolder)holder).imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return res.length;
    }

    protected View getReusableView(View view, int viewId) {
        if (view != null && view.getId() == viewId)
            return view;

        return layoutInflater.inflate(viewId, null);
    }
}
