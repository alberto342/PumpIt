package com.albert.fitness.pumpit.adapter.new_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.viewmodel.CustomPlanViewModel;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.databinding.ItemWorkoutBinding;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutObjViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<WorkoutObj> items = new ArrayList<>();
    private CustomPlanViewModel viewModel;
    private FragmentActivity fragmentActivity;

    public WorkoutAdapter(FragmentActivity context) {
        fragmentActivity = context;
        viewModel = ViewModelProviders.of(context).get(CustomPlanViewModel.class);
    }

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
        getSizeOfTraining(item.getWorkoutId(), viewHolder);
    }

    private void getSizeOfTraining(int workoutId, final WorkoutObjViewHolder viewHolder) {
        viewModel.getTrainingByWorkoutId(workoutId).observe(fragmentActivity, new Observer<List<Training>>() {
            @Override
            public void onChanged(List<Training> trainings) {
                if (trainings.isEmpty()) {
                    viewHolder.listItemBinding.setTrainingSize(0);
                    viewHolder.listItemBinding.setTotalTime(0);
                } else {
                    viewHolder.listItemBinding.setTrainingSize(trainings.size());

                    //Calculation total training
                    int totalTimeTraining = 0;
                    int countRestAfterExercise = 0;
                    int countRestBetweenSet = 0;
                    int sizeOfRept = 0;
                    float exerciseTime = 33 / 60;

                    for (Training training : trainings) {
                        countRestAfterExercise += training.getRestAfterExercise();
                        countRestBetweenSet += training.getRestBetweenSet();
                        sizeOfRept += training.getSizeOfRept();
                    }

                    float totalResetAfterExercise = countRestAfterExercise / 60;
                    float totalRestBetweenSet = countRestBetweenSet / 60;

                    totalTimeTraining += (exerciseTime * sizeOfRept) + (totalRestBetweenSet * sizeOfRept) + totalResetAfterExercise;
                    viewHolder.listItemBinding.setTotalTime(totalTimeTraining);
                }
            }
        });
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

        private WorkoutObjViewHolder(@NonNull ItemWorkoutBinding listItemBinding) {
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

        private WorkoutObjDiffCallback(ArrayList<WorkoutObj> oldItemsList, ArrayList<WorkoutObj> newItemList) {
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