package com.albert.fitness.pumpit.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.TrackerExercise;

import java.util.ArrayList;

import fitness.albert.com.pumpit.R;


public class TrackerExerciseAdapter extends RecyclerView.Adapter<TrackerExerciseAdapter.ViewHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<TrackerExercise> trackerList;

    public TrackerExerciseAdapter(ArrayList<TrackerExercise> modelMains) {
        this.trackerList = modelMains;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        TrackerExerciseAdapter.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return trackerList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_start_workout_reps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.listenerWight.updatePosition(position);
        holder.listenerReps.updatePosition(position);

        holder.tvCount.setText(String.valueOf(position+1));
        holder.edWight.setText(String.valueOf(trackerList.get(position).getWeight()));
        holder.edReps.setText(String.valueOf(trackerList.get(position).getRepsNumber()));

        if(trackerList.get(position).getWeight() == 0.0) {
            holder.edWight.setText("");
            holder.btnAdd.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_add_circle_white_24dp));
            holder.btnAdd.setTag(R.drawable.ic_add_circle_white_24dp);
        }

        if(trackerList.get(position).getRepsNumber() == 0) {
            holder.edReps.setText("");
            holder.btnAdd.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_add_circle_white_24dp));
            holder.btnAdd.setTag(R.drawable.ic_add_circle_white_24dp);
        }


        if(holder.edWight.getText().length() > 0) {
            holder.btnAdd.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_remove_circle_white_24dp));
            holder.btnAdd.setTag(R.drawable.ic_remove_circle_white_24dp);
        }else {
            holder.btnAdd.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_add_circle_white_24dp));
            holder.btnAdd.setTag(R.drawable.ic_add_circle_white_24dp);
        }


//        if (trackerList.get(position).isBtnPlus()) {
//            holder.btnAdd.setText("+");
//            holder.btnAdd.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.button_design_plus));
//        } else {
//            holder.btnAdd.setText("-");
//            holder.btnAdd.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.button_design_minus));
//        }
    }


    public void addItem(TrackerExercise trackerExercise) {
        trackerList.add(trackerExercise);
        notifyDataSetChanged();
    }


    public void deleteItem(int index) {
        if (trackerList.size() > index) {
            trackerList.remove(index);
            notifyDataSetChanged();
           // totalPrice();
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
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

            if (editText.getId() == R.id.et_reps_set_workout) {
                if (String.valueOf(trackerList.get(position).getRepsNumber()).equals(charSequence.toString()))
                    return;
                else {
                    trackerList.get(position).setRepsNumber(Integer.parseInt(charSequence.toString()));

                }
            } else if (editText.getId() == R.id.et_wight_set_workout) {
                if (String.valueOf(trackerList.get(position).getWeight()).equals(charSequence.toString())) {
                    return;
                } else {
                    trackerList.get(position).setWeight(Float.parseFloat(charSequence.toString()));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

//    private void totalPrice() {
//        int price = 0;
//        for (int j = 0; j < trackerList.size(); j++) {
//            price += trackerList.get(j).getPrice();
//        }
//
//        MainActivity.txtTotalPrice.setText("" + price);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView tvCount;
        EditText edWight, edReps;
        ImageView btnAdd;
        MyCustomEditTextListener listenerReps, listenerWight;

        public ViewHolder(View itemView) {
            super(itemView);
            edWight = itemView.findViewById(R.id.et_wight_set_workout);
            edReps = itemView.findViewById(R.id.et_reps_set_workout);
            btnAdd = itemView.findViewById(R.id.iv_finish_workout);
            tvCount = itemView.findViewById(R.id.tv_count_reps_added);
            btnAdd.setOnClickListener(this);
            this.listenerReps = new MyCustomEditTextListener(this.edReps);
            this.listenerWight = new MyCustomEditTextListener(this.edWight);
            this.edReps.addTextChangedListener(listenerReps);
            this.edWight.addTextChangedListener(listenerWight);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
}