package fitness.albert.com.pumpit.Model;

public class WorkoutPlans {

    public static final String WORKOUT_PLANS = "workoutPlans";
    public static final String WORKOUT_NAME = "workoutName";
    private String routineName;
    private String daysWeek;
    private String difficultyLevel;
    private String routineType;
    private String dayType;
    private String routineDescription;
    private String date;
    private int daysWeekPosition;

    public WorkoutPlans() {
    }

    public WorkoutPlans(String routineName, String daysWeek, String difficultyLevel, String routineType, String dayType, String routineDescription, String date, int daysWeekPosition) {
        this.routineName = routineName;
        this.daysWeek = daysWeek;
        this.difficultyLevel = difficultyLevel;
        this.dayType = dayType;
        this.routineDescription = routineDescription;
        this.date = date;
        this.daysWeekPosition = daysWeekPosition;
        this.routineType = routineType;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public int getDaysWeekPosition() {
        return daysWeekPosition;
    }

    public void setDaysWeekPosition(int daysWeekPosition) {
        this.daysWeekPosition = daysWeekPosition;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getDaysWeek() {
        return daysWeek;
    }

    public void setDaysWeek(String daysWeek) {
        this.daysWeek = daysWeek;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public String getRoutineDescription() {
        return routineDescription;
    }

    public void setRoutineDescription(String routineDescription) {
        this.routineDescription = routineDescription;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
