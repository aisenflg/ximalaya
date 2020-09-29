package com.example.himalaya.presenter;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.himalaya.utils.Constants.COUNT_DEFAULT;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG = "AlbumDetailPresenter" ;
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private static AlbumDetailPresenter sInstance = null;
    private Album mTargetAlbum;

    private AlbumDetailPresenter(){

    }

    public static AlbumDetailPresenter getInstance(){
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sInstance == null) {
                    sInstance = new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        //去根据页码和专辑id获取列表
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, albumId+"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page+"");
        map.put(DTransferConstants.PAGE_SIZE,COUNT_DEFAULT + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG,"tracks size---->"+tracks.size());
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG,"ERROR CODE --->" + errorCode);
                LogUtil.d(TAG,"ERROR MSG --->" + errorMsg);
                handlerError(errorCode,errorMsg);
            }
        });

    }

    /**
     * 如果发生错误,通知UI
     * @param errorCode
     * @param errorMsg
     */
    private void handlerError(int errorCode,String errorMsg){
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onNetworkError(errorCode,errorMsg);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback mCallback : mCallbacks) {
            mCallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargetAlbum != null) {
                detailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
            mCallbacks.remove(detailViewCallback);
    }

    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum = targetAlbum;
    }
}
