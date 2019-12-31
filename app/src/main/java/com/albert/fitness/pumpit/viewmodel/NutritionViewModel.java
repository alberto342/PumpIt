package com.albert.fitness.pumpit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.albert.fitness.pumpit.model.nutrition.room.AltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.FoodsObj;
import com.albert.fitness.pumpit.model.nutrition.room.FullNutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Nutrition;
import com.albert.fitness.pumpit.model.nutrition.room.NutritionRepository;
import com.albert.fitness.pumpit.model.nutrition.room.Photo;
import com.albert.fitness.pumpit.model.nutrition.room.QueryAltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.QueryNutritionItem;
import com.albert.fitness.pumpit.model.nutrition.room.SumNutritionPojo;
import com.albert.fitness.pumpit.model.nutrition.room.Tags;
import com.albert.fitness.pumpit.model.nutrition.room.WaterTracker;

import java.util.List;

public class NutritionViewModel extends AndroidViewModel {

    private NutritionRepository repository;
    private LiveData<List<FoodsObj>> foodList;
    private LiveData<Integer> lastId;
    private LiveData<FoodsObj> food;

    public NutritionViewModel(@NonNull Application application) {
        super(application);
        repository = new NutritionRepository(application);
    }

    public LiveData<List<FoodsObj>> getAllFoods() {
        return foodList = repository.getAllFoods();
    }

    public LiveData<Integer> getLastFoodId() {
        return lastId = repository.getLastFoodId();
    }

    public LiveData<Integer> getFoodIdByFoodName(String name) {
        return repository.getFoodIdByFoodName(name);
    }

    public LiveData<List<FoodLog>> getFoodLogById(int id) {
        return repository.getFoodLogById(id);
    }

    public LiveData<FoodLog> getFoodLogByLogId(int id) {
        return repository.getFoodLogByLogId(id);
    }

    public LiveData<Nutrition> getNutritionByFoodId(int foodId) {
        return repository.getNutritionByFoodId(foodId);
    }

    public LiveData<List<Nutrition>> getNutritionByDateAndMealType(String date, String type) {
        return repository.getNutritionByDateAndMealType(date, type);
    }

    public LiveData<List<FullNutrition>> getFullNutritiionByFoodId (int id) {
        return repository.getFullNutritiionByFoodId(id);
    }

    public LiveData<SumNutritionPojo> getSumOfNutritionByDate(String date) {
        return repository.getSumOfNutritionByDate(date);
    }

    public LiveData<List<FoodsObj>> getAllFoodsById(int id) {
        return repository.getAllFoodsById(id);
    }

    public LiveData<Tags> getTagsByFoodId(int id) {
        return repository.getTagsByFoodId(id);
    }

    public LiveData<Integer> getAltMeasuresIdByFoodId(int id) {
        return repository.getAltMeasuresIdByNutritionTable(id);
    }

    public LiveData<Integer> getAltMeasuresIdByFoodIdAndMeasureType(int id, String measure) {
        return repository.getAltMeasuresIdByFoodIdAndMeasureType(id, measure);
    }

    public LiveData<AltMeasures> getAltMeasuresById(int id) {
        return  repository.getAltMeasuresById(id);
    }

    public LiveData<List<AltMeasures>> getAltMeasuresByFoodId(int id) {
        return repository.getAltMeasuresByFoodId(id);
    }

    public LiveData<List<QueryAltMeasures>> getAltMeasuresQtyAndWeight(String date, String type) {
        return repository.getAltMeasuresQtyAndWeight(date, type);
    }

    public LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealType(String date, String type) {
        return repository.getSumOfNutritionByDateAndMealType(date, type);
    }

    public LiveData<SumNutritionPojo> getSumOfNutritionByDateAndMealTypeWithGrams(String date, String type) {
        return repository.getSumOfNutritionByDateAndMealTypeWithGrams(date, type);
    }

    public LiveData<Photo> getPhotoByFoodId(int foodId) {
        return repository.getPhotoByFoodId(foodId);
    }

    public LiveData<List<Photo>> getPhotosByDateAndMealType(String date, String type) {
        return repository.getPhotosByDateAndMealType(date, type);
    }

    public LiveData<List<WaterTracker>> getAllWaterTracker(String water) {
        return repository.getAllWaterTracker();
    }

    public LiveData<List<QueryNutritionItem>> getNutritionItem(String date, String type) {
        return repository.getNutritionItem(date, type);
    }

    public void addNewFoodName(FoodsObj foodsObj) {
        repository.insertFoodName(foodsObj);
    }

    public void addNewAllNutrition(List<FoodsObj> foodsObj,
                                   List<Nutrition> nutrition,
                                   List<FullNutrition> fullNutritionList,
                                   List<AltMeasures> altMeasuresList,
                                   List<Photo> photo,
                                   List<Tags> tags) {
        repository.insertAllNutrition(foodsObj, nutrition, fullNutritionList, altMeasuresList, photo, tags);
    }

    public void addNewNutritionList(List<Nutrition> nutritionList) {
        repository.insertListNutrition(nutritionList);
    }

    public void addNewNutrition(Nutrition nutrition) {
        repository.insertNutrition(nutrition);
    }

    public void addNewAltMeasures(AltMeasures altMeasure) {
        repository.insertAltMeasures(altMeasure);
    }

    public void addNewFoodLog(FoodLog foodLog) {
        repository.insertLog(foodLog);
    }

    public void addNewFullNutrition(FullNutrition fullNutrition) {
        repository.insertFullNutrition(fullNutrition);
    }

    public void addNewPhoto(Photo photo) {
        repository.insertPhoto(photo);
    }

    public void addNewTags(Tags tags) {
        repository.insertTag(tags);
    }

    public void addNewWaterTracker(WaterTracker waterTracker) {
        repository.insertWaterTracker(waterTracker);
    }

    public void deleteFood(FoodsObj foods) {
        repository.deleteFoodName(foods);
    }

    public void deleteFoodLog(final FoodLog foodLog) {
        repository.deleteFoodLog(foodLog);
    }

    public void deleteFullNutrition(FullNutrition fullNutrition) {
        repository.deleteFullNutrition(fullNutrition);
    }

    public void deleteNutrition(Nutrition nutrition) {
        repository.deleteNutrition(nutrition);
    }

    public void deleteAltMeasures(final AltMeasures altMeasures) {
        repository.deleteAltMeasures(altMeasures);
    }

    public void deletePhoto(final Photo photo) {
        repository.deletePhoto(photo);
    }

    public void deleteTag(final Tags tags) {
        repository.deleteTag(tags);
    }

    public void deleteWaterTracker(final WaterTracker waterTracker) {
        repository.deleteWaterTracker(waterTracker);
    }

    public void updateFullNutrition(final FullNutrition fullNutrition) {
        repository.updateFullNutrition(fullNutrition);
    }

    public void updateNutrition(final Nutrition nutrition) {
        repository.updateNutrition(nutrition);
    }

    public void updateAltMeasures(final AltMeasures altMeasures) {
        repository.updateAltMeasures(altMeasures);
    }

    public void updateFoodLog(final FoodLog foodLog) {
        repository.updateFoodLog(foodLog);
    }
}
