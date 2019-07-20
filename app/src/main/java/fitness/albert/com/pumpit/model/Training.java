package fitness.albert.com.pumpit.model;

import java.util.ArrayList;
import java.util.List;

public class Training {

    private String imgName;
    private String exerciseName;
    private int restBetweenSet;
    private int restAfterExercise;
    private boolean isFavorite;
    private String date;
    private int sizeOfRept;
    private List<TrackerExercise> trackerExercises;


    public Training() {
    }

    public Training(String exerciseName, List<TrackerExercise> trackerExercises, int sizeOfRept, int restBetweenSet, int restAfterExercise, String imgName, String date, boolean isFavorite) {
        this.exerciseName = exerciseName;
        this.restBetweenSet = restBetweenSet;
        this.restAfterExercise = restAfterExercise;
        this.imgName = imgName;
        this.isFavorite = isFavorite;
        this.date = date;
        this.sizeOfRept = sizeOfRept;
        this.trackerExercises = new ArrayList<>();
        this.trackerExercises = trackerExercises;
    }


    public int getSizeOfRept() {
        return sizeOfRept;
    }

    public void setSizeOfRept(int sizeOfRept) {
        this.sizeOfRept = sizeOfRept;
    }

    public List<TrackerExercise> getTrackerExercises() {
        return trackerExercises;
    }

    public void setTrackerExercises(List<TrackerExercise> trackerExercises) {
        this.trackerExercises = trackerExercises;
    }

    public int getRestAfterExercise() {
        return restAfterExercise;
    }

    public void setRestAfterExercise(int restAfterExercise) {
        this.restAfterExercise = restAfterExercise;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
