package com.example.xueleme.models.locals;

import com.example.xueleme.models.responses.TextAndImageContentDetail;

import java.util.List;

public class TextAndImageContent {
    public Integer id;
    public String text;
    public List<String> images;
    public static TextAndImageContent fromDetail(TextAndImageContentDetail detail) {
        TextAndImageContent content = new TextAndImageContent();
        content.id = detail.id;
        content.images = detail.images;
        content.text = detail.text;
        return content;
    }
}
