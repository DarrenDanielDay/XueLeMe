package com.example.xueleme.utils;

public class FormatHelper {
    public static String exceptionFormat(Throwable throwable) {
        return throwable == null ? "未知原因" : throwable.getMessage();
    }
}
