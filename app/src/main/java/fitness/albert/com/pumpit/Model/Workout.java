package fitness.albert.com.pumpit.Model;

public class Workout extends WorkoutPlans {

    private String workoutDayName;
    private int workoutDay;
    private int numOfExercise;
    private int lengthTraining;

    public Workout() {
    }

    public Workout(String workoutDayName, int workoutDay, int numOfExercise, int lengthTraining) {
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.numOfExercise = numOfExercise;
        this.lengthTraining = lengthTraining;
    }

    public Workout(String routineName, String daysWeek, String difficultyLevel, String dayType, String routineDescription, String date, int daysWeekPosition, String workoutDayName, int workoutDay, int numOfExercise, int lengthTraining) {
        super(routineName, daysWeek, difficultyLevel, dayType, routineDescription, date, daysWeekPosition);
        this.workoutDayName = workoutDayName;
        this.workoutDay = workoutDay;
        this.numOfExercise = numOfExercise;
        this.lengthTraining = lengthTraining;
    }

    public String getWorkoutDayName() {
        return workoutDayName;
    }

    public void setWorkoutDayName(String workoutDayName) {
        this.workoutDayName = workoutDayName;
    }

    public int getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(int workoutDay) {
        this.workoutDay = workoutDay;
    }

    public int getNumOfExercise() {
        return numOfExercise;
    }

    public void setNumOfExercise(int numOfExercise) {
        this.numOfExercise = numOfExercise;
    }

    public int getLengthTraining() {
        return lengthTraining;
    }

    public void setLengthTraining(int lengthTraining) {
        this.lengthTraining = lengthTraining;
    }
}
