package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.albert.fitness.pumpit.adapter.BreakfastListAdapter;
import com.albert.fitness.pumpit.adapter.FoodListAdapter;
import com.albert.fitness.pumpit.model.nutrition.FullNutrients;
import com.albert.fitness.pumpit.model.nutrition.Tags;
import com.albert.fitness.pumpit.model.nutrition.room.AltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.FullNutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Nutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Photo;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fitness.albert.com.pumpit.R;
import me.himanshusoni.quantityview.QuantityView;

public class ShowBreakfastActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private final String TAG = "ShowBreakfastActivity";
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem, oldServingUnit;
    private QuantityView quantityViewCustom;
    private TextView tvEnergy, tvCarbs, tvProtein, tvFat, tvAllNutrition, tvFoodName, tvGroupTag;
    private ImageView foodItem;
    private Map<String, Float> allServingWeight = new HashMap<>();
    private List<String> spinnerList = new ArrayList<>();
    private List<Float> servingWeightList = new ArrayList<>();
    private List<Integer> atterId = new ArrayList<>();
    private List<Float> values = new ArrayList<>();
    private float kcal, fat, protein, carbs, servingWeightGrams, altMeasures;
    private boolean testOnce = false;
    private int firstQty;
    private ProgressBar progressBar;
    private NutritionViewModel viewModel;
    private FoodLog foodLogs = new FoodLog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_breakfast);
        viewModel = ViewModelProviders.of(this).get(NutritionViewModel.class);

        init();
        Toolbar toolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getData();
        initCollapsingToolbar();
    }


    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving);
        tvEnergy = findViewById(R.id.tv_energy_item);
        tvCarbs = findViewById(R.id.tv_carbs_item);
        tvProtein = findViewById(R.id.tv_protein_item);
        tvFat = findViewById(R.id.tv_fat_item);
        foodItem = findViewById(R.id.iv_food_item);
        tvAllNutrition = findViewById(R.id.content_tv_all_nutrition);
        tvFoodName = findViewById(R.id.tv_show_food_name);
        tvGroupTag = findViewById(R.id.tv_show_group_tag);
        quantityViewCustom = findViewById(R.id.quantity_view);
        progressBar = findViewById(R.id.pb_show_food);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }

    // Initializing collapsing toolbar
    // Will show and hide the toolbar title on scroll
    private void initCollapsingToolbar() {
        final String foodName = BreakfastListAdapter.foodName;
        tvFoodName.setText(foodName);

        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.nutrition_collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = findViewById(R.id.nutrition_appbar);
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
                    collapsingToolbar.setTitle(foodName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    private void getData() {
        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        final List<String> allFoods = new ArrayList<>();

        if (bundle != null) {
            int logId = bundle.getInt("logId");

            viewModel.getFoodLogByLogId(logId)
                    .observe(this, new Observer<FoodLog>() {
                        @Override
                        public void onChanged(final FoodLog foodLog) {
                            foodLogs = foodLog;
                            int altMeasureId = foodLog.getAltMeasuresId();

                            viewModel.getAltMeasuresById(altMeasureId)
                                    .observe(ShowBreakfastActivity.this, new Observer<AltMeasures>() {
                                        @Override
                                        public void onChanged(AltMeasures altMeasures) {
                                            oldServingUnit = altMeasures.getMeasure();

                                            spinnerList.add(oldServingUnit == null ? "packet" : oldServingUnit);
                                            changeDataView();
                                            quantityViewCustom.setQuantity(foodLog.getQty());
                                            firstQty = foodLog.getQty();
                                            addItemsOnSpinner();
                                        }
                                    });

                            viewModel.getNutritionByFoodId(foodLog.getFoodId())
                                    .observe(ShowBreakfastActivity.this, new Observer<Nutrition>() {
                                        @Override
                                        public void onChanged(Nutrition nutrition) {
                                            kcal = nutrition.getNfCalories();
                                            fat = nutrition.getNfTotalFat();
                                            protein = nutrition.getNfProtein();
                                            carbs = nutrition.getNfTotalCarbohydrate();
                                            servingWeightGrams = nutrition.getServingWeightGrams();
                                            //  oldServingUnit = nutrition.getServingUnit();
                                        }
                                    });

                            viewModel.getFullNutritiionByFoodId(foodLog.getFoodId())
                                    .observe(ShowBreakfastActivity.this, new Observer<List<FullNutrition>>() {
                                        @Override
                                        public void onChanged(List<FullNutrition> nutrition) {
                                            for (FullNutrition n : nutrition) {
                                                atterId.add(n.getAtterId());
                                                values.add(n.getValue());

                                                FullNutrients fullNutrients = new FullNutrients(n.getAtterId(), n.getValue());
                                                allFoods.add(fullNutrients.getNutrients(n.getAtterId()));
                                            }
                                        }
                                    });

                            viewModel.getAltMeasuresByFoodId(foodLog.getFoodId())
                                    .observe(ShowBreakfastActivity.this, new Observer<List<AltMeasures>>() {
                                        @Override
                                        public void onChanged(List<AltMeasures> altMeasures) {
                                            if (!altMeasures.isEmpty()) {
                                                for (AltMeasures measures : altMeasures) {
                                                    if (measures.getMeasure().isEmpty()) {
                                                        spinnerList.add("Packet");
                                                    }
                                                    spinnerList.add(measures.getMeasure());
                                                    servingWeightList.add(measures.getServingWeight());

                                                    //add spinner to the map
                                                    allServingWeight.put(measures.getMeasure(), measures.getServingWeight());
                                                }
                                            }
                                        }
                                    });

                            viewModel.getPhotoByFoodId(foodLog.getFoodId())
                                    .observe(ShowBreakfastActivity.this, new Observer<Photo>() {
                                        @Override
                                        public void onChanged(Photo photo) {
                                            Picasso.get()
                                                    .load(photo.getHighres())
                                                    .placeholder(R.mipmap.ic_launcher)
                                                    .error(R.mipmap.ic_launcher)
                                                    .into(foodItem);
                                        }
                                    });
                            viewModel.getTagsByFoodId(foodLog.getFoodId())
                                    .observe(ShowBreakfastActivity.this, new Observer<com.albert.fitness.pumpit.model.nutrition.room.Tags>() {
                                        @Override
                                        public void onChanged(com.albert.fitness.pumpit.model.nutrition.room.Tags tags) {
                                            int foodGroup = tags.getFoodGroup();
                                            tvGroupTag.setText(Tags.foodGroup(foodGroup));
                                        }
                                    });
                        }
                    });
        }

        StringBuilder all = new StringBuilder();
        for (String fullNutrients : allFoods) {
            all.append(fullNutrients);
        }
        tvAllNutrition.setText(all.toString());

        progressBar.setVisibility(View.INVISIBLE);
    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
            quantityViewCustom.setQuantity(newQuantity);
        }
        if (spinnerSelectedItem.equals("g")) {
            altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams;
        } else {
            altMeasures = allServingWeight.get(spinnerSelectedItem) * newQuantity / servingWeightGrams;
        }
        changeDataView();
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
    @SuppressLint("NewApi")
    public void addItemsOnSpinner() {
        //Remove existing
        if (spinnerList.get(0) != null) {
            for (int i = 1; i < spinnerList.size(); i++) {
                if (spinnerList.get(0).equals(spinnerList.get(i))) {
                    spinnerList.remove(i);
                }
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServingUnit.setAdapter(dataAdapter);
        onItemSelectedListener();
    }


    //Wen user change the spinner selection
    private void onItemSelectedListener() {
        spinnerServingUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedItem = String.valueOf(spinnerServingUnit.getItemAtPosition(position));
                Log.d(TAG, "Spinner Item Position: " + spinnerSelectedItem);

                //Check if the position is change
                if (position > 0 || testOnce) {
                    testOnce = true;
                    quantityViewCustom.setQuantity(1);
                }

                if (allServingWeight.get(spinnerSelectedItem) == null) {
                    FoodListAdapter.measureMap.put(spinnerSelectedItem, servingWeightGrams);
                    allServingWeight.put(spinnerSelectedItem, servingWeightGrams);
                }

                if (spinnerSelectedItem.equals("g") && testOnce) {
                    quantityViewCustom.setQuantity(100);
                }

                if (spinnerSelectedItem.equals("g")) {
                    altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams;
                } else {
                    altMeasures = allServingWeight.get(spinnerSelectedItem) * quantityViewCustom.getQuantity() / servingWeightGrams;
                }
                Log.d(TAG, "onItemSelected: " + altMeasures);

                changeDataView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void changeDataView() {
        List<String> sAllFoods = new ArrayList<>();
        float newAltMeasures = altMeasures == 0.0 ? 1 : altMeasures;

        if (spinnerSelectedItem != null && spinnerSelectedItem.equals("g")) {
            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal / 100 * newAltMeasures * quantityViewCustom.getQuantity()));
            tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat / 100 * newAltMeasures * quantityViewCustom.getQuantity()));
            tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein / 100 * newAltMeasures * quantityViewCustom.getQuantity()));
            tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs / 100 * newAltMeasures * quantityViewCustom.getQuantity()));

            for (int i = 0; i < atterId.size(); i++) {
                FullNutrients fullNutrients = new FullNutrients(atterId.get(i), values.get(i) / 100 * newAltMeasures * quantityViewCustom.getQuantity());
                sAllFoods.add(fullNutrients.getNutrients(atterId.get(i)));
            }
        } else {
            Log.d(TAG, "changeData rr: " + kcal);
            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal * newAltMeasures));
            tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat * newAltMeasures));
            tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein * newAltMeasures));
            tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs * newAltMeasures));

            for (int i = 0; i < atterId.size(); i++) {
                FullNutrients fullNutrients = new FullNutrients(atterId.get(i), values.get(i) * newAltMeasures);
                sAllFoods.add(fullNutrients.getNutrients(atterId.get(i)));
            }
        }

        StringBuilder all = new StringBuilder();
        for (String nutrients : sAllFoods) {
            all.append(nutrients);
        }
        tvAllNutrition.setText(all.toString());
    }

    //Update fb on backPressed
    private void updateNutrition() {
        viewModel.getAltMeasuresIdByFoodIdAndMeasureType(foodLogs.getFoodId(), spinnerSelectedItem)
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer id) {
                        if (id != null) {
                            foodLogs.setAltMeasuresId(id);
                            foodLogs.setQty(quantityViewCustom.getQuantity());
                            viewModel.updateFoodLog(foodLogs);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (firstQty != quantityViewCustom.getQuantity() || !spinnerSelectedItem.equals(oldServingUnit)) {
            updateNutrition();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
