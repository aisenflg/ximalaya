package com.example.himalaya.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.himalaya.R;
import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.fragment.RecommendFragment;

public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetworkView;
    private View mEmptyView;
    private OnRetryClickListener mOnRetryClickListener = null;



    public enum UIStatus {
        LOADING,SUCCESS, NETWORK_ERROR,EMPTY,NONE
    }

    public UIStatus mCurrentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        initView();
    }

    public void updateStatus(UIStatus status){
        mCurrentStatus = status;
        //更新ui一定要在主线程上
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    /**
     * 初始化UI
     */
    private void initView() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        //加载中页面
        if (mLoadingView == null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.LOADING ? VISIBLE : GONE);

        //成功页面
        if (mSuccessView == null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        mLoadingView.setVisibility(mCurrentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);

        //网络错误页面
        if (mNetworkView == null) {
            mNetworkView = getNetworkErrorView();
            addView(mNetworkView);
        }
        mNetworkView.setVisibility(mCurrentStatus == UIStatus.NETWORK_ERROR ? VISIBLE : GONE);

        //数据为空页面
        if (mEmptyView==null) {
            mEmptyView = getEmptyView();
            addView(mEmptyView);
        }
        mEmptyView.setVisibility(mCurrentStatus == UIStatus.EMPTY ? VISIBLE : GONE);
    }

    private View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty,this,false);
    }

    private View getNetworkErrorView() {
        View networkError = LayoutInflater.from(getContext()).inflate(R.layout.fragment_network,this,false);
        networkError.findViewById(R.id.network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新获取数据
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.onRetryClick();
                }

            }
        });
        return networkError;
    }

    protected abstract View getSuccessView(ViewGroup container);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading,this,false);
    }

    public void setOnRetryClickListener(OnRetryClickListener listener){
        this.mOnRetryClickListener = listener;
    }

    public interface OnRetryClickListener{
        void onRetryClick();
    }
}
