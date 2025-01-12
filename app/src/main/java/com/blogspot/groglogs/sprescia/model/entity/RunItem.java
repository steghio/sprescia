package com.blogspot.groglogs.sprescia.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.sprescia.storage.db.converter.DateConverter;

import java.time.LocalDate;

import lombok.Data;

@Data
@Entity(tableName = "run")
@TypeConverters(DateConverter.class)
public class RunItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "km")
    private final float km;

    @ColumnInfo(name = "steps")
    private final int steps;

    @ColumnInfo(name = "hours")
    private final int hours;

    @ColumnInfo(name = "minutes")
    private final int minutes;

    @ColumnInfo(name = "date")
    private final LocalDate date;

    public float calculateKmH(){
        return km / (hours + ((float) minutes / 60));
    }
}
