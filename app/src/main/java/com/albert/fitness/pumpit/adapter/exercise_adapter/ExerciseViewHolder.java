package com.albert.fitness.pumpit.adapter.exercise_adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import fitness.albert.com.pumpit.R;


public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView image;

    ExerciseViewHolder(View itemView) {
        super(itemView);
        nameTxt =  itemView.findViewById(R.id.tv_exercise_name);
        image  = itemView.findViewById(R.id.exercise_img);
    }
}
