package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.DateObj;
import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutRepository;

import java.util.List;

public class CustomPlanViewModel extends AndroidViewModel {


    private WorkoutRepository repository;
    private LiveData<WorkoutPlanObj> workoutPlanObjLiveData;
    private LiveData<List<WorkoutPlanObj>> workoutPlans;
    private LiveData<List<WorkoutObj>> workouts;
    private LiveData<List<DateObj>> dates;
    private LiveData<List<Training>> training;
    private LiveData<List<TrackerExercise>> trackerExercise;


    public CustomPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
    }

    public LiveData<List<WorkoutPlanObj>> getAllPlan() {
        return workoutPlans = repository.getPlansByDate();
    }

    public LiveData<List<DateObj>> getAllDates() {
        return dates = repository.getDate();
    }

    public LiveData<List<WorkoutObj>> getAllWorkouts() {
        return workouts = repository.getWorkout();
    }

    public LiveData<List<Training>> getAllTraining() {
        return training = repository.getTraining();
    }

    public LiveData<List<TrackerExercise>> getAllTracker() {
        return  trackerExercise = repository.getAllTracker();
    }

    public LiveData<WorkoutPlanObj> getPlan(int id) {
        return  workoutPlanObjLiveData = repository.getPlan(id);
    }

    public LiveData<List<WorkoutPlanObj>> getPlaneOfASelectedDate(int id) {
        return workoutPlans = repository.getPlansByDate(id);
    }


    public LiveData<List<WorkoutObj>> getWorkoutNAmeOfASelectedPlan(int id) {
        return workouts = repository.getWorkouts(id);
    }

    public LiveData<List<Training>> getTrainingByWorkoutId(int id) {
        return  training = repository.getTrainingByWorkoutId(id);
    }

    public LiveData<List<TrackerExercise>> getTrackerExerciseByTraining(int id) {
        return trackerExercise = repository.getTrackerExerciseByTrainingId(id);
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

    public void addNewTraining(Training training) {
        repository.insertTraining(training);
    }

    public void addNewTracker(TrackerExercise trackerExercise) {
        repository.insertTrackerExercise(trackerExercise);
    }

    public void updateWorkoutPlan(WorkoutPlanObj plan){
        repository.updatePlan(plan);
    }

    public void deleteWorkoutPlan(WorkoutPlanObj plan){
       repository.deletePlan(plan);
    }


}
