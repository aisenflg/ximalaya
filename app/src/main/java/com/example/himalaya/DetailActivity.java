package com.example.himalaya;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.himalaya.adapter.DetailListAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenter.AlbumDetailPresenter;
;
import com.example.himalaya.view.RoundRectImageView;
import com.example.himalaya.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, UILoader.OnRetryClickListener {

    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumAuthor;
    private TextView mAlbumTitle;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentpage = 1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;
    private long mCurrentId  = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);

    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        //
        if (mUiLoader == null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
            mUiLoader.setOnRetryClickListener(DetailActivity.this);
        }
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.viv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);


    }

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        mDetailList.setLayoutManager(new LinearLayoutManager(this));
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //设置item的上下间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 2);
                outRect.right = UIUtil.dip2px(view.getContext(), 2);
            }
        });
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //根据结果显示UI
        if (tracks == null || tracks.size() == 0) {
            mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
        }
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //更新UI数据
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onNetworkError(int errorCode, String errorMsg) {
        //请求发生错误,显示网络异常状态
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        mCurrentId = album.getId();
        //获取专辑内容详情
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) album.getId(), mCurrentpage);
        }
        //拿数据,显示Loading状态
        if (mUiLoader != null) {
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (mLargeCover != null && null != mLargeCover) {
            //模糊加载
            Glide.with(this).load(album.getCoverUrlLarge()).apply(bitmapTransform(new BlurTransformation(45))).into(mLargeCover);
            if (mSmallCover != null) {
                Glide.with(this).load(album.getCoverUrlLarge()).into(mSmallCover);
            }
        }
        if (mSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlSmall()).into(mSmallCover);
        }

    }

    @Override
    public void onRetryClick() {
        //点击重新加载
        if (mAlbumDetailPresenter != null) {
            mAlbumDetailPresenter.getAlbumDetail((int) mCurrentId, mCurrentpage);
        }
    }
}
