package com.example.xueleme.models.responses;

import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;

public class TextAndImageContentDetail extends ReflectiveJSONModel<TextAndImageContentDetail> {
    public Integer id;
    public String text;
    public List<String> images;
}
