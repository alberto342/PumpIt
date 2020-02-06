package com.albert.fitness.pumpit.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutRepository {
    private WorkoutDAO workoutDAO;
    private WorkoutPlanDAO workoutPlanDAO;
    private TrainingDAO trainingDAO;
    private TrackerExerciseDAO trackerExerciseDAO;
    private FinishTrainingDAO finishTrainingDAO;

    public WorkoutRepository(Application application) {
        //    private LiveData<List<WorkoutPlanObj>> workoutPlans;
        //    private LiveData<List<WorkoutObj>> workouts;
        //    private LiveData<List<Training>> training;
        //    private LiveData<List<TrackerExercise>> trackerExercise;
        //    private LiveData<List<FinishTraining>> finishTraining;
        Executors.newFixedThreadPool(5);
        WorkoutDatabase workoutDatabase = WorkoutDatabase.getInstance(application);
        workoutDAO = workoutDatabase.workoutDAO();
        workoutPlanDAO = workoutDatabase.workoutPlanDAO();
        trainingDAO = workoutDatabase.trainingDAO();
        trackerExerciseDAO = workoutDatabase.trackerExerciseDAO();
        finishTrainingDAO = workoutDatabase.finishTrainingDAO();
    }


    public LiveData<List<WorkoutPlanObj>> getPlansByDate() {
        return workoutPlanDAO.getAllPlans();
    }

    public LiveData<List<WorkoutObj>> getWorkout() {
        return workoutDAO.getAllWorkout();
    }

    public LiveData<List<Training>> getTraining() {
        return trainingDAO.getAllTraining();
    }

    public LiveData<Integer> getLastTrainingId() {
        return trainingDAO.getLastTrainingId();
    }

    public LiveData<List<Training>> getTrainingByWorkoutId(int workoutId) {
        return trainingDAO.getTrainings(workoutId);
    }

    public LiveData<List<Training>> getAllTrainingByDate(String date) {
        return trainingDAO.getAllTrainingByDate(date);

    }

    public LiveData<List<Training>> getTrainingExerciseName(int exercise) {
        return trainingDAO.getExercise(exercise);
    }

    public LiveData<WorkoutObj> getWorkoutById(int workoutId) {
        return workoutDAO.getWorkoutById(workoutId);
    }

    public LiveData<List<WorkoutObj>> getWorkouts(int workoutId) {
        return workoutDAO.getWorkout(workoutId);
    }

    public LiveData<WorkoutObj> getWorkoutByWorkoutDay(String workoutDay) {
        return workoutDAO.getWorkoutByWorkoutDay(workoutDay);
    }

    public LiveData<WorkoutPlanObj> getPlan(int planId) {
        return workoutPlanDAO.getPlan(planId);
    }

    public LiveData<List<TrackerExercise>> getAllTracker() {
        return trackerExerciseDAO.getAllTrackerExercise();
    }

    public LiveData<List<TrackerExercise>> getTrackerExerciseByTrainingId(int trainingId) {
        return trackerExerciseDAO.getTrackerExercises(trainingId);
    }

    public LiveData<List<TrackerExercise>> getAllTrackerByFinishTrainingId(int id) {
        return trackerExerciseDAO.getAllTrackerByFinishTrainingId(id);
    }

    public LiveData<Integer> getLastFinishTrainingById() {
        return finishTrainingDAO.getLastFinishTrainingById();
    }

    public LiveData<FinishTraining> getFinishTrainingById(int id) {
        return finishTrainingDAO.getFinishTrainingById(id);
    }

    public LiveData<FinishTraining> getFinishTrainingByDate(String date) {
        return finishTrainingDAO.getFinishTrainingsByDate(date);
    }

    public LiveData<List<QueryFinishWorkout>> getFinishWorkout(String date) {
        return finishTrainingDAO.getFinishWorkout(date);
    }

    public LiveData<List<QueryFinishWorkout>> getFinishWorkoutByDateAndExerciseId(String date, int id) {
        return finishTrainingDAO.getFinishWorkoutByDateAndExerciseId(date, id);
    }

    public LiveData<List<Integer>> getExerciseIdByDate(String date) {
        return finishTrainingDAO.getExerciseIdByDate(date);
    }

    public LiveData<Integer> getMaxIdFromFinishTraining() {
        return finishTrainingDAO.getMaxIdFromFinishTraining();
    }

    public LiveData<List<QueryLogWorkout>> getLogWorkout(String date) {
        return finishTrainingDAO.getLogWorkout(date);
    }

    public LiveData<List<TrackerExercise>> getAllTrackerWhereFinishIdZero() {
        return trackerExerciseDAO.getAllTrackerWhereFinishIdZero();
    }

    public void insertWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutDAO.insert(workout));
    }

    public void insertPlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutPlanDAO.insert(plan));
    }

    public void insertTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.insert(training));
    }

    public void insertTrainingAndTracker(final Training training, final List<TrackerExercise> trackerExercises) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.insertTrainingAndTracker(training, trackerExercises));
    }

    public void insertTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trackerExerciseDAO.insert(trackerExercise));
    }

    public void insertFinishTraining(final FinishTraining finishTraining) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> finishTrainingDAO.insert(finishTraining));
    }

    public void insertFinishTrainingAndTracker(final FinishTraining finishTraining, final List<TrackerExercise> trackerExercises) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> finishTrainingDAO.insertFinishTrainingAndTracker(finishTraining, trackerExercises));
    }

    public void deletePlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutPlanDAO.delete(plan));
    }

    public void deleteWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutDAO.delete(workout));
    }

    public void deleteTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.delete(training));
    }

    public void deleteTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trackerExerciseDAO.delete(trackerExercise));
    }

    public void deleteTrackerExerciseByTrainingId(final int trainingId) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trackerExerciseDAO.deleteTrackerExerciseByTrainingId(trainingId));
    }

    public void deleteFinishTrainingByFinishId(final int finishId) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> finishTrainingDAO.deleteFinishTrainingByFinishId(finishId));
    }

    public void deleteTrackerExerciseByTrackerId(final int trackerId) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trackerExerciseDAO.deleteTrackerExerciseByTrackerId(trackerId));
    }

    public void updatePlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutPlanDAO.update(plan));
    }

    public void updateWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> workoutDAO.update(workout));
    }

    public void updateTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.update(training));
    }

    public void updateTrainingPosition(final int newPosition, final int position) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.updateTrainingPosition(newPosition, position));
    }

    public void updateTrainingRestExercise(int restBetweenSet, int restAfterExercise, int trainingId) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trainingDAO.updateTrainingRestExercise(restBetweenSet, restAfterExercise, trainingId));
    }

    public void updateTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> trackerExerciseDAO.update(trackerExercise));
    }
}
