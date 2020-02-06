package com.albert.fitness.pumpit.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_category_table")
public class ExerciseCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    public ExerciseCategory() {
    }

    public ExerciseCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public String toString() {
        return this.categoryName;
    }
}
