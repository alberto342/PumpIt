package com.albert.fitness.pumpit.model;

public class FinishTraining extends Training{

    public static final String TRAINING_LOG = "trainingLog";
    public static final String TRAINING_NAME = "trainingName";

    private String chrTotalTraining;
    private Boolean exerciseComplete;
    private int caloriesBurned;

    public FinishTraining() {
    }

    public FinishTraining(String chrTotalTraining, Boolean exerciseComplete) {
        this.chrTotalTraining = chrTotalTraining;
        this.exerciseComplete = exerciseComplete;
    }

//    public FinishTraining(String exerciseName, List<TrackerExercise> trackerExercises, int sizeOfRept,
//                          int restBetweenSet, int restAfterExercise, String imgName, String date,
//                          boolean isFavorite, String chrTotalTraining, int caloriesBurned) {
//        super(exerciseName, trackerExercises, sizeOfRept, restBetweenSet, restAfterExercise, imgName, date, isFavorite);
//        this.chrTotalTraining = chrTotalTraining;
//        this.caloriesBurned = caloriesBurned;
//    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public static String getTrainingLog() {
        return TRAINING_LOG;
    }

    public static String getTrainingName() {
        return TRAINING_NAME;
    }

    public String getChrTotalTraining() {
        return chrTotalTraining;
    }

    public void setChrTotalTraining(String chrTotalTraining) {
        this.chrTotalTraining = chrTotalTraining;
    }

    public Boolean getExerciseComplete() {
        return exerciseComplete;
    }

    public void setExerciseComplete(Boolean exerciseComplete) {
        this.exerciseComplete = exerciseComplete;
    }

}
