package com.albert.fitness.pumpit.workout;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import fitness.albert.com.pumpit.R;

public class ShowExerciseImgActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_img);
        toolbar = getSupportActionBar();
        assert toolbar != null;
        receiveImgAndName();
    }


    private void receiveImgAndName() {
        ImageView exerciseImg = findViewById(R.id.iv_exercise_img);
        Bundle extras = getIntent().getExtras();
        String imgName = null;
        String exerciseName = null;

        if (extras != null) {
            imgName = extras.getString("imgName");
            exerciseName = extras.getString("exerciseName");
        }

        toolbar.setTitle(exerciseName);

        try {
            String imgFile = "file:///android_asset/images/" + imgName;


            Glide.with(this)
                    .asGif()
                    .load(imgFile)

                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model,
                                                       Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(exerciseImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}