package com.example.himalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {

    private List<Album> data = new ArrayList<>();
    private onItemClickListener mListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, final int position) {
        //封装数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v.getId(),data.get(position));
                }
            }
        });
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size() != 0 ? data.size() : 0;
    }

    public void setData(List<Album> albumList) {
        if (data != null) {
            data.clear();
            data.addAll(albumList);
        }
        //更新ui
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到各个控件,设置数据
            //专辑封面
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            //标题
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album_play_count);
            //专辑内容数量
            TextView albumContentCountTv = itemView.findViewById(R.id.album_content_size);

            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount()+"");
            albumContentCountTv.setText(album.getIncludeTrackCount()+"");
            Glide.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);
        }
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.mListener = listener;
    }

    public interface onItemClickListener{
        void onItemClick(int position, Album album);
    }
}
