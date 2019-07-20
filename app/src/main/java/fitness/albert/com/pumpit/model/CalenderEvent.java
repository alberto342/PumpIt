package fitness.albert.com.pumpit.model;

import io.realm.RealmObject;

public class CalenderEvent extends RealmObject {

    public static final String REALM_FILE_EVENT = "event.realm";

    private int id;
    private String date;
    private boolean isNutrition;
    private int goalExerciseOfDay;
    private int exerciseSuccess;
    private int goalDayKcal;
    private int actualKcal;

    public CalenderEvent() {
    }

    public CalenderEvent(String date, boolean isNutrition, int goalDayKcal, int actualKcal) {
        this.date = date;
        this.isNutrition = isNutrition;
        this.goalDayKcal = goalDayKcal;
        this.actualKcal = actualKcal;
    }


    public CalenderEvent(boolean isNutrition,int goalExerciseOfDay,  int exerciseSuccess, String date) {
        this.date = date;
        this.isNutrition = isNutrition;
        this.goalExerciseOfDay = goalExerciseOfDay;
        this.exerciseSuccess = exerciseSuccess;
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

    public boolean isNutrition() {
        return isNutrition;
    }

    public void setNutrition(boolean nutrition) {
        isNutrition = nutrition;
    }

    public int getGoalExerciseOfDay() {
        return goalExerciseOfDay;
    }

    public void setGoalExerciseOfDay(int goalExerciseOfDay) {
        this.goalExerciseOfDay = goalExerciseOfDay;
    }

    public int getExerciseSuccess() {
        return exerciseSuccess;
    }

    public void setExerciseSuccess(int exerciseSuccess) {
        this.exerciseSuccess = exerciseSuccess;
    }

    public int getGoalDayKcal() {
        return goalDayKcal;
    }

    public void setGoalDayKcal(int goalDayKcal) {
        this.goalDayKcal = goalDayKcal;
    }

    public int getActualKcal() {
        return actualKcal;
    }

    public void setActualKcal(int actualKcal) {
        this.actualKcal = actualKcal;
    }
}
