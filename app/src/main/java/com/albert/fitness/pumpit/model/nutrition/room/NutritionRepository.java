package com.albert.fitness.pumpit.model.nutrition.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NutritionRepository {
    private FoodsDAO mFoodsDAO;
    private AltMeasuresDAO altMeasuresDAO;
    private FullNutritionDAO fullNutritionDAO;
    private NutritionDAO nutritionDAO;
    private PhotoDAO photoDAO;
    private TagsTAO tagsTAO;
    private FoodLogDAO foodLogDAO;
    private WaterTrackerDAO waterTrackerDAO;

    public NutritionRepository(Application application) {
        Executors.newFixedThreadPool(5);
        NutritionDatabase nutritionDatabase = NutritionDatabase.getInstance(application);
        mFoodsDAO = nutritionDatabase.foodsDAO();
        altMeasuresDAO = nutritionDatabase.altMeasuresDAO();
        fullNutritionDAO = nutritionDatabase.fullNutritionDAO();
        nutritionDAO = nutritionDatabase.nutritionDAO();
        photoDAO = nutritionDatabase.photoDAO();
        tagsTAO = nutritionDatabase.tagsTAO();
        foodLogDAO = nutritionDatabase.foodLogDAO();
        waterTrackerDAO = nutritionDatabase.waterTrackerDAO();

    }

    public LiveData<List<FoodsObj>> getAllFoods() {
        return mFoodsDAO.getAllFoodsObj();
    }

    public LiveData<Integer> getLastFoodId() {
        return mFoodsDAO.getLastFoodId();
    }

    public LiveData<List<AltMeasures>> getAllAltMeasures() {
        return altMeasuresDAO.getAllAltMeasures();
    }

    public LiveData<List<FullNutrition>> getFullAllNutrition() {
        return fullNutritionDAO.getAllFullNutrition();
    }

    public LiveData<List<Nutrition>> getAllNutrition() {
        return nutritionDAO.getAllNutrition();
    }

    public LiveData<List<Photo>> getAllPhoto() {
        return photoDAO.getAllPhoto();
    }

    public LiveData<List<Tags>> getAllTags() {
        return tagsTAO.getAllTags();
    }

    public LiveData<List<FoodLog>> getAllLogs() {
        return foodLogDAO.getAllFoodLog();
    }

    public LiveData<List<FoodsObj>> getAllFoodsById(int id) {
        return mFoodsDAO.getFoodsObjs(id);
    }

    public LiveData<AltMeasures> getAltMeasuresById(int id) {
        return altMeasuresDAO.getAltMeasures(id);
    }

    public LiveData<List<AltMeasures>> getAltMeasuresByFoodId(int id) {
        return altMeasuresDAO.getAltMeasuresByFoodId(id);
    }

    public LiveData<List<QueryAltMeasures>> getAltMeasuresQtyAndWeight(String date, String type) {
        return altMeasuresDAO.getAltMeasuresQtyAndWeight(date, type);
    }

    public LiveData<Integer> getAltMeasuresIdByNutritionTable(int id) {
        return altMeasuresDAO.getAltMeasuresIdByNutritionTable(id);
    }

    public LiveData<Integer> getAltMeasuresIdByFoodIdAndMeasureType(int id, String measure) {
        return altMeasuresDAO.getAltMeasuresIdByFoodIdAndMeasureType(id, measure);
    }

    public LiveData<List<FullNutrition>> getFullNutritionById(int id) {
        return fullNutritionDAO.getFullNutrition(id);
    }

    public LiveData<List<FullNutrition>> getFullNutritiionByFoodId(int id) {
        return fullNutritionDAO.getFullNutritiionByFoodId(id);
    }

    public LiveData<List<Nutrition>> getNutritionById(int id) {
        return nutritionDAO.getNutrition(id);
    }

    public LiveData<Nutrition> getNutritionByFoodId(int foodId) {
        return nutritionDAO.getNutritionByFoodId(foodId);
    }

    public LiveData<List<Nutrition>> getNutritionByDateAndMealType(String date, String type) {
        return nutritionDAO.getNutritionByDateAndMealType(date, type);
    }

    public LiveData<Photo> getPhotoById(int id) {
        return photoDAO.getPhotos(id);
    }

    public LiveData<Photo> getPhotoByFoodId(int foodId) {
        return photoDAO.getPhotosByFoodId(foodId);
    }

    public LiveData<List<Photo>> getPhotosByDateAndMealType(String date, String type) {
        return photoDAO.getPhotosByDateAndMealType(date, type);
    }

    public LiveData<List<Tags>> getTagsbyId(int id) {
        return tagsTAO.getTagss(id);
    }

    public LiveData<Tags> getTagsByFoodId(int id) {
        return tagsTAO.getTagsByFoodId(id);
    }

    public LiveData<List<FoodLog>> getFoodLogById(int id) {
        return foodLogDAO.getFoodLogs(id);
    }

    public LiveData<FoodLog> getFoodLogByLogId(int id) {
        return foodLogDAO.getFoodLogByLogId(id);
    }

    public LiveData<Integer> getFoodIdByFoodName(final String foodName) {
        return mFoodsDAO.getFoodIdByName(foodName);
    }

    public LiveData<SumNutritionPojo> getSumOfNutritionByDate(String date) {
        return foodLogDAO.getSumOfNutritionByDate(date);
    }

    public LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealType(String date, String type) {
        return foodLogDAO.getSumOfNutritionByDateAndMealType(date, type);
    }

    public LiveData<List<QueryNutritionItem>> getNutritionItem(String date, String type) {
        return mFoodsDAO.getNutritionItem(date, type);
    }


    public void insertAllNutrition(final List<FoodsObj> foodsObj,
                                   final List<Nutrition> nutritionList,
                                   final List<FullNutrition> fullNutritionList,
                                   final List<AltMeasures> altMeasuresList,
                                   final List<Photo> photoList,
                                   final List<Tags> tagsList) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFoodsDAO.insertAllNutrition(foodsObj, nutritionList, fullNutritionList,
                        altMeasuresList, photoList, tagsList);
            }
        });
    }

    public LiveData<List<WaterTracker>> getAllWaterTracker() {
        return waterTrackerDAO.getAllWaterTracker();
    }

    public void insertFoodName(final FoodsObj foodsObj) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFoodsDAO.insert(foodsObj);
            }
        });
    }

    public void insertListNutrition(final List<Nutrition> nutritionList) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                nutritionDAO.insert(nutritionList);
            }
        });
    }

    public void insertListFullNutrition(final List<FullNutrition> fullNutritionList) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fullNutritionDAO.insert(fullNutritionList);
            }
        });
    }

    public void insertListAltMeasures(final List<AltMeasures> altMeasuresList) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                altMeasuresDAO.insert(altMeasuresList);
            }
        });
    }

    public void insertLog(final FoodLog foodLog) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                foodLogDAO.insert(foodLog);
            }
        });
    }

    public void insertFullNutrition(final FullNutrition fullNutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fullNutritionDAO.insert(fullNutrition);
            }
        });
    }

    public void insertNutrition(final Nutrition nutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                nutritionDAO.insert(nutrition);
            }
        });
    }

    public void insertAltMeasures(final AltMeasures altMeasures) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                altMeasuresDAO.insert(altMeasures);
            }
        });
    }

    public void insertPhoto(final Photo photo) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDAO.insert(photo);
            }
        });
    }

    public void insertTag(final Tags tags) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                tagsTAO.insert(tags);
            }
        });
    }

    public void insertWaterTracker(final WaterTracker waterTracker) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                waterTrackerDAO.insert(waterTracker);
            }
        });
    }

    public void deleteFoodName(final FoodsObj foodsObj) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFoodsDAO.delete(foodsObj);
            }
        });
    }

    public void deleteFullNutrition(final FullNutrition fullNutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fullNutritionDAO.delete(fullNutrition);
            }
        });
    }

    public void deleteNutrition(final Nutrition nutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                nutritionDAO.delete(nutrition);
            }
        });
    }

    public void deleteAltMeasures(final AltMeasures altMeasures) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                altMeasuresDAO.delete(altMeasures);
            }
        });
    }

    public void deletePhoto(final Photo photo) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDAO.delete(photo);
            }
        });
    }

    public void deleteTag(final Tags tags) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                tagsTAO.delete(tags);
            }
        });
    }

    public void deleteFoodLog(final FoodLog foodLog) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                foodLogDAO.delete(foodLog);
            }
        });
    }

    public void deleteWaterTracker(final WaterTracker waterTracker) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                waterTrackerDAO.delete(waterTracker);
            }
        });
    }

    public void updateFoodName(final FoodsObj foodsObj) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mFoodsDAO.update(foodsObj);
            }
        });
    }

    public void updateFullNutrition(final FullNutrition fullNutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fullNutritionDAO.update(fullNutrition);
            }
        });
    }

    public void updateNutrition(final Nutrition nutrition) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                nutritionDAO.update(nutrition);
            }
        });
    }

    public void updateAltMeasures(final AltMeasures altMeasures) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                altMeasuresDAO.update(altMeasures);
            }
        });
    }

    public void updatePhoto(final Photo photo) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                photoDAO.update(photo);
            }
        });
    }

    public void updateTag(final Tags tags) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                tagsTAO.update(tags);
            }
        });
    }

    public void updateWaterTracker(final WaterTracker waterTracker) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                waterTrackerDAO.update(waterTracker);
            }
        });
    }

    public void updateFoodLog(final FoodLog foodLog) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                foodLogDAO.update(foodLog);
            }
        });
    }
}
