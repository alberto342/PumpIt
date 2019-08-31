package fitness.albert.com.pumpit.nutrition;

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
import java.util.Objects;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.adapter.FoodListAdapter;
import fitness.albert.com.pumpit.adapter.SnacksListAdapter;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.model.FullNutrients;
import fitness.albert.com.pumpit.model.Tags;
import fitness.albert.com.pumpit.model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowSnackActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private final String TAG = "ShowSnackActivity";
    private Spinner spinnerServingUnit;
    private QuantityView quantityViewCustom;
    private TextView tvEnergy, tvCarbs, tvProtein, tvFat, tvAllNutrition, tvFoodName, tvGroupTag;
    private ImageView foodItem;
    private List<String> spinnerList = new ArrayList<>();
    private Map<String, Float> allServingWeight = new HashMap<>();
    private List<Float> servingWeightList = new ArrayList<>();
    private List<Integer> atterId = new ArrayList<>();
    private List<Float> values = new ArrayList<>();
    private float kcal, fat, protein, carbs, servingWeightGrams, altMeasures;
    private int firstQty;
    private boolean testOnce = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressdialog;
    private String docId, oldServingUnit, spinnerSelectedItem;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_snack);
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
        progressBar = findViewById(R.id.pb_show_food);
        tvFoodName = findViewById(R.id.tv_show_food_name);
        tvGroupTag = findViewById(R.id.tv_show_group_tag);

        quantityViewCustom = findViewById(R.id.quantity_view);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }

    // Initializing collapsing toolbar
    // Will show and hide the toolbar title on scroll
    private void initCollapsingToolbar() {

        final String foodName = SnacksListAdapter.foodName;

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
        List<String> allFoods = new ArrayList<>();

        if (bundle != null) {

            Picasso.get()
                    .load(bundle.getString("foodPhoto"))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(foodItem);

            kcal = bundle.getFloat("kcal");
            fat = bundle.getFloat("fat");
            protein = bundle.getFloat("protein");
            carbs = bundle.getFloat("carbohydrate");
            servingWeightGrams = bundle.getFloat("servingWeightGrams");
            int qty = bundle.getInt("qty");
            firstQty = bundle.getInt("qty");
            oldServingUnit = bundle.getString("servingUnit");
            int foodGroup = bundle.getInt("foodGroup");
            tvGroupTag.setText(Tags.foodGroup(foodGroup));

            //Get Serving Unit
            if (oldServingUnit == null) {
                spinnerList.add("Packet");
            } else {
                spinnerList.add(oldServingUnit);
            }

            //add value into list
            for (int r = 0; r < bundle.getInt("altMeasuresSize"); r++) {
                spinnerList.add(bundle.getString("measure" + r));
                servingWeightList.add(Float.valueOf(Objects.requireNonNull(bundle.getString("measureServingWeight" + r))));

                //add spinner to the map
                if (servingWeightList.isEmpty()) {
                    allServingWeight.put("package", servingWeightGrams);
                } else {
                    allServingWeight.put(Objects.requireNonNull(bundle.getString("measure" + r)),
                            Float.valueOf(Objects.requireNonNull(bundle.getString("measureServingWeight" + r))));
                }
            }

            for (int r = 0; r < bundle.getInt("fullNutrientsSize"); r++) {
                atterId.add(bundle.getInt("AttrId" + r));
                values.add(bundle.getFloat("values" + r));
                FullNutrients fullNutrients = new FullNutrients(bundle.getInt("AttrId" + r), bundle.getFloat("values" + r));
                allFoods.add(fullNutrients.getNutrients(bundle.getInt("AttrId" + r)));
            }

            //DELEDED IF NOT NEED
            changeDataView(1);
            quantityViewCustom.setQuantity(qty);
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
                .collection(Foods.SNACK).document(UserRegister.getTodayDate())
                .collection(Foods.All_NUTRITION)
                .whereEqualTo("foodName", SnacksListAdapter.foodName)
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
                                    .collection(Foods.SNACK).document(UserRegister.getTodayDate())
                                    .collection(Foods.All_NUTRITION).document(docId);

                            int qty = quantityViewCustom.getQuantity();
                            Log.d(TAG, "onComplete: " + tvAllNutrition.getText().toString());

                            for (int i = 0; i < values.size(); i++) {
                                @SuppressLint("UseSparseArrays")
                                Map<Integer, Float> fullNutrients = new HashMap<>();

                                fullNutrients.put(atterId.get(i), values.get(i));
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
