package com.example.himalaya.interfaces;

import android.os.Trace;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerCallback {

    /**
     * 开始播放
     */
    void onPlayStart();

    /**
     * 暂停播放
     */
    void onPlayPause();

    /**
     * 停止播放
     */
    void onPlayStop();

    /**
     * 播放错误
     */
    void onPlayError();

    /**
     * 下一首
     */
    void onNextPlay(Track track);

    /**
     * 上一首
     */
    void onPrePlay(Track track);

    /**
     * 播放列表
     * @param list 播放器列表数据
     */
    void onListloaded(List<Track> list);

    /**
     * 播放器模式改变
     * @param playMode
     */
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    /**
     * 进度条的改变
     * @param currentProgress
     * @param total
     */
    void onProgressChange(long currentProgress,long total);

    /**
     * 广告正在加载
     */
    void onAdloading();

    /**
     * 广告结束
     */
    void onAdFinshed();

}
