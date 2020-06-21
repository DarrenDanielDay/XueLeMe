package com.example.xueleme.models;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser implements JSONParser<Date> {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss'.'SSS'Z'", Locale.CHINA);
    @Override
    public Date parse(Object source) {
        Date date = null;
        try {
            date = format.parse(source.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public JSONObject serialize(Date data) {
        return null;
    }
}
