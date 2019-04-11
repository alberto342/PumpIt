package fitness.albert.com.pumpit.Model;

public class Training {

    private String imgName;
    private String exerciseName;
    private int setNumber;
    private int repNumber;
    private float weight;
    private int restTime;
    private boolean isFavorite;
    private String date;

    public Training() {
    }

    public Training(String exerciseName, int setNumber, int repNumber, int restTime, String imgName, String date, boolean isFavorite, float weight) {
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.repNumber = repNumber;
        this.restTime = restTime;
        this.imgName = imgName;
        this.isFavorite = isFavorite;
        this.date = date;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getRepNumber() {
        return repNumber;
    }

    public void setRepNumber(int repNumber) {
        this.repNumber = repNumber;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }
}
