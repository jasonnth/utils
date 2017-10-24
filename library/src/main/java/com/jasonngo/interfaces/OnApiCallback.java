package com.jasonngo.interfaces;


public interface OnApiCallback<T> {

    void onCompleted();

    void onSuccess(T pResult);

    void onFailure(String pError);
}