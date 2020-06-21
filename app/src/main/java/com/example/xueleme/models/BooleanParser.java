package com.example.xueleme.models;

import org.json.JSONObject;

public class BooleanParser implements JSONParser<Boolean>  {
    @Override
    public Boolean parse(Object source) {
        return (Boolean) source;
    }

    @Override
    public JSONObject serialize(Boolean data) {
        return null;
    }
}
