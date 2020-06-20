package com.example.xueleme.business;

public class UserAction<TData, TResult, TError> {
    public TData data;
    public ActionResultHandler<TResult, TError> resultHandler;
    public UserAction(TData data, ActionResultHandler<TResult, TError> handler) {
        this.data = data;
        this.resultHandler = handler;
    }
}
