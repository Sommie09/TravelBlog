package com.travelblog.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.travelblog.http.Blog;

import java.util.List;

@Dao
public interface BlogDAO {

    @Query("SELECT * FROM blog")//Select all data from the blog table
    List<Blog> getAll(); //Get all blog articles

    @Insert
    void insertAll(List<Blog> blogList); //Save all blog articles

    @Query("DELETE FROM blog")
    void deleteAll(); //Delete blog articles
}
