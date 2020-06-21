package com.example.xueleme.models;

/**
 * response和本地的model类适配器接口
 * @param <TResponse> Response JSON的类型
 * @param <TModel>  本地model的类型
 */
public interface ResponseModelAdapter<TResponse, TModel> {
    TModel convert(TResponse response);
}
