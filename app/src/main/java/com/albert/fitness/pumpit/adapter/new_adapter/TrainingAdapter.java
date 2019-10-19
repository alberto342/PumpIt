package com.albert.fitness.pumpit.adapter.new_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.Exercise;
import com.albert.fitness.pumpit.model.Training;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ItemTrainingBinding;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<Training> items = new ArrayList<>();
    private List<Exercise> exerciseList;

    public TrainingAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemTrainingBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_training, viewGroup, false);
        return new TrainingViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder viewHolder, int i) {
        Training item = items.get(i);
        Exercise exercise = exerciseList.get(i);
        viewHolder.listItemBinding.setTraining(item);
        viewHolder.listItemBinding.setExercise(exercise);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Training> newItems) {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new TrainingDiffCallback(items, newItems), false);
        items = newItems;
        result.dispatchUpdatesTo(TrainingAdapter.this);
    }

    class TrainingViewHolder extends RecyclerView.ViewHolder {
        private ItemTrainingBinding listItemBinding;

        private TrainingViewHolder(@NonNull ItemTrainingBinding listItemBinding) {
            super(listItemBinding.getRoot());
            this.listItemBinding = listItemBinding;
            listItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(clickedPosition), clickedPosition);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Training item, int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class TrainingDiffCallback extends DiffUtil.Callback {
        ArrayList<Training> oldItemsList;
        ArrayList<Training> newItemList;

        private TrainingDiffCallback(ArrayList<Training> oldItemsList, ArrayList<Training> newItemList) {
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
            return oldItemsList.get(oldItemPosition).getTrainingId() == newItemList.get(newItemPosition).getTrainingId();
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