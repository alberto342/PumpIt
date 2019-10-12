package com.albert.fitness.pumpit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "training_table", foreignKeys = @ForeignKey(entity = WorkoutObj.class,
        parentColumns = "workout_id", childColumns = "workout_id", onDelete = CASCADE))
public class Training {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "training_id")
    private int trainingId;
    @ColumnInfo(name = "img_name")
    private String imgName;
    @ColumnInfo(name = "exercise_name")
    private String exerciseName;
    @ColumnInfo(name = "rest_between_set")
    private int restBetweenSet;
    @ColumnInfo(name = "rest_after_exercise")
    private int restAfterExercise;
    private String date;
    @ColumnInfo(name = "size_of_rept")
    private int sizeOfRept;
//    @ColumnInfo(name = "tracker_exercises")
//    private List<TrackerExercise> trackerExercises;
    @ColumnInfo(name = "workout_id")
    private int workoutId;


    public Training() {
    }

    public Training(String imgName, String exerciseName, int restBetweenSet, int restAfterExercise, String date, int sizeOfRept, int workoutId) {
        this.imgName = imgName;
        this.exerciseName = exerciseName;
        this.restBetweenSet = restBetweenSet;
        this.restAfterExercise = restAfterExercise;
        this.date = date;
        this.sizeOfRept = sizeOfRept;
      //  this.trackerExercises = trackerExercises;
        this.workoutId = workoutId;
    }

    public Training(String exerciseName, int sizeOfRept, int restBetweenSet, int restAfterExercise, String imgName, String date) {
        this.exerciseName = exerciseName;
        this.restBetweenSet = restBetweenSet;
        this.restAfterExercise = restAfterExercise;
        this.imgName = imgName;
        this.date = date;
        this.sizeOfRept = sizeOfRept;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getSizeOfRept() {
        return sizeOfRept;
    }

    public void setSizeOfRept(int sizeOfRept) {
        this.sizeOfRept = sizeOfRept;
    }

    public int getRestAfterExercise() {
        return restAfterExercise;
    }

    public void setRestAfterExercise(int restAfterExercise) {
        this.restAfterExercise = restAfterExercise;
    }

    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }


    public int getRestBetweenSet() {
        return restBetweenSet;
    }

    public void setRestBetweenSet(int restBetweenSet) {
        this.restBetweenSet = restBetweenSet;
    }
}
