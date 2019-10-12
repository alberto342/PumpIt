package com.albert.fitness.pumpit.workout;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.albert.fitness.pumpit.adapter.exercise_adapter.ExerciseAdapter;
import fitness.albert.com.pumpit.R;

public class ImgExerciseFragment extends Fragment {

    public ImgExerciseFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_img_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String TAG = "ImgExerciseFragment";
        ImageView exerciseImg;
        final ProgressBar progress = view.findViewById(R.id.progress_befor_img);


        exerciseImg = view.findViewById(R.id.iv_exercise_img);

        try {
            String imgFile = "file:///android_asset/images/" + ExerciseAdapter.exerciseImg;


            Glide.with(this)
                    .asGif()
                    .load(imgFile)

                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress.setVisibility(View.GONE);
                            return false;
                        }
                    })

                    .into(exerciseImg);

            Log.d(TAG, "Img successfully loaded " + ExerciseAdapter.exerciseImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
