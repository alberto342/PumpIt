package fitness.albert.com.pumpit.workout;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
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
        ImageView exerciseImg;
        String TAG = "ImgExerciseFragment";

        exerciseImg = view.findViewById(R.id.iv_exercise_img);

        try {
            String imgFile = "file:///android_asset/images/" + ExerciseAdapter.exerciseImg;

            Glide.with(this)
                    .asGif()
                    .load(imgFile)
                    .into(exerciseImg);

            Log.d(TAG, "Img successfully loaded " + ExerciseAdapter.exerciseImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
