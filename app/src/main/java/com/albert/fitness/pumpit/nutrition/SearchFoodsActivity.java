package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.adapter.FoodListAdapter;
import com.albert.fitness.pumpit.api.RestApi;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.nutrition.FoodListResponse;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.room.AltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.FoodsObj;
import com.albert.fitness.pumpit.model.nutrition.room.FullNutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Nutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Photo;
import com.albert.fitness.pumpit.model.nutrition.room.Tags;
import com.albert.fitness.pumpit.retro_request.FoodRequest;
import com.albert.fitness.pumpit.utils.Global;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fitness.albert.com.pumpit.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFoodsActivity extends AppCompatActivity {

    private String TAG = "SearchFoodsActivity";
    public static ArrayList<Foods> mListItem = new ArrayList<>();
    private PrefsUtils prefsUtils = new PrefsUtils();
    //    private ArrayList<Common> commonArrayList = new ArrayList<>();
//    CommonListAdapter commonListAdapter;
    FoodListAdapter foodListAdapter;
    RecyclerView rvList;
    Button btnSaveAllFood;
    TextView tvEmpty;
    RestApi api;
    private Foods foods = new Foods();
    private MaterialSearchBar searchBar;
    private NutritionViewModel viewModel;
    private int foodId;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foods);
        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);
        prefsUtils = new PrefsUtils(this, "suggestions");
        getLastFoodId();
        //checkPermission();
        Objects.requireNonNull(getSupportActionBar()).hide();
        api = Global.initRetrofit();
        findViews();
    }


    @SuppressLint("WrongConstant")
    private void findViews() {
        tvEmpty = findViewById(R.id.tvEmpty);
        rvList = findViewById(R.id.rvList);
        btnSaveAllFood = findViewById(R.id.btn_save_all_food);
        searchBar = findViewById(R.id.food_search_bar);
        loadSuggestions();
        //   searchBar.setCardViewElevation(25);
        pb = findViewById(R.id.pb_search_food);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searchBarClicked();

        btnSaveAllFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFoodNameExisting();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }

    private void getLastFoodId() {
        viewModel.getLastFoodId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    foodId = integer + 1;
                } else {
                    foodId = 1;
                }
            }
        });
    }


    private void searchBarClicked() {
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSearchConfirmed(CharSequence text) {

                if (Global.isNetworkAvailable(SearchFoodsActivity.this)) {
                    getFoodList();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                        openVoiceRecognizer();
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        searchBar.disableSearch();
                }
            }
        });
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getFoodList() {
        pb.setVisibility(View.VISIBLE);

        FoodRequest foodRequest = null;
        final String search = searchBar.getText();

        if (search.length() == 0) {
            Toast.makeText(this, "Please Enter food name", Toast.LENGTH_SHORT).show();
        } else {
            foodRequest = new FoodRequest(search);
        }
        Call<FoodListResponse> call = api.foodList(Global.x_app_id, Global.x_app_key, foodRequest);

        call.enqueue(new Callback<FoodListResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<FoodListResponse> call, @NonNull Response<FoodListResponse> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getFoods().size() > 0) {
                        tvEmpty.setVisibility(View.GONE);
                        searchBar.setText("");

                        if (response.body().getFoods().size() > 1) {
                            btnSaveAllFood.setVisibility(View.VISIBLE);
                        } else {
                            btnSaveAllFood.setVisibility(View.INVISIBLE);
                        }
                        mListItem = response.body().getFoods();
                        foodListAdapter = new FoodListAdapter(SearchFoodsActivity.this, mListItem);
                        rvList.setAdapter(foodListAdapter);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(SearchFoodsActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<FoodListResponse> call, @NonNull Throwable t) {
                pb.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }


//    private void getCommonList() {
//        FoodRequest foodRequest = null;
//        final String search = searchBar.getText();
//
//        if (search.length() == 0) {
//            Toast.makeText(this, "Please Enter food name", Toast.LENGTH_SHORT).show();
//        } else {
//            foodRequest = new FoodRequest(search);
//        }
//
//        Call<CommonListResponse> call = api.foodListInstant(Global.x_app_id, Global.x_app_key, foodRequest);
//
//        call.enqueue(new Callback<CommonListResponse>() {
//            @SuppressLint("ShowToast")
//            @Override
//            public void onResponse(@NonNull Call<CommonListResponse> call, @NonNull Response<CommonListResponse> response) {
//                if (response.body() != null) {
//                    commonArrayList = response.body().getFoods();
//                    Log.d(TAG, "LOLO:" + commonArrayList.get(0).getFoodName());
//
//                    commonListAdapter = new CommonListAdapter(SearchFoodsActivity.this, commonArrayList);
//                    rvList.setAdapter(commonListAdapter);
//                } else {
//                    Toast.makeText(SearchFoodsActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<CommonListResponse> call, @NonNull Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

    private void checkFoodNameExisting() {
        if (!mListItem.isEmpty()) {
            for (final Foods foods : mListItem) {
                viewModel.getFoodIdByFoodName(foods.getFoodName())
                        .observe(this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer id) {
                                if (id == null) {
                                    saveNutrition(foods);
                                } else {
                                    Log.d(TAG, "Save Food: NOT SAVING FoodNameExisting + id: " + id);
                                    getAltMeasuresId(id, foods);
                                    Log.d(TAG, "saveFoodLogs id: " + id + ", EatType: " + getEatType() + ", Date:" + Event.getTodayData());
                                    finish();
                                }
                            }
                        });
            }
        }
    }

    private void saveNutrition(Foods foods) {
        List<FoodsObj> foodsObjList = new ArrayList<>();
        List<Nutrition> nutritionList = new ArrayList<>();
        List<FullNutrition> fullNutritionList = new ArrayList<>();
        List<AltMeasures> altMeasuresList = new ArrayList<>();
        List<Photo> photoList = new ArrayList<>();
        List<Tags> tagsList = new ArrayList<>();

        try {
            foodsObjList.add(new FoodsObj(foods.getFoodName()));

            photoList.add(new Photo(foodId, foods.getPhoto().getHighres(), foods.getPhoto().getThumb()));

            tagsList.add(new Tags(foodId, foods.getTags().getFoodGroup(),
                    foods.getTags().getItem(), foods.getTags().getMeasure(),
                    foods.getTags().getQuantity(), foods.getTags().getTagId()));

            nutritionList.add(new Nutrition(foodId, foods.getNfCalories(),
                    foods.getNfDietaryFiber(), foods.getNfCholesterol(), foods.getNfProtein(),
                    foods.getNfSaturatedFat(), foods.getNfTotalFat(), foods.getNfSugars(),
                    foods.getNfTotalCarbohydrate(), 1, foods.getServingUnit(),
                    (int) foods.getServingWeightGrams(), foods.getSource()));

            for (int j = 0; j < foods.getFullNutrients().size(); j++) {
                fullNutritionList.add(new FullNutrition(foodId,
                        foods.getFullNutrients().get(j).getAttrId(),
                        foods.getFullNutrients().get(j).getValue()));
            }

            for (int j = 0; j < foods.getAltMeasures().size(); j++) {
                altMeasuresList.add(new AltMeasures(foodId,
                        foods.getAltMeasures().get(j).getMeasure(),
                        foods.getAltMeasures().get(j).getQty(),
                        Integer.valueOf(foods.getAltMeasures().get(j).getSeq()),
                        Integer.valueOf(foods.getAltMeasures().get(j).getServingWeight())));
            }

            arrayListIntoClass();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewModel.addNewAllNutrition(foodsObjList, nutritionList, fullNutritionList, altMeasuresList, photoList, tagsList);
        // getAltMeasuresId(foodId, foods);
        foodId++;

        Event.saveEvent(this);

        Toast.makeText(SearchFoodsActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void getAltMeasuresId(final int id, final Foods foods) {
        viewModel.getAltMeasuresIdByFoodId(id).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer measuresId) {
                if (measuresId != null) {
                    Log.d(TAG, "getAltMeasuresId: " + measuresId + " foodId: " + id + " qty: " + foods.getServingQty());
                    viewModel.addNewFoodLog(new FoodLog(id, measuresId, foods.getServingQty(), getEatType(), Event.getTodayData()));
                    Event.saveEvent(SearchFoodsActivity.this);
                }
            }
        });
    }


    private String getEatType() {
        final PrefsUtils prefsUtils = new PrefsUtils(this, Foods.SHARED_PREFERENCES_FILE);
        boolean dinner = prefsUtils.getBoolean(Foods.DINNER, false);
        boolean breakfast = prefsUtils.getBoolean(Foods.BREAKFAST, false);
        boolean lunch = prefsUtils.getBoolean(Foods.LUNCH, false);
        if (dinner) {
            return Foods.DINNER;
        } else if (breakfast) {
            return Foods.BREAKFAST;
        } else if (lunch) {
            return Foods.LUNCH;
        } else {
            return Foods.SNACK;
        }
    }


    private void openVoiceRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            a.getMessage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(result.get(0));
                getFoodList();
            }
        }
    }

    private void arrayListIntoClass() {
        for (int i = 0; i < mListItem.size(); i++) {
            foods.setFoodName(mListItem.get(i).getFoodName());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed searchBar LastSuggestions: " + searchBar.getLastSuggestions());
        int size = searchBar.getLastSuggestions().size();
        prefsUtils.saveData("suggestionsSize", size);
        for (int i = 0; i < size; i++) {
            prefsUtils.saveData("k" + i, searchBar.getLastSuggestions().get(i).toString());
        }
    }

    private void loadSuggestions() {
        List<String> lastSearches = new ArrayList<>();
        //if(searchBar.getLastSuggestions() != null) {

        int size = prefsUtils.getInt("suggestionsSize", -1);

        if(size != -1) {
            for (int i = 0; i < size; i++) {
                if (!prefsUtils.getString("k" + i, "").isEmpty()) {
                    lastSearches.add(prefsUtils.getString("k" + i, ""));
                }
            }
        }
        searchBar.setLastSuggestions(lastSearches);

//        if (lastSearches.size() > 0) {
//            searchBar.setLastSuggestions(lastSearches);
//        }
    }
    // }
}
