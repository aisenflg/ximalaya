package com.example.himalaya.base;

public interface IBasePresenter<T> {

    /**
     * 注册UI回调的接口
     * @param t
     */
    void registerViewCallback(T t);

    /**
     * 取消注册
     * @param m
     */
    void unRegisterViewCallback(T t);
}
