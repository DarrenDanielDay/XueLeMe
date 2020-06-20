package com.example.xueleme.models;

import org.json.JSONObject;

import java.util.Map;

public interface JSONParser<T> {
    T parse(Object source);
    JSONObject serialize(T data);
}
