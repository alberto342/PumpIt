package com.albert.fitness.pumpit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.helper.BitmapFromAssent;
import com.albert.fitness.pumpit.model.Exercise;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import fitness.albert.com.pumpit.R;

public class FinishWorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Exercise> exerciseList;
    private Map<String, Object> mapFinishWorkouts;

    public FinishWorkoutAdapter(Context context, List<Exercise> exerciseList, Map<String, Object> mapFinishWorkouts) {
        this.mContext = context;
        this.exerciseList = exerciseList;
        this.mapFinishWorkouts = mapFinishWorkouts;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_finish_workout, parent, false);
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
        Bitmap bitmap = BitmapFromAssent.getBitmapFromAsset(mContext, exerciseList.get(position).getImgName());

        Glide.with(mContext)
                .load(bitmap)
                .into(holder.thm);

        holder.exerciseName.setText(exerciseList.get(position).getExerciseName());


        if (mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 0) == null) {
            holder.setNum.setVisibility(View.INVISIBLE);
        } else {
            holder.setNum.setText(String.format(Locale.US, "Set %d:\t  %.2f x %d", 1, mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 0), mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + " rept " + 0)));
        }

        if (mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 1) == null) {
            holder.setNum2.setVisibility(View.INVISIBLE);
        } else {
            holder.setNum2.setText(String.format(Locale.US, "Set %d:\t  %.2f x %d", 2, mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 1), mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + " rept " + 1)));
        }

        if (mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 2) == null) {
            holder.setNum3.setVisibility(View.INVISIBLE);
        } else {
            holder.setNum3.setText(String.format(Locale.US, "Set %d:\t  %.2f x %d", 3, mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 2), mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + " rept " + 2)));
        }

        if(mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 3) == null) {
            holder.setNum4.setVisibility(View.INVISIBLE);
        } else {
            holder.setNum4.setText(String.format(Locale.US, "Set %d:\t  %.2f x %d", 4, mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + 3), mapFinishWorkouts.get(exerciseList.get(position).getExerciseName() + " rept " + 3)));
        }

        holder.itemView.setOnClickListener(v -> {


//                Intent i = new Intent(mContext, ShowLogExerciseActivity.class);
//                i.putExtra("exerciseName", finishTrainingList.get(position).getExerciseName());
//                i.putExtra("trackerExercisesSize", finishTrainingList.get(position).getTrackerExercises().size());
//
//                for(int p=0; p<finishTrainingList.get(position).getTrackerExercises().size(); p++) {
//                    int repNumber =  finishTrainingList.get(position).getTrackerExercises().get(p).getRepNumber();
//                    float weight = finishTrainingList.get(position).getTrackerExercises().get(p).getWeight();
//
//                    i.putExtra("repNumber" + p, repNumber);
//                    i.putExtra("weight" + p, weight);
//                }
//                mContext.startActivity(i);
        });

    }


    @Override
    public int getItemCount() {
        return exerciseList.size();
    }


//    public void removeItem(int position) {
//        finishTrainingList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thm;
        TextView exerciseName, setNum, setNum2, setNum3, setNum4;


        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);
            this.thm = rowView.findViewById(R.id.iv_log_exercise_img);
            this.exerciseName = rowView.findViewById(R.id.tv_log_exercise_name);
            this.setNum = rowView.findViewById(R.id.tv_log_set);
            this.setNum2 = rowView.findViewById(R.id.tv_log_set_2);
            this.setNum3 = rowView.findViewById(R.id.tv_log_set_3);
            this.setNum4 = rowView.findViewById(R.id.tv_log_set_4);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
