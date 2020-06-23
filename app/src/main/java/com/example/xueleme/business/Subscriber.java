package com.example.xueleme.business;

/**
 * 观察者模式的观察者（订阅者）接口
 * @param <TMessage> 订阅的消息类型
 */
public interface Subscriber<TMessage> {
    /**
     * 收到消息时的回调
     * @param message 收到的消息
     */
    void onReceive(TMessage message);
}
