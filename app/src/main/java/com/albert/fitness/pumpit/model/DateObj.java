package com.albert.fitness.pumpit.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "date_table")
public class DateObj {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;

    @Ignore
    public DateObj() {
    }

    public DateObj(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return this.date;
    }
}
