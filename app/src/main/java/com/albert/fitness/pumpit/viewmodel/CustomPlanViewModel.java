package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.TrackerExercise;
import com.albert.fitness.pumpit.model.Training;
import com.albert.fitness.pumpit.model.WorkoutObj;
import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.model.WorkoutRepository;

import java.util.List;

public class CustomPlanViewModel extends AndroidViewModel {


    private WorkoutRepository repository;
    private LiveData<WorkoutPlanObj> workoutPlanObjLiveData;
    private LiveData<WorkoutObj> workoutObjLiveData;
    private LiveData<List<WorkoutPlanObj>> workoutPlans;
    private LiveData<List<WorkoutObj>> workouts;
    private LiveData<List<Training>> training;
    private LiveData<List<TrackerExercise>> trackerExercise;
    private LiveData<Integer> lastTrainingId;


    public CustomPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
    }

    public LiveData<List<WorkoutPlanObj>> getAllPlan() {
        return workoutPlans = repository.getPlansByDate();
    }

    public LiveData<List<WorkoutObj>> getAllWorkouts() {
        return workouts = repository.getWorkout();
    }

    public LiveData<List<Training>> getAllTraining() {
        return training = repository.getTraining();
    }

    public LiveData<Integer> getLastTrainingId() {
        return  lastTrainingId = repository.getLastTrainingId();
    }

    public LiveData<List<Training>> getAllTrainingByDate(String date) {
        return  training = repository.getAllTrainingByDate(date);
    }

    public LiveData<List<TrackerExercise>> getAllTracker() {
        return  trackerExercise = repository.getAllTracker();
    }

    public LiveData<WorkoutPlanObj> getPlan(int id) {
        return  workoutPlanObjLiveData = repository.getPlan(id);
    }

    public LiveData<List<WorkoutObj>> getWorkoutNAmeOfASelectedPlan(int id) {
        return workouts = repository.getWorkouts(id);
    }

    public LiveData<WorkoutObj> getWorkoutById(int id) {
        return workoutObjLiveData = repository.getWorkoutById(id);
    }

    public LiveData<WorkoutObj> getWorkoutByWorkoutDay(String workoutDay) {
        return workoutObjLiveData = repository.getWorkoutByWorkoutDay(workoutDay);
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

    public void addNewTraining(Training training) {
        repository.insertTraining(training);
    }

    public void addNewTrainingAndTracker(Training training, List<TrackerExercise> trackerExercises) {
        repository.insertTrainingAndTracker(training, trackerExercises);
    }

    public void addNewTracker(TrackerExercise trackerExercise) {
        repository.insertTrackerExercise(trackerExercise);
    }

    public void updateWorkoutPlan(WorkoutPlanObj plan){
        repository.updatePlan(plan);
    }

    public void updateWorkout(WorkoutObj workout) {
        repository.updateWorkout(workout);
    }

    public void updateTraining(Training training) {
        repository.updateTraining(training);
    }

    public void updateTrainingPosition(int newPosition, int position) {
        repository.updateTrainingPosition(newPosition, position);
    }

    public void updateTracker(TrackerExercise trackerExercise) {
        repository.updateTrackerExercise(trackerExercise);
    }

    public void deleteWorkoutPlan(WorkoutPlanObj plan){
       repository.deletePlan(plan);
    }

    public void deleteWorkout(WorkoutObj workout) {
        repository.deleteWorkout(workout);
    }

    public void deleteTraining(Training training) {
        repository.deleteTraining(training);
    }


}