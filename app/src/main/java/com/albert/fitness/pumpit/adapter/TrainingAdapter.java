package com.albert.fitness.pumpit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.helper.BitmapFromAssent;
import com.albert.fitness.pumpit.helper.ItemTouchHelperAdapter;
import com.albert.fitness.pumpit.helper.ItemTouchHelperViewHolder;
import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.workout.ShowExerciseImgActivity;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.R;

public class TrainingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private Context mContext;
    private List<Training> trainingList;
    private List<Exercise> exerciseList;

    public TrainingAdapter(Context mContext, List<Training> trainingList, List<Exercise> exerciseList) {
        this.mContext = mContext;
        this.trainingList = trainingList;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }


    private void bindViews(final ViewHolder holder, final int position) {

        final String TAG = "TrainingAdapter";

        Bitmap bitmap = BitmapFromAssent.getBitmapFromAsset(mContext, exerciseList.get(position).getImgName());

        Glide.with(mContext)
                .load(bitmap)
                .into(holder.ivTraining);

        // int rep = trackerList.get(position).getRepNumber();
        int set = trainingList.get(position).getSizeOfRept();
        holder.trainingName.setText( exerciseList.get(position).getExerciseName());

       // holder.trainingName.setText(trainingList.get(position).getExerciseName());
        holder.tvSetAndRep.setText(String.format(Locale.US, "%d SET(s)", set));
        holder.tvRestTime.setText(String.format(Locale.US, "Rest: %ds", trainingList.get(position).getRestBetweenSet()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + exerciseList.get(position).getExerciseName());
                Intent intent = new Intent(mContext, ShowExerciseImgActivity.class);
                intent.putExtra("exerciseName", exerciseList.get(position).getExerciseName());
                intent.putExtra("imgName", exerciseList.get(position).getImgName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(trainingList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        trainingList.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {

        private ImageView ivTraining;
        private TextView trainingName, tvSetAndRep, tvRestTime;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);
            this.ivTraining = rowView.findViewById(R.id.iv_training_thm);
            this.trainingName = rowView.findViewById(R.id.tv_training_name);
            this.tvSetAndRep = rowView.findViewById(R.id.tv_set_and_rep);
            this.tvRestTime = rowView.findViewById(R.id.tv_rest_time);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


}
