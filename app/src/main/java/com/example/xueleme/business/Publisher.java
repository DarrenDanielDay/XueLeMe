package com.example.xueleme.business;

import android.util.Log;

import java.util.Collection;
import java.util.HashSet;

/**
 * 观察者模式的发布者
 * @param <TMessage> 发布的消息类型
 */
public class Publisher<TMessage> {
    protected final Collection<Subscriber<TMessage>> subscribers = new HashSet<>();

    public void attach(Subscriber<TMessage> subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
            subscriber.setPublisher(this);
        }
    }
    public void detach(Subscriber<TMessage> subscriber) {
        subscribers.remove(subscriber);
    }

    protected void publish(TMessage message) {
        Log.d("发布消息", message.toString());
        for (Subscriber<TMessage> subscriber: subscribers) {
            Log.d("通知订阅者", subscriber.toString());
            subscriber.onReceive(message);
        }
    }
}
