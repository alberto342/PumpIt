package com.albert.fitness.pumpit.workout;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import fitness.albert.com.pumpit.R;

public class ImgExerciseFragment extends Fragment {
    private ImageView ivExerciseImg;

    public ImgExerciseFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_img_exercise, container, false);
        ivExerciseImg = view.findViewById(R.id.iv_exercise_img);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String TAG = "ImgExerciseFragment";
        ExerciseDetailActivity resultActivity = (ExerciseDetailActivity)getActivity();
        String getExerciseName = resultActivity.getExerciseName();

        try {
            String imgFile = "file:///android_asset/images/" + getExerciseName;

            Glide.with(this)
                    .asGif()
                    .load(imgFile)

                    .listener(new RequestListener<GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })

                    .into(ivExerciseImg);

            Log.d(TAG, "Img successfully loaded " + getExerciseName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
