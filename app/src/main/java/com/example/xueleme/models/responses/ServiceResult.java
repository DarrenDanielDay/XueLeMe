package com.example.xueleme.models.responses;

import com.example.xueleme.models.JSONParser;
import com.example.xueleme.models.ListParser;
import com.example.xueleme.models.ReflectiveJSONModel;

import java.util.List;
import java.util.Map;

public class ServiceResult<TExtraData> extends ReflectiveJSONModel<ServiceResult<TExtraData>> {
    public String detail;
    public Integer state;
    public TExtraData extraData;
    private Class<TExtraData> extraDataClass;
    private Class<?> listItemClass;
    public ServiceResult(Class<TExtraData> extraDataClass) {
        this.extraDataClass = extraDataClass;
    }

    public ServiceResult(Class<TExtraData> extraDataCLass, Class<?> listItemClass) {
        this.extraDataClass = extraDataCLass;
        this.listItemClass = listItemClass;
    }

    @Override
    public ServiceResult<TExtraData> parse(Object source) {
        ServiceResult<TExtraData> serviceResult = super.parse(source);
        Map<String, Object> sourceMap = (Map<String, Object>) source;
        JSONParser<TExtraData> parser = null;
        Object value = sourceMap.get("extraData");
        if (extraDataClass.equals(List.class)) {
            parser = (JSONParser<TExtraData>) new ListParser<Object>((Class<Object>) this.listItemClass);
        } else {
            parser = ReflectiveJSONModel.parserOf(this.extraDataClass);
        }
        this.extraData = parser.parse(value);
        return  serviceResult;
    }
}
