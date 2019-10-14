package com.albert.fitness.pumpit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.FinishTraining;

import java.util.List;

import fitness.albert.com.pumpit.R;

public class FinishWorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FinishTraining> finishTrainingList;

    public FinishWorkoutAdapter(Context context, List<FinishTraining> finishTrainingList) {
        this.mContext = context;
        this.finishTrainingList = finishTrainingList;
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
//        Bitmap bitmap = BitmapFromAssent.getBitmapFromAsset(mContext, finishTrainingList.get(position).getImgName());
//
//        Glide.with(mContext)
//                .load(bitmap)
//                .into(holder.thm);
//
//        holder.exerciseName.setText(finishTrainingList.get(position).getExerciseName());

//        for(int i=0; i<finishTrainingList.get(position).getTrackerExercises().size(); i++) {
//            int repNumber =  finishTrainingList.get(position).getTrackerExercises().get(i).getRepNumber();
//            float weight = finishTrainingList.get(position).getTrackerExercises().get(i).getWeight();
//
//            holder.setNum.setText(String.format(Locale.US, "Set %d: %.2f x %d", i+1,weight,repNumber));
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
            }
        });

    }


    @Override
    public int getItemCount() {
        return finishTrainingList.size();
    }


//    public void removeItem(int position) {
//        finishTrainingList.remove(position);
//        notifyItemRemoved(position);
//    }

    @Override
    public long getItemId(int position) {
        //Auto-generated method stub
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thm;
        TextView exerciseName, setNum;


        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);
            this.thm = rowView.findViewById(R.id.iv_log_exercise_img);
            this.exerciseName = rowView.findViewById(R.id.tv_log_exercise_name);
            this.setNum = rowView.findViewById(R.id.tv_log_set);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
