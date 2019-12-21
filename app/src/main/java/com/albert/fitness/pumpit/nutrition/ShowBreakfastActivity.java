package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.FullNutrients;
import com.albert.fitness.pumpit.model.nutrition.Tags;
import com.albert.fitness.pumpit.model.nutrition.room.AltMeasures;
import com.albert.fitness.pumpit.model.nutrition.room.FoodLog;
import com.albert.fitness.pumpit.model.nutrition.room.FullNutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Nutrition;
import com.albert.fitness.pumpit.model.nutrition.room.Photo;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.viewmodel.NutritionViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem, docId, oldServingUnit;
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
    private ProgressDialog progressdialog;
    private int firstQty;
    private ProgressBar progressBar;
    private NutritionViewModel viewModel;


    // TODO: 2019-07-09 save full nutrients not working, check fb saving

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
            String foodName = bundle.getString("foodName");

            viewModel.getFoodLogByLogId(logId).observe(this, new Observer<FoodLog>() {
                @Override
                public void onChanged(FoodLog foodLog) {
                    viewModel.getNutritionByFoodId(foodLog.getFoodId())
                            .observe(ShowBreakfastActivity.this, new Observer<Nutrition>() {
                                @Override
                                public void onChanged(Nutrition nutrition) {
                                    kcal = nutrition.getNfCalories();
                                    fat = nutrition.getNfTotalFat();
                                    protein = nutrition.getNfProtein();
                                    carbs = nutrition.getNfTotalCarbohydrate();
                                    servingWeightGrams = nutrition.getServingWeightGrams();
                                    int qty = nutrition.getServingQty();
                                    firstQty = nutrition.getServingQty();
                                    oldServingUnit = nutrition.getServingUnit();

                                    //DELEDED IF NOT NEED
                                    changeDataView(1);
                                    quantityViewCustom.setQuantity(qty);
                                }
                            });

                    viewModel.getFullNutritiionByFoodId(foodLog.getFoodId())
                            .observe(ShowBreakfastActivity.this, new Observer<List<FullNutrition>>() {
                                @Override
                                public void onChanged(List<FullNutrition> nutrition) {
                                    for(FullNutrition n : nutrition) {
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

                                            if(measures.getMeasure().isEmpty()) {
                                                spinnerList.add("Packet");
                                            }

                                            spinnerList.add(measures.getMeasure());
                                            servingWeightList.add((float) measures.getServingWeight());

                                            //add spinner to the map
                                            allServingWeight.put(measures.getMeasure(), (float) measures.getServingWeight());
//                                            if (servingWeightList.isEmpty()) {
//                                                allServingWeight.put("package", servingWeightGrams);
//                                            } else {
//
//                                            }
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

            //Get Serving Unit
            if (oldServingUnit == null) {
                spinnerList.add("Packet");
            } else {
                spinnerList.add(oldServingUnit);
            }
        }

        StringBuilder all = new StringBuilder();
        for (String fullNutrients : allFoods) {
            all.append(fullNutrients);
        }
        tvAllNutrition.setText(all.toString());

        progressBar.setVisibility(View.INVISIBLE);
        addItemsOnSpinner();
    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
            quantityViewCustom.setQuantity(newQuantity);
        }
        if (spinnerSelectedItem.equals("g")) {
            altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams / 100 / firstQty;
        } else {
            altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams / firstQty;
        }
        changeDataView(newQuantity);
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

                if (allServingWeight.get(spinnerSelectedItem) == null) {
                    FoodListAdapter.measureMap.put(spinnerSelectedItem, servingWeightGrams);
                    allServingWeight.put(spinnerSelectedItem, servingWeightGrams);
                }

                if (spinnerSelectedItem.equals("g")) {
                    altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams / 100 / firstQty;
                } else {
                    altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams / firstQty;
                }
                //Check if the position is change
                if (position > 0 || testOnce) {
                    testOnce = true;
                    quantityViewCustom.setQuantity(1);
                }
                if (spinnerSelectedItem.equals("g") && testOnce) {
                    quantityViewCustom.setQuantity(100);
                }
                changeDataView(quantityViewCustom.getQuantity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void changeDataView(int qty) {
        List<String> sAllFoods = new ArrayList<>();
        int gramSelected = oldServingUnit.equals("g") ? 100 : 1;

        for (int i = 0; i < atterId.size(); i++) {
            FullNutrients fullNutrients = new FullNutrients(atterId.get(i), values.get(i) * altMeasures * qty * gramSelected);
            sAllFoods.add(fullNutrients.getNutrients(atterId.get(i)));
        }

        tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal * altMeasures * qty * gramSelected));
        tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat * altMeasures * qty * gramSelected));
        tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein * altMeasures * qty * gramSelected));
        tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs * altMeasures * qty * gramSelected));

        StringBuilder all = new StringBuilder();
        for (String nutrients : sAllFoods) {
            all.append(nutrients);
        }
        tvAllNutrition.setText(all.toString());
    }

    //Update fb on backPressed
    private void updateNutrition() {

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(Foods.BREAKFAST).document(UserRegister.getTodayDate())
                .collection(Foods.All_NUTRITION)
                .whereEqualTo("foodName", BreakfastListAdapter.foodName)
                .whereEqualTo("servingUnit", oldServingUnit)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "document.getId " + document.getId() + " => " + document.getData());
                                docId = document.getId();
                            }
                            DocumentReference doc = db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                                    .collection(Foods.BREAKFAST).document(UserRegister.getTodayDate())
                                    .collection(Foods.All_NUTRITION).document(docId);

                            int qty = quantityViewCustom.getQuantity();

                            //   int notGrams = spinnerSelectedItem.equals("g") ? 1 : 100;
//                            doc.update("servingQty", qty);
//                            doc.update("servingUnit", spinnerSelectedItem);
//                            doc.update("nfCalories", kcal * altMeasures * qty * notGrams);
//                            doc.update("nfTotalFat", fat * altMeasures * qty * notGrams);
//                            doc.update("nfProtein", protein * altMeasures * qty * notGrams);
//                            doc.update("nfTotalCarbohydrate", carbs * altMeasures * qty * notGrams);
//                            doc.update("servingWeightGrams", allServingWeight.get(spinnerSelectedItem))

                            Log.d(TAG, "onComplete: " + tvAllNutrition.getText().toString());


                            for (int i = 0; i < values.size(); i++) {
                                @SuppressLint("UseSparseArrays")
                                Map<Integer, Float> fullNutrients = new HashMap<>();

                                fullNutrients.put(atterId.get(i), values.get(i));
                                //   FullNutrients fullNutrient = new FullNutrients(atterId.get(i),values.get(i));

                                // doc.update("fullNutrients", fullNutrients);
                            }


                            doc.update("servingQty", qty);
                            doc.update("servingUnit", spinnerSelectedItem);
                            doc.update("nfCalories", Float.valueOf(tvEnergy.getText().toString()));
                            doc.update("nfTotalFat", Float.valueOf(tvFat.getText().toString()));
                            doc.update("nfProtein", Float.valueOf(tvProtein.getText().toString()));
                            doc.update("nfTotalCarbohydrate", Float.valueOf(tvCarbs.getText().toString()));
                            doc.update("servingWeightGrams", allServingWeight.get(spinnerSelectedItem))

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressdialog.hide();
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");

                                            //oldServingUnit = spinnerSelectedItem;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressdialog.hide();
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
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
