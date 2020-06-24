package com.example.xueleme.business;

/**
 * 用户操作的回调接口
 * @param <TResult> 操作期望的结果类型
 * @param <TError>  操作失败的错误类型
 */
public interface ActionResultHandler<TResult, TError> {
    void onSuccess(TResult result);
    void onError(TError error);
}
