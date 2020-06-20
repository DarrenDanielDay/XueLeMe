package com.example.xueleme.business;

public interface ActionResultHandler<TResult, TError> {
    void onSuccess(TResult result);
    void onError(TError error);
}
