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

import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.model.UserRegister;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.R;
import com.albert.fitness.pumpit.adapter.FoodListAdapter;
import com.albert.fitness.pumpit.model.CalenderEvent;
import com.albert.fitness.pumpit.model.Event;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.model.nutrition.FullNutrients;
import com.albert.fitness.pumpit.utils.PrefsUtils;
import com.albert.fitness.pumpit.model.nutrition.Tags;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.himanshusoni.quantityview.QuantityView;

public class ShowFoodBeforeAddedActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    //Todo check if spinner is empty
    // TODO: 2019-07-09 on change quantity manuel its not show up

    private final String TAG = "ShowFoodBeforeAddedActivity";
    private QuantityView quantityViewCustom;
    private ProgressBar progressBar;
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem, foodName;
    private TextView tvEnergy, tvCrabs, tvProtein, tvFat, tvFullNutrition, tvFoodName, tvGroupTag;
    private ImageView ivFoodImg, btnSaveFood;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean testOnce = false;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_before_added);

        init();

        Toolbar toolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(toolbar);

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

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            foodName = FoodListAdapter.mListItem.get(i).getFoodName();
        }
        tvFoodName.setText(foodName);
        //groupTag
        tvGroupTag.setText(Tags.foodGroup(FoodListAdapter.mListItem.get(FoodListAdapter.mItemPosition).getTags().getFoodGroup()));

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
                    collapsingToolbar.setTitle(foodName);
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
        float kcal = 1, crabs = 1, protein = 1, fat = 1;
        List<String> fullNutrient = new ArrayList<>();

        float calAltMeasures;
        try {
            if (spinnerSelectedItem.equals("g")) {
                calAltMeasures = FoodListAdapter.measureMap.get(spinnerSelectedItem) / getServingWeightGrams() / 100 * quantityViewCustom.getQuantity();
            } else {
                calAltMeasures = FoodListAdapter.measureMap.get(spinnerSelectedItem) / getServingWeightGrams() * quantityViewCustom.getQuantity();
            }

            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
                kcal = FoodListAdapter.mListItem.get(i).getNfCalories();
                crabs = FoodListAdapter.mListItem.get(i).getNfTotalCarbohydrate();
                protein = FoodListAdapter.mListItem.get(i).getNfProtein();
                fat = FoodListAdapter.mListItem.get(i).getNfTotalFat();

                for (int r = 0; r < FoodListAdapter.mListItem.get(i).getFullNutrients().size(); r++) {
                    int atterId = FoodListAdapter.mListItem.get(i).getFullNutrients().get(r).getAttrId();
                    float value = FoodListAdapter.mListItem.get(i).getFullNutrients().get(r).getValue();

                    FullNutrients fullNutrients = new FullNutrients(atterId, value * calAltMeasures);
                    fullNutrient.add(fullNutrients.getNutrients(atterId));
                }
            }
            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f Kcal", kcal * calAltMeasures));
            tvCrabs.setText(String.format(Locale.getDefault(), "%.2f g", crabs * calAltMeasures));
            tvProtein.setText(String.format(Locale.getDefault(), "%.2f g", protein * calAltMeasures));
            tvFat.setText(String.format(Locale.getDefault(), "%.2f g", fat * calAltMeasures));

            StringBuilder all = new StringBuilder();

            for (String fullNutrients : fullNutrient) {
                all.append(fullNutrients);
                Log.d(TAG, "getFoodInfo: " + fullNutrients);
            }
            tvFullNutrition.setText(all.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getServingWeightGrams() {
        float servingWeight = 1;
        if (!FoodListAdapter.mListItem.isEmpty()) {
            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
                servingWeight = FoodListAdapter.mListItem.get(i).getServingWeightGrams();
            }
        }
        return servingWeight;
    }


    //get food info from searchFood
    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void getFoodInfo() {
        progressBar.setVisibility(View.INVISIBLE);

        int quantity = quantityViewCustom.getQuantity();

        List<String> fullNutrient = new ArrayList<>();

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            String imgHigher = FoodListAdapter.mListItem.get(i).getPhoto().getHighres();

            Picasso.get().load(imgHigher).error(R.mipmap.ic_launcher).into(ivFoodImg);

            float fKcal = FoodListAdapter.mListItem.get(i).getNfCalories();
            String energy = String.format(Locale.getDefault(), "%.0f", fKcal * quantity);
            this.tvEnergy.setText(energy + " kcal");

            float fCrabs = FoodListAdapter.mListItem.get(i).getNfTotalCarbohydrate();
            String crabs = String.format(Locale.getDefault(), "%.2f", fCrabs * quantity);
            this.tvCrabs.setText(crabs + " g");

            float fProtein = FoodListAdapter.mListItem.get(i).getNfProtein();
            String protein = String.format(Locale.getDefault(), "%.2f", fProtein * quantity);
            this.tvProtein.setText(protein + " g");

            float fFat = FoodListAdapter.mListItem.get(i).getNfTotalFat();
            String fat = String.format(Locale.getDefault(), "%.2f", fFat * quantity);
            this.tvFat.setText(fat + " g");

            for (int r = 0; r < FoodListAdapter.mListItem.get(i).getFullNutrients().size(); r++) {
                int atterId = FoodListAdapter.mListItem.get(i).getFullNutrients().get(r).getAttrId();
                float value = FoodListAdapter.mListItem.get(i).getFullNutrients().get(r).getValue();

                FullNutrients fullNutrients = new FullNutrients(atterId, value * quantity);
                fullNutrient.add(fullNutrients.getNutrients(atterId));
            }
            StringBuilder all = new StringBuilder();

            for (String fullNutrients : fullNutrient) {
                all.append(fullNutrients);
                Log.d(TAG, "getFoodInfo: " + fullNutrients);
            }
            tvFullNutrition.setText(all.toString());
        }
    }


    @SuppressLint("LongLogTag")
    private void saveDataToFirestore() {

        float measureMapIsNull = FoodListAdapter.measureMap.get(spinnerSelectedItem) == null ? 1 : FoodListAdapter.measureMap.get(spinnerSelectedItem);

        float calAltMeasures;

        if (spinnerSelectedItem == null) {
            spinnerSelectedItem = "1 packet";
        }


        if (spinnerSelectedItem.equals("g")) {
            calAltMeasures = measureMapIsNull / getServingWeightGrams() / 100 * quantityViewCustom.getQuantity();
        } else {
            calAltMeasures = measureMapIsNull / getServingWeightGrams() * quantityViewCustom.getQuantity();
        }

        float kcal = 1, crabs = 1, protein = 1, fat = 1, cholesterol = 1, dietaryFiber = 1,
                nfp = 1, potassium = 1, saturatedFat = 1, sodium = 1, sugars = 1;

        for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
            kcal = FoodListAdapter.mListItem.get(i).getNfCalories();
            crabs = FoodListAdapter.mListItem.get(i).getNfTotalCarbohydrate();
            protein = FoodListAdapter.mListItem.get(i).getNfProtein();
            fat = FoodListAdapter.mListItem.get(i).getNfTotalFat();
            cholesterol = FoodListAdapter.mListItem.get(i).getNfCholesterol();
            dietaryFiber = FoodListAdapter.mListItem.get(i).getNfDietaryFiber();
            nfp = FoodListAdapter.mListItem.get(i).getNfP();
            potassium = FoodListAdapter.mListItem.get(i).getNfPotassium();
            saturatedFat = FoodListAdapter.mListItem.get(i).getNfSaturatedFat();
            sodium = FoodListAdapter.mListItem.get(i).getNfSodium();
            sugars = FoodListAdapter.mListItem.get(i).getNfSugars();
        }

        try {
            for (int i = 0; i < FoodListAdapter.mListItem.size(); i++) {
                if (!FoodListAdapter.mListItem.get(i).getAltMeasures().isEmpty()) {
                    FoodListAdapter.mListItem.get(i).setServingWeightGrams(FoodListAdapter.measureMap.get(spinnerSelectedItem));
                    FoodListAdapter.mListItem.get(i).setServingUnit(spinnerSelectedItem);
                    FoodListAdapter.mListItem.get(i).setNfCalories(kcal * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfTotalCarbohydrate(crabs * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfProtein(protein * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfTotalFat(fat * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfCholesterol(cholesterol * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfDietaryFiber(dietaryFiber * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfP(nfp * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfPotassium(potassium * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfSaturatedFat(saturatedFat * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfSodium(sodium * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setNfSugars(sugars * calAltMeasures);
                    FoodListAdapter.mListItem.get(i).setServingQty(quantityViewCustom.getQuantity());
                }
                for (int j = 0; j < FoodListAdapter.mListItem.get(i).getAltMeasures().size(); j++) {
                    int atterId = FoodListAdapter.mListItem.get(i).getFullNutrients().get(j).getAttrId();

                    if (atterId == FoodListAdapter.mListItem.get(i).getFullNutrients().get(j).getAttrId()) {
                        float value = FoodListAdapter.mListItem.get(i).getFullNutrients().get(j).getValue();
                        FoodListAdapter.mListItem.get(i).getFullNutrients().get(j).setValue(value * calAltMeasures);
                    }
                }
            }

            db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                    .collection(getMeal()).document(UserRegister.getTodayDate())
                    .collection(Foods.All_NUTRITION)
                    .add(FoodListAdapter.mListItem.get(FoodListAdapter.mItemPosition))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Event.saveEvent(ShowFoodBeforeAddedActivity.this);
                            FoodListAdapter.measure.clear();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document ", e);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("LongLogTag")
    private void saveDataIntoRealm() {
        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name(CalenderEvent.REALM_FILE_EVENT).deleteRealmIfMigrationNeeded().build();
        Realm realm = Realm.getInstance(config);
        final CalenderEvent calenderEvent = new CalenderEvent(UserRegister.getTodayDate(), true, 0, 0);

        realm.getSchema();
        realm.executeTransaction(new Realm.Transaction() {
            @SuppressLint("LongLogTag")
            @Override
            public void execute(@NonNull Realm realm) {

                String date = realm.where(CalenderEvent.class).equalTo("date", UserRegister.getTodayDate()).findFirst().getDate();

                if (date != null && !date.equals(UserRegister.getTodayDate())) {
                    Number currentIdNum = realm.where(CalenderEvent.class).max("id");
                    int nextId;

                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }

                    calenderEvent.setId(nextId);

                    realm.insertOrUpdate(calenderEvent);

                    Log.d(TAG, "Success saved into realm");
                }
            }
        });
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
                saveDataToFirestore();
                final Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was save", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                // saveDataIntoRealm();

                //save activity to sharedPreferences
                PrefsUtils prefsUtils = new PrefsUtils();
                prefsUtils.createSharedPreferencesFiles(ShowFoodBeforeAddedActivity.this, "activity");
                prefsUtils.saveData("fromActivity", "ShowFoodBeforeAddedActivity");
                //startActivity(new Intent(ShowFoodBeforeAddedActivity.this, FragmentNavigationActivity.class));
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