package com.example.xueleme.business;

import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.ResponseModelAdapter;
import com.example.xueleme.models.responses.ServiceResult;
import com.example.xueleme.models.responses.ServiceResultEnum;
import com.example.xueleme.utils.HttpRequester;

public class RequestController {
    private <TExtraData, TResult> ActionResultHandler<ServiceResult<TExtraData>, String>
        getHandler(ServiceResultEnum expected,
                   ResponseModelAdapter<TExtraData, TResult> adapter,
                   ActionResultHandler<TResult, String> handler) {
        return new ActionResultHandler<ServiceResult<TExtraData>, String>() {
            @Override
            public void onSuccess(ServiceResult<TExtraData> tExtraDataServiceResult) {
                ServiceResultEnum serviceResultEnum = ServiceResultEnum.values()[tExtraDataServiceResult.state];
                if (serviceResultEnum.equals(expected)) {
                    handler.onSuccess(adapter.convert(tExtraDataServiceResult.extraData));
                } else {
                    handler.onError(tExtraDataServiceResult.detail);
                }
            }

            @Override
            public void onError(String s) {
                handler.onError(s);
            }
        };
    }

    private  ActionResultHandler<ServiceResult<Object>, String>
        getSimpleHandler(ServiceResultEnum expected,
                         ActionResultHandler<String, String> handler
                         ){
        return new ActionResultHandler<ServiceResult<Object>, String>() {
            @Override
            public void onSuccess(ServiceResult<Object> objectServiceResult) {
                ServiceResultEnum serviceResultEnum = ServiceResultEnum.values()[objectServiceResult.state];
                if (serviceResultEnum.equals(expected)) {
                    handler.onSuccess(objectServiceResult.detail);
                } else {
                    handler.onError(objectServiceResult.detail);
                }
            }

            @Override
            public void onError(String s) {
                handler.onError(s);
            }
        };
    }

    public <TData, TExtraData, TResult> void handleGetAction(
            UserAction<TData, TResult, String> action,
            String path,
            ServiceResultEnum expected,
            ServiceResult<TExtraData> parser,
            ResponseModelAdapter<TExtraData, TResult> adapter
    ) {
        HttpRequester.getInstance().get(path, parser, getHandler(expected, adapter, action.resultHandler));
    }

    public <TData extends ReflectiveJSONModel, TExtraData, TResult> void handlePostAction(
            UserAction<TData, TResult, String> action,
            String path,
            ServiceResultEnum expected,
            ServiceResult<TExtraData> parser,
            ResponseModelAdapter<TExtraData, TResult> adapter
    ) {
        HttpRequester.getInstance().post(path, action.data, parser, getHandler(expected, adapter, action.resultHandler));

    }

    public <TData extends ReflectiveJSONModel> void handlePostAction(
        UserAction<TData, String, String> action,
        String path,
        ServiceResultEnum expected
    ) {
        HttpRequester.getInstance().post(path, action.data, ServiceResult.noExtra(), getSimpleHandler(expected, action.resultHandler));
    }
}
