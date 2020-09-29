package com.example.himalaya.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.himalaya.R;
import com.example.himalaya.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        return layoutInflater.inflate(R.layout.fragment_history,container,false);
    }
}
