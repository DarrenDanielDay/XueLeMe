package com.example.xueleme.business;

import java.io.File;

public interface IFileController {
    void postFile(UserAction<File, String, String> action);
    void getFile(UserAction<String, File, String> action);
}
