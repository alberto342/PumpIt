package com.albert.fitness.pumpit.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkoutRepository {
    private WorkoutDAO workoutDAO;
    private WorkoutPlanDAO workoutPlanDAO;
    private DateDAO dateDAO;
    private TrainingDAO trainingDAO;
    private TrackerExerciseDAO trackerExerciseDAO;
    private LiveData<List<WorkoutPlanObj>> workoutPlans;
    private LiveData<List<WorkoutObj>> workouts;
    private LiveData<List<DateObj>> dates;
    private LiveData<List<Training>> training;
    private LiveData<List<TrackerExercise>> trackerExercise;
    private Executor executor;

    public WorkoutRepository(Application application) {
        executor = Executors.newFixedThreadPool(5);
        WorkoutDatabase workoutDatabase = WorkoutDatabase.getInstance(application);
        workoutDAO = workoutDatabase.workoutDAO();
        workoutPlanDAO = workoutDatabase.workoutPlanDAO();
        dateDAO = workoutDatabase.dateDAO();
        trainingDAO = workoutDatabase.trainingDAO();
        trackerExerciseDAO = workoutDatabase.trackerExerciseDAO();
    }

    public LiveData<List<DateObj>> getDate() {
        return dateDAO.getAllDate();
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

    public LiveData<List<Training>> getTrainingByWorkoutId(int workoutId) {
        return trainingDAO.getTrainings(workoutId);
    }

    public LiveData<List<Training>> getTrainingExerciseName(int exercise) {
        return trainingDAO.getExercise(exercise);
    }

    public LiveData<List<WorkoutObj>> getWorkouts(int workoutId) {
        return workoutDAO.getWorkout(workoutId);
    }

    public LiveData<List<WorkoutPlanObj>> getPlansByDate(int planId) {
        return workoutPlanDAO.getPlansByDate(planId);
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

    public void insertDate(final DateObj date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dateDAO.insert(date);
            }
        });
    }

    public void insertWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutDAO.insert(workout);
            }
        });
    }

    public void insertPlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutPlanDAO.insert(plan);
            }
        });
    }

    public void insertTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
               trainingDAO.insert(training);
            }
        });
    }

    public void insertTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                trackerExerciseDAO.insert(trackerExercise);
            }
        });
    }

    public void deleteDate(final DateObj date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dateDAO.delete(date);
            }
        });
    }

    public void deletePlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutPlanDAO.delete(plan);
            }
        });
    }

    public void deleteWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutDAO.delete(workout);
            }
        });
    }

    public void deleteTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                trainingDAO.delete(training);
            }
        });
    }

    public void deleteTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                trackerExerciseDAO.delete(trackerExercise);
            }
        });
    }

    public void updateDate(final DateObj date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dateDAO.update(date);
            }
        });
    }

    public void updatePlan(final WorkoutPlanObj plan) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutPlanDAO.update(plan);
            }
        });
    }

    public void updateWorkout(final WorkoutObj workout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutDAO.update(workout);
            }
        });
    }

    public void updateTraining(final Training training) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                trainingDAO.update(training);
            }
        });
    }

    public void updateTTrackerExercise(final TrackerExercise trackerExercise) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                trackerExerciseDAO.update(trackerExercise);
            }
        });
    }
}
