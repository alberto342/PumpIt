package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "exercise_table", foreignKeys = @ForeignKey(entity = ExerciseCategory.class,
        parentColumns = "id", childColumns = "category_id", onDelete = CASCADE))


public class Exercise {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    private int exerciseId;

    @ColumnInfo(name = "exercise_name")
    private String exerciseName;

    @ColumnInfo(name = "img_Name")
    private String imgName;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "secondary_category_id")
    private int secondaryCategoryId;


    public Exercise() {
    }

    public Exercise(int id, String exerciseName, String img_name, int categoryId) {
        this.exerciseId = id;
        this.exerciseName = exerciseName;
        this.imgName = img_name;
        this.categoryId = categoryId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSecondaryCategoryId() {
        return secondaryCategoryId;
    }

    public void setSecondaryCategoryId(int secondaryCategoryId) {
        this.secondaryCategoryId = secondaryCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return getExerciseId() == exercise.getExerciseId() &&
                getCategoryId() == exercise.getCategoryId() &&
                getExerciseName().equals(exercise.getExerciseName()) &&
                getImgName().equals(exercise.getImgName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExerciseId(), getExerciseName(), getImgName(), getCategoryId());
    }
}
