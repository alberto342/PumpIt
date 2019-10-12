package com.albert.fitness.pumpit.adapter.exercise_adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fitness.albert.com.pumpit.R;


public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView image;
    LinearLayout layout;

    ExerciseViewHolder(View itemView) {
        super(itemView);

        nameTxt =  itemView.findViewById(R.id.tv_exercise_name);
        image  = itemView.findViewById(R.id.exercise_img);
        layout = itemView.findViewById(R.id.item_exe_selected);
    }
}
