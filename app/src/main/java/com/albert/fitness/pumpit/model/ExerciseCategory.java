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

    @ColumnInfo(name = "secondary_category")
    private String secondaryCategory;


    public ExerciseCategory() {
    }

    public ExerciseCategory(int id, String categoryName, String secondaryCategory) {
        this.id = id;
        this.categoryName = categoryName;
        this.secondaryCategory = secondaryCategory;
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

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    @Override
    public String toString() {
        return this.categoryName;
    }
}
