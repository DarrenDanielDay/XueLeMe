package com.example.xueleme.business;

import java.util.function.Consumer;

/**
 * 观察者模式的观察者（订阅者）
 * @param <TMessage> 订阅的消息类型
 */
public final class Subscriber<TMessage> {
    private Consumer<TMessage> consumer;
    private Publisher<TMessage> publisher = null;
    public Subscriber(Consumer<TMessage> consumer) {
        this.consumer = consumer;
    }

    public void setPublisher(Publisher<TMessage> publisher) {
        if (this.publisher != null) {
            this.detach();
        }
        this.publisher = publisher;
    }

    /**
     * 订阅消息。一个Subscriber只能订阅一个Publisher的消息。
     */
    public void attach() {
        if (publisher != null) {
            publisher.attach(this);
        }
    }

    /**
     * 当不想再接收到消息时，调用detach。
     */
    public void detach() {
        if (publisher != null) {
            publisher.detach(this);
        }
    }

    public void onReceive(TMessage message) {
        consumer.accept(message);
    }
}
