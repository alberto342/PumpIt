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
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.viewmodel.WelcomeActivityViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import fitness.albert.com.pumpit.R;

public class ImgExerciseFragment extends Fragment {
    private ImageView ivExerciseImg, ivMusicalType, ivMusicalType2;
    private WelcomeActivityViewModel viewModel;

    public ImgExerciseFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_img_exercise2, container, false);
        ivExerciseImg = view.findViewById(R.id.iv_exercise_img);
        ivMusicalType = view.findViewById(R.id.iv_musical_type);
        ivMusicalType2 = view.findViewById(R.id.iv_musical_type_2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(WelcomeActivityViewModel.class);
        final String TAG = "ImgExerciseFragment";
        String imgFile;
        ExerciseDetailActivity resultActivity = (ExerciseDetailActivity) getActivity();
        String getExerciseName = resultActivity.getExerciseName();
        int exerciseId = resultActivity.getExerciseId();
        String charAtZero = Character.toString(getExerciseName.charAt(0));
        checkTheExerciseType(exerciseId);

        try {
            if (charAtZero.equals("/")) {
                Glide.with(this)
                        .load(getExerciseName)
                        .into(ivExerciseImg);
            } else {
                imgFile = "file:///android_asset/images/" + getExerciseName;
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
            }
            Log.d(TAG, "Img successfully loaded " + getExerciseName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkTheExerciseType(int id) {
        viewModel.getExerciseById(id).observe(this, exercise -> {
            if (exercise != null) {
                int categoryId = exercise.getCategoryId();
                int secondaryCategory = exercise.getSecondaryCategoryId();

                setImg(categoryId, secondaryCategory);


            }
        });
    }

    private void setImg(int categoryId, int secondaryCategory) {
        switch (categoryId) {
            case 1:
                ivMusicalType.setImageResource(R.mipmap.rectus_abdominis);
                ivMusicalType2.setVisibility(View.GONE);
                break;
            case 2:
                break;
            case 3:
                ivMusicalType.setImageResource(R.mipmap.deltoid);
                ivMusicalType2.setVisibility(View.GONE);
                break;
            case 4:
                break;
            case 5:
                if (secondaryCategory == 7) {
                    ivMusicalType.setImageResource(R.mipmap.gluteus_maximus);
                    ivMusicalType2.setVisibility(View.VISIBLE);
                    ivMusicalType2.setImageResource(R.mipmap.quadriceps_femoris);
                } else if (secondaryCategory == 5) {
                    ivMusicalType.setImageResource(R.mipmap.calf);
                    ivMusicalType2.setVisibility(View.GONE);
                } else {
                    ivMusicalType.setImageResource(R.mipmap.quadriceps_femoris);
                    ivMusicalType2.setVisibility(View.GONE);
                }
                break;
            case 6:
                if (secondaryCategory == 2) {
                    ivMusicalType.setImageResource(R.mipmap.biceps);
                    ivMusicalType2.setVisibility(View.GONE);
                } else if (secondaryCategory == 4) {
                    ivMusicalType.setImageResource(R.mipmap.biceps);
                    ivMusicalType2.setVisibility(View.VISIBLE);
                    ivMusicalType2.setImageResource(R.mipmap.brachioradialis);
                } else if (secondaryCategory == 3) {
                    ivMusicalType.setImageResource(R.mipmap.triceps);
                    ivMusicalType2.setVisibility(View.VISIBLE);
                } else if (secondaryCategory == 6) {
                    ivMusicalType.setImageResource(R.mipmap.biceps);
                    ivMusicalType2.setVisibility(View.VISIBLE);
                    ivMusicalType2.setImageResource(R.mipmap.triceps);
                }
                break;
            case 7:
                ivMusicalType.setImageResource(R.mipmap.pectoralis_major);
                ivMusicalType2.setVisibility(View.GONE);
                break;
            case 8:
                ivMusicalType.setImageResource(R.mipmap.latissimus_dorsi);
                ivMusicalType2.setVisibility(View.VISIBLE);
                ivMusicalType2.setImageResource(R.mipmap.trapezius);
        }
    }
}
