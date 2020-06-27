package com.example.xueleme.models;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InvalidClassException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReflectiveJSONModel<T> implements JSONParser<T> {

    @Override
    public T parse(Object source) {
        if (source == null) return null;
        Map<String, Object> sourceDataMap = (Map<String, Object>) source;
        T model = null;
        Class<?> thisClass = this.getClass();
        try {
            try {
                model = (T) thisClass.newInstance();
            } catch (Exception e) {
                model = (T) this;
            }
            Field[] fields = this.getClass().getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object value = sourceDataMap.get(fieldName);
                Class<?> fieldClass = field.getType();
                if (fieldClass.equals(Object.class)) { // field is erased by generic
                   continue;
                } else if (fieldClass.equals(List.class)) {
                     Type genericType = field.getGenericType();
                     if (genericType instanceof ParameterizedType) {
                         Class<?> itemClass = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                         ListParser<?> listParser = new ListParser<>(itemClass);
                         value = listParser.parse(value);
                     } else {
                         throw new InvalidClassException("Cannot parse this list");
                     }
                } else {
                    value = ReflectiveJSONModel.parserOf(fieldClass).parse(value) ;
                }
                field.set(model, value);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            model = null;
        }
        return model;
    }

    @Override
    public JSONObject serialize(T data) {
        JSONObject jsonObject = new JSONObject();
        try {
            Field[] fields = data.getClass().getFields();
            for (Field field: fields) {
                String name = field.getName();
                Object value = field.get(data);
                if (value == null) {
                    value = JSONObject.NULL;
                } else if (value instanceof List) {
                    JSONArray jsonArray = new JSONArray();
                    List<Object> objectList = (List<Object>) value;
                    for (Object object: objectList) {
                        if (object instanceof ReflectiveJSONModel) {
                            jsonArray.put(((ReflectiveJSONModel) object).serialize());
                        } else {
                            jsonArray.put(object);
                        }
                    }
                    value = jsonArray;
                }
                jsonObject.put(name, value);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return jsonObject;
    }

    public static <T> JSONParser<T> parserOf(Class<T> targetClass) {
        JSONParser<T> result = null;
        if (ReflectiveJSONModel.class.isAssignableFrom(targetClass)) {
            try {
                result = (ReflectiveJSONModel<T>) targetClass.newInstance();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else if (targetClass.equals(Integer.class)) {
            return (JSONParser<T>) new IntegerParser();
        } else if (targetClass.equals(String.class)) {
            return (JSONParser<T>) new StringParser();
        } else if (targetClass.equals(Boolean.class)) {
            return (JSONParser<T>) new BooleanParser();
        } else if (targetClass.equals(Date.class)) {
            return (JSONParser<T>) new DateParser();
        } else if (targetClass.equals(Object.class)) {
            return new JSONParser<T>() {
                @Override
                public T parse(Object source) {
                    return null;
                }

                @Override
                public JSONObject serialize(T data) {
                    return null;
                }
            };
        }
        return  result;
    }

    public JSONObject serialize() {
        return  this.serialize((T) this);
    }

    @NonNull
    @Override
    public String toString() {
        return this.serialize().toString();
    }
}
