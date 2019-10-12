package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.DateObj;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutRepository;

import java.util.List;

public class CustomPlanViewModel extends AndroidViewModel {


    private WorkoutRepository repository;
    private LiveData<List<WorkoutPlanObj>> workoutPlans;
    private LiveData<List<WorkoutObj>> workouts;
    private LiveData<List<DateObj>> dates;


    public CustomPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
    }

    public LiveData<List<WorkoutPlanObj>> getAllPlan() {
        return workoutPlans = repository.getPlans();
    }

    public LiveData<List<DateObj>> getAllDates() {
        return dates = repository.getDate();
    }

    public LiveData<List<WorkoutObj>> getAllWorkouts() {
        return workouts = repository.getWorkout();
    }

    public LiveData<List<WorkoutPlanObj>> getPlaneOfASelectedDate(int id) {
        return workoutPlans = repository.getPlans(id);
    }


    public LiveData<List<WorkoutObj>> getWorkoutNAmeOfASelectedPlan(int id) {
        return workouts = repository.getWorkouts(id);
    }

    public void addNewWorkout(WorkoutObj workout) {
        repository.insertWorkout(workout);
    }

    public void AddNewPlan(WorkoutPlanObj workoutPlan) {
        repository.insertPlan(workoutPlan);
    }

    public void addNewDate(DateObj date) {
        repository.insertDate(date);
    }


}
