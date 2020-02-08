package com.albert.fitness.pumpit.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.albert.fitness.pumpit.helper.BitmapFromAssent;
import com.bumptech.glide.Glide;

import java.util.Objects;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "exercise_table", foreignKeys = @ForeignKey(entity = ExerciseCategory.class,
        parentColumns = "id", childColumns = "category_id", onDelete = CASCADE))


public class Exercise {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    private int exerciseId;

    @ColumnInfo(name = "exercise_name")
    private String exerciseName;

    @ColumnInfo(name = "img_Name")
    private String imgName;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "secondary_category_id")
    private int secondaryCategoryId;


    public Exercise() {
    }

    public Exercise(String exerciseName, String imgName) {
        this.exerciseName = exerciseName;
        this.imgName = imgName;
    }


    public Exercise(int id, String exerciseName, String imgName, int categoryId) {
        this.exerciseId = id;
        this.exerciseName = exerciseName;
        this.imgName = imgName;
        this.categoryId = categoryId;
    }

    public Exercise(String exerciseName, String imgName, int categoryId, int secondaryCategoryId) {
        this.exerciseName = exerciseName;
        this.imgName = imgName;
        this.categoryId = categoryId;
        this.secondaryCategoryId = secondaryCategoryId;
    }

    @BindingAdapter({"img_name"})
    public static void loadImage(ImageView view, String imageUrl) {
        Bitmap bitmap;
        String charAtZero = null;

        try{
            charAtZero = Character.toString(imageUrl.charAt(0));
        } catch (Exception e) {
            Log.i("exerciseObj", "loadImage error: " + e.getMessage());
        }

//        if(!imageUrl.isEmpty()) {
//            charAtZero = Character.toString(imageUrl.charAt(0));
//        }

        if(charAtZero != null && charAtZero.equals("/")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inTempStorage = new byte[16 * 1024];
             bitmap = BitmapFactory.decodeFile(imageUrl, options);
        } else {
            bitmap = BitmapFromAssent.getBitmapFromAsset(view.getContext(), imageUrl);
        }

        Glide.with(view.getContext())
                .load(bitmap)
              //  .placeholder(R.drawable.loading)
                .into(view);
    }


    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSecondaryCategoryId() {
        return secondaryCategoryId;
    }

    public void setSecondaryCategoryId(int secondaryCategoryId) {
        this.secondaryCategoryId = secondaryCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return getExerciseId() == exercise.getExerciseId() &&
                getCategoryId() == exercise.getCategoryId() &&
                getExerciseName().equals(exercise.getExerciseName()) &&
                getImgName().equals(exercise.getImgName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExerciseId(), getExerciseName(), getImgName(), getCategoryId());
    }
}
