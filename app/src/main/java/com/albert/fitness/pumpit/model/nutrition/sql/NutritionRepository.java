package com.albert.fitness.pumpit.model.nutrition.sql;

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


    private Executor executor;

    public NutritionRepository(Application application) {
        executor = Executors.newFixedThreadPool(5);
        NutritionDatabase nutritionDatabase = NutritionDatabase.getInstance(application);
        mFoodsDAO = nutritionDatabase.foodsDAO();
        altMeasuresDAO = nutritionDatabase.altMeasuresDAO();
        fullNutritionDAO = nutritionDatabase.fullNutritionDAO();
        nutritionDAO = nutritionDatabase.nutritionDAO();
        photoDAO = nutritionDatabase.photoDAO();
        tagsTAO = nutritionDatabase.tagsTAO();


    }

    public LiveData<List<FoodsObj>> getAllFoods() {
        return mFoodsDAO.getAllFoodsObj();
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


    public LiveData<List<AltMeasures>> getAltMeasuresById(int id) {
        return altMeasuresDAO.getAltMeasuress(id);
    }

    public LiveData<List<FullNutrition>> getFullNutritionById(int id) {
        return fullNutritionDAO.getFullNutritions(id);
    }

    public LiveData<List<Nutrition>> getNutritionById(int id) {
        return nutritionDAO.getNutritions(id);
    }


    public LiveData<List<Photo>> getPhotoById(int id) {
        return photoDAO.getPhotos(id);
    }

    public LiveData<List<Tags>> getTagsbyId(int id) {
        return tagsTAO.getTagss(id);
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





}
