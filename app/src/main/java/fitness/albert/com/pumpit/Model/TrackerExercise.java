package fitness.albert.com.pumpit.Model;

public class TrackerExercise {

    private int repsNumber;
    private float weight;

    public TrackerExercise() {
    }


    public TrackerExercise(int repNumber, float weight) {
        this.repsNumber = repNumber;
        this.weight = weight;
    }


    public TrackerExercise(float weight) {
        this.weight = weight;
    }

    public TrackerExercise(int repNumber) {
        this.repsNumber = repNumber;
    }


    public int getRepNumber() {
        return repsNumber;
    }

    public void setRepNumber(int repNumber) {
        this.repsNumber = repNumber;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
