package com.travelblog.repository;

import com.travelblog.http.Blog;

import java.util.List;

public interface DataFromDatabaseCallback {
    void onSuccess(List<Blog> blogList);

    void onError();
}
