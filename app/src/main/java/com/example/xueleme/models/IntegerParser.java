package com.example.xueleme.models;

import org.json.JSONObject;

import java.util.Map;

import kotlin.NotImplementedError;

public class IntegerParser implements JSONParser<Integer> {
    @Override
    public Integer parse(Object source) {
        return ((Double) source).intValue();
    }

    @Override
    public JSONObject serialize(Integer data) {
        return null;
    }
}
