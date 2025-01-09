package com.blogspot.groglogs.sprescia.storage.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.storage.db.converter.DateConverter;
import com.blogspot.groglogs.sprescia.storage.db.dao.RunDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;

@Database(entities = {RunItem.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    @Getter
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract RunDao fuelDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "spresciadb"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}