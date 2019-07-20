package fitness.albert.com.pumpit.model;

import java.util.List;

public class FinishTraining extends Training{

    public static final String TRAINING_LOG = "trainingLog";
    public static final String TRAINING_NAME = "trainingName";

    private String chrTotalTraining;
    private Boolean exerciseComplete;
    private String chrWasted;
    private String chrActual;
    private String chrRestTime;
    private float totalWeight;
    private int newRecord;

    public FinishTraining() {
    }

    public FinishTraining(String chrTotalTraining, Boolean exerciseComplete, String chrWasted, String chrActual, String chrRestTime, float totalWeight, int newRecord) {
        this.chrTotalTraining = chrTotalTraining;
        this.exerciseComplete = exerciseComplete;
        this.chrWasted = chrWasted;
        this.chrActual = chrActual;
        this.chrRestTime = chrRestTime;
        this.totalWeight = totalWeight;
        this.newRecord = newRecord;
    }

    public FinishTraining(String exerciseName, List<TrackerExercise> trackerExercises, int sizeOfRept, int restBetweenSet, int restAfterExercise, String imgName, String date, boolean isFavorite, String chrTotalTraining, Boolean exerciseComplete, String chrWasted, String chrActual, String chrRestTime, float totalWeight, int newRecord) {
        super(exerciseName, trackerExercises, sizeOfRept, restBetweenSet, restAfterExercise, imgName, date, isFavorite);
        this.chrTotalTraining = chrTotalTraining;
        this.exerciseComplete = exerciseComplete;
        this.chrWasted = chrWasted;
        this.chrActual = chrActual;
        this.chrRestTime = chrRestTime;
        this.totalWeight = totalWeight;
        this.newRecord = newRecord;
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

    public String getChrWasted() {
        return chrWasted;
    }

    public void setChrWasted(String chrWasted) {
        this.chrWasted = chrWasted;
    }

    public String getChrActual() {
        return chrActual;
    }

    public void setChrActual(String chrActual) {
        this.chrActual = chrActual;
    }

    public String getChrRestTime() {
        return chrRestTime;
    }

    public void setChrRestTime(String chrRestTime) {
        this.chrRestTime = chrRestTime;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getNewRecord() {
        return newRecord;
    }

    public void setNewRecord(int newRecord) {
        this.newRecord = newRecord;
    }
}
