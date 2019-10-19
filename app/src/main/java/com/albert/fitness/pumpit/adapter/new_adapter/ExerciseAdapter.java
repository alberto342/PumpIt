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

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ExerciseListItemBinding;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<Exercise> items = new ArrayList<>();

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ExerciseListItemBinding listItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.exercise_list_item, viewGroup, false);
        return new ExerciseViewHolder(listItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder viewHolder, int i) {
        Exercise item = items.get(i);
        viewHolder.listItemBinding.setExercise(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Exercise> newList) {
        items = new ArrayList<>();
        items.addAll(newList);
        notifyDataSetChanged();
    }


    public void setItems(ArrayList<Exercise> newItems) {
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ExerciseDiffCallback(items, newItems), false);
        items = newItems;
        result.dispatchUpdatesTo(ExerciseAdapter.this);

    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private ExerciseListItemBinding listItemBinding;

        public ExerciseViewHolder(@NonNull ExerciseListItemBinding listItemBinding) {
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
        void onItemClick(Exercise item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ExerciseDiffCallback extends DiffUtil.Callback {
        ArrayList<Exercise> oldItemsList;
        ArrayList<Exercise> newItemList;

        public ExerciseDiffCallback(ArrayList<Exercise> oldItemsList, ArrayList<Exercise> newItemList) {
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
            return oldItemsList.get(oldItemPosition).getExerciseId() == newItemList.get(newItemPosition).getExerciseId();
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