package fitness.albert.com.pumpit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import fitness.albert.com.pumpit.Adapter.FoodListAdapter;
import fitness.albert.com.pumpit.Adapter.SnacksListAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowSnackActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private final String TAG = "ShowSnackActivity";
    private Spinner spinnerServingUnit;
    private String spinnerSelectedItem;
    private QuantityView quantityViewCustom;
    private TextView tvEnergy, tvCarbs, tvProtein, tvFat;
    private ImageView foodItem;
    private List<String> spinnerList = new ArrayList<>();
    private Map<String, Float> allServingWeight = new HashMap<>();
    private List<Float> servingWeightList = new ArrayList<>();
    private float kcal, fat, protein, carbs, servingWeightGrams;
    private int qty;
    private boolean testOnce = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressdialog;
    private boolean isGrams = true;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_snack);
        init();
        getData();
    }

    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving);
        tvEnergy = findViewById(R.id.tv_energy_item);
        tvCarbs = findViewById(R.id.tv_carbs_item);
        tvProtein = findViewById(R.id.tv_protein_item);
        tvFat = findViewById(R.id.tv_fat_item);
        foodItem = findViewById(R.id.iv_food_item);

        setActionBar();

        quantityViewCustom = findViewById(R.id.quantity_view);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }

    private void setActionBar() {
        ActionBar mToolbar;
        mToolbar = getSupportActionBar();
        String foodName = SnacksListAdapter.foodName;
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
            qty = bundle.getInt("qty");

            quantityViewCustom.setQuantity(qty);

            String servingUnit = bundle.getString("servingUnit");

            //Get Serving Unit
            if (servingUnit == null) {
                spinnerList.add("Packet");
            } else {
                spinnerList.add(servingUnit);
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
        }
        tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal));
        tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat));
        tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein));
        tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs));
        quantityViewCustom.setQuantity(qty);

        addItemsOnSpinner();
    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if (newQuantity == 0) {
            newQuantity = 1;
            quantityViewCustom.setQuantity(newQuantity);
        }

        float altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams * quantityViewCustom.getQuantity();


        updateNutrition(altMeasures);
    }

    @Override
    public void onLimitReached() {
        Log.d(getClass().getSimpleName(), "Limit reached");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                Log.d(TAG, "Spinner Item Position: " + spinnerServingUnit.getItemAtPosition(position));

                if (allServingWeight.get(spinnerSelectedItem) == null) {
                    FoodListAdapter.measureMap.put(spinnerSelectedItem, servingWeightGrams);
                    allServingWeight.put(spinnerSelectedItem, servingWeightGrams);
                }

                float altMeasures = allServingWeight.get(spinnerSelectedItem) / servingWeightGrams * quantityViewCustom.getQuantity();


                //Check if the position is change
                if (position > 0 || testOnce) {
                    testOnce = true;

                    quantityViewCustom.setQuantity(1);
                    updateNutrition(altMeasures);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                if (spinnerSelectedItem.contains("g")) {
//                    spinnerIsGram(Foods.BREAKFAST);
//                }
            }
        });
    }


//    private void spinnerIsGram(final String keyValue) {
//        progressdialog = new ProgressDialog(this);
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();
//
//        DocumentReference doc = db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
//                .collection(keyValue).document(UserRegister.getTodayData())
//                .collection(Foods.All_NUTRITION).document(FirestoreFoodListAdapter.fireId);
//        doc.update("serving_unit", spinnerSelectedItem);
//        doc.update("serving_qty", quantityViewCustom.getQuantity());
//        doc.update("nf_calories", kcal + quantityViewCustom.getQuantity());
//        doc.update("nf_total_fat", fat + quantityViewCustom.getQuantity());
//        doc.update("nf_protein", protein + quantityViewCustom.getQuantity());
//        doc.update("nf_total_carbohydrate", carbs + quantityViewCustom.getQuantity());
//        doc.update("serving_weight_grams", allServingWeight.get(spinnerSelectedItem))
//
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressdialog.hide();
//                        Log.d(TAG, FirestoreFoodListAdapter.fireId + " DocumentSnapshot successfully updated!");
//                        tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal + quantityViewCustom.getQuantity()));
//                        tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat + quantityViewCustom.getQuantity()));
//                        tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein + quantityViewCustom.getQuantity()));
//                        tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs + quantityViewCustom.getQuantity()));
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error updating document", e);
//            }
//        });
//    }

    // TODO: 20/03/2019 check if its grams and change the info

    //Update if spinner
    private void updateNutrition(final float altMeasures) {

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(Foods.SNACK).document(UserRegister.getTodayData())
                .collection(Foods.All_NUTRITION)
                .whereEqualTo("food_name", SnacksListAdapter.foodName)
                .whereEqualTo("serving_unit", spinnerSelectedItem)
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
                                    .collection(Foods.SNACK).document(UserRegister.getTodayData())
                                    .collection(Foods.All_NUTRITION).document(docId);

                            doc.update("serving_unit", spinnerSelectedItem);
                            doc.update("serving_qty", quantityViewCustom.getQuantity());
                            doc.update("nf_calories", kcal * altMeasures);
                            doc.update("nf_total_fat", fat * altMeasures);
                            doc.update("nf_protein", protein * altMeasures);
                            doc.update("nf_total_carbohydrate", carbs * altMeasures);
                            doc.update("serving_weight_grams", allServingWeight.get(spinnerSelectedItem))

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressdialog.hide();
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            tvEnergy.setText(String.format(Locale.getDefault(), "%.0f", kcal * altMeasures));
                                            tvFat.setText(String.format(Locale.getDefault(), "%.2f", fat * altMeasures));
                                            tvProtein.setText(String.format(Locale.getDefault(), "%.2f", protein * altMeasures));
                                            tvCarbs.setText(String.format(Locale.getDefault(), "%.2f", carbs * altMeasures));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
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
    }


}
