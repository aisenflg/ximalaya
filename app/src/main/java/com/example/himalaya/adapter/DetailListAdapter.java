package com.example.himalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mDetailData = new ArrayList<>();
    private ItemClickListener mItemClickListener = null;

    //格式化时间
    private SimpleDateFormat mUpdateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");

    @NonNull
    @Override
    public DetailListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.InnerHolder holder, final int position) {
        //顺序id
        TextView orderTv = holder.itemView.findViewById(R.id.order_text);
        //标题
        TextView titleTv = holder.itemView.findViewById(R.id.detail_item_title);
        //播放次数
        TextView playCountTv = holder.itemView.findViewById(R.id.detail_item_play_count);
        //播放时长
        TextView durationTv = holder.itemView.findViewById(R.id.detail_item_duration);
        //更新日期
        TextView updateDateTv = holder.itemView.findViewById(R.id.detail_item_update_time);

        //设置数据
        Track track = mDetailData.get(position);
        orderTv.setText(track.getOrderNum() + "");
        titleTv.setText(track.getTrackTitle());
        playCountTv.setText(track.getPlayCount() + "");
        durationTv.setText(mDurationFormat.format(track.getDuration()*1000) + "");
        updateDateTv.setText(mUpdateDateFormat.format(track.getUpdatedAt()));

        //设置item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    public void setData(List<Track> tracks) {
        mDetailData.clear();
        mDetailData.addAll(tracks);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener{
        void onItemClick();
    }

    public  class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
