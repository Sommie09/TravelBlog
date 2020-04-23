package com.travelblog.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseProvider {

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseProvider.class) {
                if (instance == null) {
                    instance = Room
                            .databaseBuilder(context, AppDatabase.class, "blog-database")
                            .build();
                }
            }
        }
        return instance;
    }
}
