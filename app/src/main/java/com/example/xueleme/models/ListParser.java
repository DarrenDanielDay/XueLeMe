package com.example.xueleme.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListParser<TItem> implements JSONParser<List<TItem>> {

    private Class<TItem> itemClass;

    public ListParser(Class<TItem> itemClass) {
        this.itemClass = itemClass;
    }

    @Override
    public List<TItem> parse(Object source) {
        List<TItem> itemList = new ArrayList<>();
        List<Object> targetList = (List<Object>) source;
        JSONParser<?> parser = ReflectiveJSONModel.parserOf(this.itemClass);
        for (Object item: targetList) {
            itemList.add((TItem) parser.parse(item));
        }
        return itemList;
    }

    @Override
    public JSONObject serialize(List<TItem> data) {
        return null;
    }
}
