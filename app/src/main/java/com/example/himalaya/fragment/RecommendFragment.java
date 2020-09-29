package com.example.himalaya.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.DetailActivity;
import com.example.himalaya.R;
import com.example.himalaya.adapter.RecommendListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallback;
import com.example.himalaya.presenter.AlbumDetailPresenter;
import com.example.himalaya.presenter.RecommendPresenter;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.view.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback, UILoader.OnRetryClickListener {

    private static String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {
        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater,container);
            }
        };
        //获取到逻辑层对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //先要设置接口注册的通知
        mRecommendPresenter.registerViewCallback(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }
        mUiLoader.setOnRetryClickListener(this);
        //返回view给界面显示
        return mUiLoader;
    }

    private View createSuccessView(final LayoutInflater layoutInflater,ViewGroup container) {
        mRootView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
        initView();
        return mRootView;
    }

    private void initView() {
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        //设置布局管理器
        mRecommendRv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        //增加每个item之间的间距
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration(){
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });
        //设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRv.setAdapter(mRecommendListAdapter);
        mRecommendListAdapter.setOnItemClickListener(new RecommendListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position, Album album) {
                AlbumDetailPresenter.getInstance().setTargetAlbum(album);
                startActivity(new Intent(getContext(), DetailActivity.class));

            }
        });
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //当获取到推荐内容时,这个方法就会被调用
        //数据回来以后就是更新ui
        mRecommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册,避免内存泄露
        if (mRecommendPresenter == null) {
            mRecommendPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onRetryClick() {
        //表示网络不佳的时候,用户点击了重试
        //重新获取数据即可
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }
}
