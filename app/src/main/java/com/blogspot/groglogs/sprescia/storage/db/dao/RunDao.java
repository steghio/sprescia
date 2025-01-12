package com.blogspot.groglogs.sprescia.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;

import java.util.List;

//transaction annotation so that operations are immediately flushed and ui refresh picks always latest data
@Dao
public interface RunDao {

    @Query("SELECT * FROM run where id = :id")
    RunItem findById(Long id);

    //make it return Long if we want generated ID of new entity in output
    @Transaction
    @Insert
    void insert(RunItem runItem);

    @Transaction
    @Query("DELETE FROM run WHERE id = :id")
    void delete(long id);

    @Transaction
    @Query("DELETE FROM run")
    void deleteAll();

    @Transaction
    @Update
    void update(RunItem runItem);

    //multiple items in same day are sorted by insertion time..
    @Transaction
    @Query("SELECT * FROM run ORDER BY date DESC")
    List<RunItem> getAllItemsByDateDesc();

    @Transaction
    @Query("SELECT * FROM run ORDER BY date ASC")
    List<RunItem> getAllItemsByDateAsc();
}
