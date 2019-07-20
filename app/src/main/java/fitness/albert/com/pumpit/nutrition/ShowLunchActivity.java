package fitness.albert.com.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import fitness.albert.com.pumpit.adapter.BreakfastListAdapter;
import fitness.albert.com.pumpit.adapter.FoodListAdapter;
import fitness.albert.com.pumpit.adapter.LunchListAdapter;
import fitness.albert.com.pumpit.model.FireBaseInit;
import fitness.albert.com.pumpit.model.Foods;
import fitness.albert.com.pumpit.model.FullNutrients;
import fitness.albert.com.pumpit.model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowLunchActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private final String TAG = "ShowLunchActivity";
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem, oldServingUnit, docId;
    private QuantityView quantityViewCustom;
    private TextView tvEnergy, tvCarbs, tvProtein, tvFat, tvAllNutrition;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lunch);
        init();
        setActionBar();
        getData();
    }

    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving);
        tvEnergy = findViewById(R.id.tv_energy_item);
        tvCarbs = findViewById(R.id.tv_carbs_item);
        tvProtein = findViewById(R.id.tv_protein_item);
        tvFat = findViewById(R.id.tv_fat_item);
        foodItem = findViewById(R.id.iv_food_item);
        tvAllNutrition = findViewById(R.id.tv_lunch_all_foods);
        quantityViewCustom = findViewById(R.id.quantity_view);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }

    private void setActionBar() {
        ActionBar mToolbar;
        mToolbar = getSupportActionBar();
        String foodName = LunchListAdapter.foodName;
        String foodNameCapitalizeFirstLetter = foodName.substring(0, 1).toUpperCase() + foodName.substring(1);
        assert mToolbar != null;
        mToolbar.setTitle(foodNameCapitalizeFirstLetter);

        // Create a TextView programmatically.
        TextView tv = new TextView(getApplicationContext());

        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                android.app.ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                android.app.ActionBar.LayoutParams.WRAP_CONTENT);

        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);

        // Set text to display in TextView
        tv.setText(mToolbar.getTitle());

        // Set the text color of TextView
        tv.setTextColor(Color.WHITE);

        //set the text size
        tv.setTextSize(20);

        // Set TextView text alignment to center
        tv.setGravity(Gravity.CENTER);

        mToolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        //Set the newly created TextView as ActionBar custom view
        mToolbar.setCustomView(tv);
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

        addItemsOnSpinner();
    }


    //Calculation the quantity + -
    @Override
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
                .collection(Foods.BREAKFAST).document(UserRegister.getTodayData())
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
                                    .collection(Foods.BREAKFAST).document(UserRegister.getTodayData())
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
}
