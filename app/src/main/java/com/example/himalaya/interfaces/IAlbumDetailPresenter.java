package com.example.himalaya.interfaces;

public interface IAlbumDetailPresenter {
    /**
     * 下拉加载更多内容
     */
    void pull2RefreshMore();

    /**
     * 上拉加载更多
     */
    void loadMore();

    /**
     * 获取专辑详情
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);

    /**
     * 通知ui注册的接口
     * @param detailViewCallback
     */
    void registerViewCallback(IAlbumDetailViewCallback detailViewCallback);

    /**
     * 删除UI通知接口
     * @param detailViewCallback
     */
    void unRegisterViewCallback(IAlbumDetailViewCallback detailViewCallback);

}
