package com.example.xueleme.business;

/**
 * 抽象的用户动作
 * @param <TData>   用户操作时涉及的数据类型
 * @param <TResult> 操作期望得到的结果类型
 * @param <TError>  操作失败后的错误类型
 */
public class UserAction<TData, TResult, TError> {
    public TData data;
    public ActionResultHandler<TResult, TError> resultHandler;
    public UserAction(TData data, ActionResultHandler<TResult, TError> handler) {
        this.data = data;
        this.resultHandler = handler;
    }
}
