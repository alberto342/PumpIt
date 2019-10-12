package com.albert.fitness.pumpit.adapter.exercise_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.helper.BitmapFromAssent;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.workout.ExerciseDetailActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;


public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> implements SearchView.OnQueryTextListener {

    private Context mContext;
    private List<Exercise> exerciseList;
    public static String exerciseImg;
    public static String exerciseName;


    public ExerciseAdapter(Context c, List<Exercise> exerciseList) {
        this.mContext = c;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_exercise, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseViewHolder holder, int position) {

        final String TAG = "ExerciseAdapter";

        Bitmap bitmap = BitmapFromAssent.getBitmapFromAsset(mContext, exerciseList.get(position).getImgName());

        Glide.with(mContext)
                .load(bitmap)
                .into(holder.image);

        holder.nameTxt.setText(exerciseList.get(position).getExerciseName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                exerciseImg = exerciseList.get(position).getImgName();
                exerciseName = exerciseList.get(position).getExerciseName();
                mContext.startActivity(new Intent(mContext, ExerciseDetailActivity.class));
                Log.d(TAG, "Exercise position: " + exerciseList.get(position).getExerciseName());
            }
        });
    }


    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void updateList(List<Exercise> newList) {
        exerciseList = new ArrayList<>();
        exerciseList.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
