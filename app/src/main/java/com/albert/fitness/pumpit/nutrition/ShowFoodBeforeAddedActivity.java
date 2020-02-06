package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.adapter.FoodListAdapter;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.FullNutrients;
import com.albert.fitness.pumpit.model.nutrition.Tags;
import com.albert.fitness.pumpit.model.nutrition.room.AltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.FoodsObj;
import com.albert.fitness.pumpit.model.nutrition.room.FullNutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Nutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Photo;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.R;
import me.himanshusoni.quantityview.QuantityView;

public class ShowFoodBeforeAddedActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private final String TAG = "ShowFoodBeforeAddedActivity";
    private QuantityView quantityViewCustom;
    private ProgressBar progressBar;
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem;
    private TextView tvEnergy, tvCrabs, tvProtein, tvFat, tvFullNutrition, tvFoodName, tvGroupTag;
    private ImageView ivFoodImg, btnSaveFood;
    private boolean testOnce = false;
    private CoordinatorLayout coordinatorLayout;
    private NutritionViewModel viewModel;
    private int foodId;
    private Foods foods = new Foods();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_before_added);

        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);
        getLastFoodId();
        init();

        foods = FoodListAdapter.mListItem.get(FoodListAdapter.mItemPosition);

        Toolbar toolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(toolbar);
        setTitle(foods.getFoodName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initCollapsingToolbar();

        getFoodInfo();

        addItemsOnSpinner();

        onAddButtonClick();
    }


    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving_unit);
        tvEnergy = findViewById(R.id.tv_energy);
        tvCrabs = findViewById(R.id.tv_carbs);
        tvProtein = findViewById(R.id.tv_protein);
        tvFat = findViewById(R.id.tv_fat);
        btnSaveFood = findViewById(R.id.btn_save_food);
        progressBar = findViewById(R.id.pb_show_food);
        ivFoodImg = findViewById(R.id.iv_food_img);
        tvFullNutrition = findViewById(R.id.tv_all_nutrition);
        tvFoodName = findViewById(R.id.tv_show_food_name);
        tvGroupTag = findViewById(R.id.tv_show_group_tag);
        quantityViewCustom = findViewById(R.id.quantityView_custom);
        quantityViewCustom.setOnQuantityChangeListener(this);
        coordinatorLayout = findViewById(R.id.main_content);
    }


    // Initializing collapsing toolbar
    // Will show and hide the toolbar title on scroll
    private void initCollapsingToolbar() {
        tvFoodName.setText(foods.getFoodName());
        //groupTag
        tvGroupTag.setText(Tags.foodGroup(foods.getTags().getFoodGroup()));

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(foods.getFoodName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    //Calculation the quantity + -
    @SuppressLint("LongLogTag")
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
            quantityViewCustom.setQuantity(newQuantity);
        }
        getFoodInfo();
        calculateOnSpinnerChange();
    }


    @Override
    public void onLimitReached() {
        Log.d(getClass().getSimpleName(), "Limit reached");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //Spinner Unit list
    @SuppressLint("LongLogTag")
    public void addItemsOnSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, FoodListAdapter.measure);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServingUnit.setAdapter(dataAdapter);

        spinnerServingUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinnerSelectedItem = String.valueOf(spinnerServingUnit.getItemAtPosition(position));

                Log.d(TAG, "Spinner Item Position: " + spinnerServingUnit.getItemAtPosition(position));

                //Check if the position is change
                if (position > 0 || testOnce) {
                    testOnce = true;
                    quantityViewCustom.setQuantity(1);
                }
                if (spinnerSelectedItem.equals("g")) {
                    quantityViewCustom.setQuantity(100);
                }
                calculateOnSpinnerChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @SuppressLint("LongLogTag")
    private void calculateOnSpinnerChange() {
        float kcal, crabs, protein, fat;
        List<String> fullNutrient = new ArrayList<>();

        float calAltMeasures;
        try {
            if (spinnerSelectedItem.equals("g")) {
                calAltMeasures = FoodListAdapter.measureMap.get(spinnerSelectedItem) / foods.getServingWeightGrams() / 100 * quantityViewCustom.getQuantity();
            } else {
                calAltMeasures = FoodListAdapter.measureMap.get(spinnerSelectedItem) / foods.getServingWeightGrams() * quantityViewCustom.getQuantity();
            }


            if (calAltMeasures == 0.0) {
                calAltMeasures = 1.0f;
            }

            kcal = foods.getNfCalories();
            crabs = foods.getNfTotalCarbohydrate();
            protein = foods.getNfProtein();
            fat = foods.getNfTotalFat();

            for (int r = 0; r < foods.getFullNutrients().size(); r++) {
                int atterId = foods.getFullNutrients().get(r).getAttrId();
                float value = foods.getFullNutrients().get(r).getValue();

                FullNutrients fullNutrients = new FullNutrients(atterId, value * calAltMeasures);
                fullNutrient.add(fullNutrients.getNutrients(atterId));
            }
            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f Kcal", kcal * calAltMeasures));
            tvCrabs.setText(String.format(Locale.getDefault(), "%.2f g", crabs * calAltMeasures));
            tvProtein.setText(String.format(Locale.getDefault(), "%.2f g", protein * calAltMeasures));
            tvFat.setText(String.format(Locale.getDefault(), "%.2f g", fat * calAltMeasures));

            StringBuilder all = new StringBuilder();

            for (String fullNutrients : fullNutrient) {
                all.append(fullNutrients);
            }
            tvFullNutrition.setText(all.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //get food info from searchFood
    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void getFoodInfo() {
        progressBar.setVisibility(View.INVISIBLE);

        int quantity = quantityViewCustom.getQuantity();

        List<String> fullNutrient = new ArrayList<>();

        String imgHigher = foods.getPhoto().getHighres();

        Picasso.get().load(imgHigher).error(R.mipmap.ic_launcher).into(ivFoodImg);

        float fKcal = foods.getNfCalories();
        String energy = String.format(Locale.getDefault(), "%.0f", fKcal * quantity);
        this.tvEnergy.setText(energy + " kcal");

        float fCrabs = foods.getNfTotalCarbohydrate();
        String crabs = String.format(Locale.getDefault(), "%.2f", fCrabs * quantity);
        this.tvCrabs.setText(crabs + " g");

        float fProtein = foods.getNfProtein();
        String protein = String.format(Locale.getDefault(), "%.2f", fProtein * quantity);
        this.tvProtein.setText(protein + " g");

        float fFat = foods.getNfTotalFat();
        String fat = String.format(Locale.getDefault(), "%.2f", fFat * quantity);
        this.tvFat.setText(fat + " g");

        for (int r = 0; r < foods.getFullNutrients().size(); r++) {
            int atterId = foods.getFullNutrients().get(r).getAttrId();
            float value = foods.getFullNutrients().get(r).getValue();

            FullNutrients fullNutrients = new FullNutrients(atterId, value * quantity);
            fullNutrient.add(fullNutrients.getNutrients(atterId));
        }
        StringBuilder all = new StringBuilder();

        for (String fullNutrients : fullNutrient) {
            all.append(fullNutrients);
        }
        tvFullNutrition.setText(all.toString());
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


    private void checkFoodNameExisting() {
        if (foods != null) {
            viewModel.getFoodIdByFoodName(foods.getFoodName())
                    .observe(this, new Observer<Integer>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onChanged(Integer id) {
                            Log.d(TAG, "checkFoodNameExisting: " + id);
                            if (id == null) {
                                saveNutrition(foods);
                            } else {
                                Log.d(TAG, "Save Food: NOT SAVING FoodNameExisting + id: " + id);
                                getAltMeasuresId(id, spinnerSelectedItem);
                            }
                        }
                    });
        }
    }


    public void getAltMeasuresId(final int id, String measure) {
        viewModel.getAltMeasuresIdByFoodIdAndMeasureType(id, measure)
                .observe(this, new Observer<Integer>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onChanged(Integer measureId) {
                        Log.d(TAG, "getAltMeasuresId: " + measureId);
                        if (measureId != null) {
                            viewModel.addNewFoodLog(new FoodLog(id, measureId, quantityViewCustom.getQuantity(), getMeal(), Event.getTodayData()));
                            Log.d(TAG, "Log is saved:  " + id + " getEatType: " + getMeal() + " Date: " + Event.getTodayData());
                        }
                    }
                });
    }


    @SuppressLint("LongLogTag")
    private void saveNutrition(Foods foods) {
        List<FoodsObj> foodsObjList = new ArrayList<>();
        List<Nutrition> nutritionList = new ArrayList<>();
        List<FullNutrition> fullNutritionList = new ArrayList<>();
        List<AltMeasures> altMeasuresList = new ArrayList<>();
        List<Photo> photoList = new ArrayList<>();
        List<com.albert.fitness.pumpit.model.nutrition.room.Tags> tagsList = new ArrayList<>();

        try {
            foodsObjList.add(new FoodsObj(foods.getFoodName()));

            photoList.add(new Photo(foodId, foods.getPhoto().getHighres(), foods.getPhoto().getThumb()));

            tagsList.add(new com.albert.fitness.pumpit.model.nutrition.room.Tags(foodId, foods.getTags().getFoodGroup(),
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
                Log.d(TAG, "saveNutrition qty: " + foods.getAltMeasures().get(j).getQty() + "\t ServingWeight: " + foods.getAltMeasures().get(j).getServingWeight() + "\t Measure: " + foods.getAltMeasures().get(j).getMeasure());

                if (foods.getAltMeasures().get(j).getSeq() == null) {
                    foods.getAltMeasures().get(j).setSeq("1");
                }

                altMeasuresList.add(new AltMeasures(foodId,
                        foods.getAltMeasures().get(j).getMeasure(),
                        foods.getAltMeasures().get(j).getQty(),
                        Integer.valueOf(foods.getAltMeasures().get(j).getSeq()),
                        Float.valueOf(foods.getAltMeasures().get(j).getServingWeight())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            viewModel.addNewAllNutrition(foodsObjList, nutritionList, fullNutritionList, altMeasuresList, photoList, tagsList);
            Event.saveEvent(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        foodId++;
    }


    //get Meal from SharedPreferences file
    public String getMeal() {
        SharedPreferences pref = getSharedPreferences(Foods.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean breakfast = pref.getBoolean("dinner", false);
        boolean dinner = pref.getBoolean("breakfast", false);
        boolean lunch = pref.getBoolean("lunch", false);
        boolean snack = pref.getBoolean("snack", false);

        if (breakfast) {
            return "dinner";
        }
        if (dinner) {
            return "breakfast";
        }
        if (lunch) {
            return "lunch";
        }
        if (snack) {
            return "snack";
        } else {
            return null;
        }
    }

    private void onAddButtonClick() {
        btnSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFoodNameExisting();
                final Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was save", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

                //save activity to sharedPreferences
                PrefsUtils prefsUtils = new PrefsUtils();
                prefsUtils.createSharedPreferencesFiles(ShowFoodBeforeAddedActivity.this, "activity");
                prefsUtils.saveData("fromActivity", "ShowFoodBeforeAddedActivity");
                //startActivity(new Intent(ShowFoodBeforeAddedActivity.this, FragmentNavigationActivity.class));
                FoodListAdapter.mListItem.clear();
                FoodListAdapter.measure.clear();
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FoodListAdapter.measure.clear();
    }
}