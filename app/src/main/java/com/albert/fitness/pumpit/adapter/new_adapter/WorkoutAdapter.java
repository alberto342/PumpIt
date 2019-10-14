package com.albert.fitness.pumpit.adapter.new_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.WorkoutObj;

import java.util.ArrayList;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ItemWorkoutBinding;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutObjViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<WorkoutObj> items = new ArrayList<>();

    @NonNull
    @Override
    public WorkoutObjViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemWorkoutBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_workout, viewGroup, false);
        return new WorkoutObjViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutObjViewHolder viewHolder, int i) {
        WorkoutObj item = items.get(i);
        viewHolder.listItemBinding.setWorkout(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<WorkoutObj> newItems) {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new WorkoutObjDiffCallback(items, newItems), false);
        items = newItems;
        result.dispatchUpdatesTo(WorkoutAdapter.this);
    }

    class WorkoutObjViewHolder extends RecyclerView.ViewHolder {
        private ItemWorkoutBinding listItemBinding;

        public WorkoutObjViewHolder(@NonNull ItemWorkoutBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
            listItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(clickedPosition));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WorkoutObj item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class WorkoutObjDiffCallback extends DiffUtil.Callback {
        ArrayList<WorkoutObj> oldItemsList;
        ArrayList<WorkoutObj> newItemList;

        public WorkoutObjDiffCallback(ArrayList<WorkoutObj> oldItemsList, ArrayList<WorkoutObj> newItemList) {
            this.oldItemsList = oldItemsList;
            this.newItemList = newItemList;
        }

        @Override
        public int getOldListSize() {
            return oldItemsList == null ? 0 : oldItemsList.size();
        }

        @Override
        public int getNewListSize() {
            return newItemList == null ? 0 : newItemList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItemsList.get(oldItemPosition).getWorkoutId() == newItemList.get(newItemPosition).getWorkoutId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItemsList.get(oldItemPosition).equals(newItemList.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }
}