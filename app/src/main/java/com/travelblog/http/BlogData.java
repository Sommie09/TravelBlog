package com.travelblog.http;

import java.util.ArrayList;
import java.util.List;

public class BlogData {
    private List<Blog> data;

    public List<Blog> getData() {
        return data != null ? data : new ArrayList<>();
    }


}
