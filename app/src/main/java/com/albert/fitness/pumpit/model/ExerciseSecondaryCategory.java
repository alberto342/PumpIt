package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_secondary_category_table")
public class ExerciseSecondaryCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "secondary_category")
    private String secondaryCategory;

    public ExerciseSecondaryCategory() {
    }

    public ExerciseSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    @Override
    public String toString() {
        return this.secondaryCategory;
    }
}
