package com.example.xueleme.models;

import org.json.JSONObject;

public class StringParser implements JSONParser<String> {
    @Override
    public String parse(Object source) {
        return (String) source;
    }

    @Override
    public JSONObject serialize(String data) {
        return null;
    }
}
