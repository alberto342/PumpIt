package com.albert.fitness.pumpit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.TrackerExercise;

import java.util.List;

import fitness.albert.com.pumpit.R;

public class NewTrackerExerciseAdapter extends RecyclerView.Adapter<NewTrackerExerciseAdapter.ViewHolder> {

    private static MyClickListener myClickListener;
    private List<TrackerExercise> trackerExerciseList;
    private boolean isWeightGone;

    public NewTrackerExerciseAdapter(List<TrackerExercise> trackerExerciseList, boolean isWeightGone) {
        this.trackerExerciseList = trackerExerciseList;
        this.isWeightGone = isWeightGone;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        NewTrackerExerciseAdapter.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_workout_tracker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(isWeightGone) {
            holder.edWight.setVisibility(View.GONE);
        }

        holder.tvCount.setText(String.valueOf(position + 1));
        holder.edWight.setText(String.valueOf(trackerExerciseList.get(position).getWeight()));
        holder.edReps.setText(String.valueOf(trackerExerciseList.get(position).getRepsNumber()));
        holder.btnRemove.setTag(R.drawable.ic_delete_black);
    }

    @Override
    public int getItemCount() {
        return trackerExerciseList.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    public void deleteItem(int index) {
        if (trackerExerciseList.size() > index) {
            trackerExerciseList.remove(index);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvCount, edWight, edReps;
        ImageView btnRemove;


        public ViewHolder(View itemView) {
            super(itemView);
            edWight = itemView.findViewById(R.id.tv_weight);
            edReps = itemView.findViewById(R.id.tv_reps);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnRemove = itemView.findViewById(R.id.iv_remove_tracker);
            btnRemove.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
}
