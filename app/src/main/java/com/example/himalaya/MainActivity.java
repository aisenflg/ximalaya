package com.example.himalaya;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.himalaya.adapter.IndicatorAdapter;
import com.example.himalaya.adapter.MainContentAdapter;
import com.example.himalaya.utils.LogUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;


public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        mIndicatorAdapter.setIndicatorTapClickListener(new IndicatorAdapter.onIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"click index is ----->"+ index);
                if (mContentPager != null) {
                    mContentPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initView() {
        mMagicIndicator = this.findViewById(R.id.magic_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);

        //Viewpager
        mContentPager = this.findViewById(R.id.content_pager);
        //创建适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);

        mContentPager.setAdapter(mainContentAdapter);
        //把Viewpager和indicator绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator,mContentPager);
    }
}
