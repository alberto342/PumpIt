package com.albert.fitness.pumpit.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.TrackerExercise;

import java.util.List;

import fitness.albert.com.pumpit.R;

public class LogTrackerExerciseAdapter extends RecyclerView.Adapter<LogTrackerExerciseAdapter.ViewHolder> {
    private List<TrackerExercise> trackerExerciseList;

    public LogTrackerExerciseAdapter(List<TrackerExercise> trackerExerciseList) {
        this.trackerExerciseList = trackerExerciseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_log_sets_and_rep, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.listenerWight.updatePosition(position);
        holder.listenerReps.updatePosition(position);

        holder.tvCount.setText("Set " + (position + 1));
        holder.edWight.setText(String.valueOf(trackerExerciseList.get(position).getWeight()));
        holder.edReps.setText(String.valueOf(trackerExerciseList.get(position).getRepsNumber()));

        if(trackerExerciseList.get(position).getWeight() == 0.0) {
            holder.edWight.setText("");
        }

        if(trackerExerciseList.get(position).getRepsNumber() == 0) {
            holder.edReps.setText("");
        }

    }


    @Override
    public int getItemCount() {
        return trackerExerciseList.size();
    }

    private class MyCustomEditTextListener implements TextWatcher {

        private int position;
        private EditText editText;

        public MyCustomEditTextListener(EditText ed) {
            this.editText = ed;
        }

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (charSequence.length() == 0) {
                return;
            }

            if (editText.getId() == R.id.et_log_reps) {
                if (String.valueOf(trackerExerciseList.get(position).getRepsNumber()).equals(charSequence.toString()))
                    return;
                else {
                    trackerExerciseList.get(position).setRepsNumber(Integer.parseInt(charSequence.toString()));

                }
            } else if (editText.getId() == R.id.et_log_weight) {
                if (String.valueOf(trackerExerciseList.get(position).getWeight()).equals(charSequence.toString())) {
                    return;
                } else {
                    trackerExerciseList.get(position).setWeight(Float.parseFloat(charSequence.toString()));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public void deleteItem(int index) {
        if (trackerExerciseList.size() > index) {
            trackerExerciseList.remove(index);
            notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCount;
        EditText edWight, edReps;
        MyCustomEditTextListener listenerReps, listenerWight;

        public ViewHolder(View itemView) {
            super(itemView);
            edWight = itemView.findViewById(R.id.et_log_weight);
            edReps = itemView.findViewById(R.id.et_log_reps);
            tvCount = itemView.findViewById(R.id.tv_log_sets_num);
            this.listenerReps = new MyCustomEditTextListener(this.edReps);
            this.listenerWight = new MyCustomEditTextListener(this.edWight);
            this.edReps.addTextChangedListener(listenerReps);
            this.edWight.addTextChangedListener(listenerWight);
        }
    }
}
