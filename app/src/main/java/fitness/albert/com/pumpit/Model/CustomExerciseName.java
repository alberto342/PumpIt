package fitness.albert.com.pumpit.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CustomExerciseName  extends RealmObject {

    public static final String REALM_FILE_EXERCISE = "exercise.realm";

    @PrimaryKey
    private int id;
    private String exercise_name;
    private String muscle_group;
    private String muscle_group_2;
    private String exercise_description;
    private String file_name;
    private String path;

    public CustomExerciseName() {
    }

    public CustomExerciseName(String exercise_name, String muscle_group, String muscle_group_2, String exercise_description, String img_name, String path) {
        this.exercise_name = exercise_name;
        this.muscle_group = muscle_group;
        this.muscle_group_2 = muscle_group_2;
        this.exercise_description = exercise_description;
        this.file_name = img_name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(String exercise_name) {
        this.exercise_name = exercise_name;
    }

    public String getMuscle_group() {
        return muscle_group;
    }

    public void setMuscle_group(String muscle_group) {
        this.muscle_group = muscle_group;
    }

    public String getMuscle_group_2() {
        return muscle_group_2;
    }

    public void setMuscle_group_2(String muscle_group_2) {
        this.muscle_group_2 = muscle_group_2;
    }

    public String getExercise_description() {
        return exercise_description;
    }

    public void setExercise_description(String exercise_description) {
        this.exercise_description = exercise_description;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
