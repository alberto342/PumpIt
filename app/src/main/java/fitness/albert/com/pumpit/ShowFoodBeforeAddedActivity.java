package fitness.albert.com.pumpit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowFoodBeforeAddedActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private QuantityView quantityViewCustom;
    private Spinner spinnerServingUnit;
    private TextView tvEnergy, tvCrabs, tvProtein, tvFat, tvServingWeightGrams, tvCholesterol,
            tvSodium, tvTotalCarbohydrate, tvDietaryFiber, tvSugars, tvPotassium, tvNfP, tvEnergyScroll,
            tvCrabsScroll, tvProteinScroll, tvFatScroll, tvSaturatedFat;
    private ImageView ivFoodImg, btnSaveFood;
    private String TAG;
    private Foods foods = new Foods();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food_before_added);

        init();

        getExtras(1);

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


        //scroll
        tvEnergyScroll = findViewById(R.id.tv_nf_calories);
        tvCrabsScroll = findViewById(R.id.tv_nf_total_carbohydrate);
        tvProteinScroll = findViewById(R.id.tv_nf_protein);
        tvFatScroll = findViewById(R.id.tv_nf_total_fat);
        tvServingWeightGrams = findViewById(R.id.tv_serving_weight_grams);
        tvCholesterol = findViewById(R.id.tv_cholesterol);
        tvSodium = findViewById(R.id.tv_sodium);
        tvTotalCarbohydrate = findViewById(R.id.tv_total_carbohydrate);
        tvDietaryFiber = findViewById(R.id.tv_dietary_fiber);
        tvSugars = findViewById(R.id.tv_sugars);
        tvPotassium = findViewById(R.id.tv_potassium);
        tvNfP = findViewById(R.id.tv_nf_p);
        tvSaturatedFat = findViewById(R.id.tv_saturated_fat);
        ivFoodImg = findViewById(R.id.iv_food_img);

        quantityViewCustom = findViewById(R.id.quantityView_custom);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if(newQuantity == 0) {
            newQuantity = 1;
        }
        getExtras(newQuantity);
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
        List<String> list = new ArrayList<>();
        String servingUnit = foods.getServing_unit();
        list.add(servingUnit);
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServingUnit.setAdapter(dataAdapter);
    }

//get food info from searchFood
    private void getExtras(int quantity) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            String imgHigher = String.valueOf(bundle.get("photoHighres"));
            String imgThumb = String.valueOf(bundle.get("thumb"));

            //load img into ivFoodImg
            Picasso.get().load(imgHigher).error(R.mipmap.ic_launcher).into(ivFoodImg);

            String foodName = String.valueOf(bundle.get("food_name"));


            String energy = String.format("%.2f", bundle.getFloat("nf_calories") * quantity);
            this.tvEnergy.setText(energy + " kcal");
            this.tvEnergyScroll.setText("Energy: " + energy + " kcal");


            String crabs = String.format("%.2f", bundle.getFloat("nf_total_carbohydrate") * quantity);
            this.tvCrabs.setText(crabs + " g");
            this.tvCrabsScroll.setText("Crabs " + crabs + " g");


            String protein = String.format("%.2f", bundle.getFloat("nf_protein") * quantity);
            this.tvProtein.setText(protein + " g");
            this.tvProteinScroll.setText("Protein: " + protein + " g");


            String fat = String.format("%.2f", bundle.getFloat("nf_total_fat") * quantity);
            this.tvFat.setText(fat + " g");
            this.tvFatScroll.setText("Fat: " + fat + " g");


            String weightGrams = String.format("%.2f", bundle.getFloat("serving_weight_grams") * quantity);
            this.tvServingWeightGrams.setText("Weight grams: " + weightGrams + " g");

            String saturatedFat = String.format("%.2f", bundle.getFloat("nf_saturated_fat") * quantity);
            this.tvSaturatedFat.setText("Saturated fat: " + saturatedFat + " g");

            String cholesterol = String.format("%.2f", bundle.getFloat("nf_cholesterol") * quantity);
            this.tvCholesterol.setText("Cholesterol: " + cholesterol + " g");

            String sodium = String.format("%.2f", bundle.getFloat("nf_sodium") * quantity);
            this.tvSodium.setText("Sodium: " + sodium + " g");

            String carbohydrate = String.format("%.2f", bundle.getFloat("nf_total_carbohydrate") * quantity);
            this.tvTotalCarbohydrate.setText("Carbohydrate: " + carbohydrate + " g");

            String dietaryFiber = String.format("%.2f", bundle.getFloat("nf_dietary_fiber") * quantity);
            this.tvDietaryFiber.setText("Dietary fiber: " + dietaryFiber + " g");

            String sugars = String.format("%.2f", bundle.getFloat("nf_sugars") * quantity);
            this.tvSugars.setText("Sugars: " + sugars + " g");

            String potassium = String.format("%.2f", bundle.getFloat("nf_potassium") * quantity);
            this.tvPotassium.setText("Potassium: " + potassium + " g");

            String nf_p = String.format("%.2f", bundle.getFloat("nf_p") * quantity);
            this.tvNfP.setText("Nutrition Facts Panel: " + nf_p + " g");

            foods.setImgUrl(imgHigher);
            foods.setThumb(imgThumb);
            foods.setFood_name(foodName);
            foods.setNf_calories(Float.parseFloat(energy));
            foods.setNf_total_carbohydrate(Float.parseFloat(crabs));
            foods.setNf_protein(Float.parseFloat(protein));
            foods.setNf_total_fat(Float.parseFloat(fat));
            foods.setServing_weight_grams(Float.parseFloat(weightGrams));
            foods.setNf_saturated_fat(Float.parseFloat(saturatedFat));
            foods.setNf_cholesterol(Float.parseFloat(cholesterol));
            foods.setNf_sodium(Float.parseFloat(sodium));
            foods.setNf_total_carbohydrate(Float.parseFloat(carbohydrate));
            foods.setNf_dietary_fiber(Float.parseFloat(dietaryFiber));
            foods.setNf_sugars(Float.parseFloat(sugars));
            foods.setNf_potassium(Float.parseFloat(potassium));
            foods.setNf_p(Float.parseFloat(nf_p));
            foods.setServing_qty(String.valueOf(quantity));
            foods.setServing_unit(String.valueOf(bundle.get("serving_unit")));
        }
    }



    //  db.collection("users").document(userRegister.getEmail()).set(saveData)

    private void saveDataToFirestore() {
        try {

            UserRegister user = new UserRegister();

            //CollectionPatch -> get myEmail -> get myMeal -> get the dayDate
            FireBaseInit.getInstance(this).db.collection("nutrition").document(FireBaseInit.fireBaseInit.getEmailRegister()).collection(getMeal()).document(getTodayData()).collection("fruit").add(foods)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });


            Log.d(TAG, "Food data save successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get Meal from SharedPreferences file
    public String getMeal() {

        SharedPreferences pref = getSharedPreferences(Foods.SharedPreferencesFile, Context.MODE_PRIVATE);

        boolean breakfast = pref.getBoolean("dinner", false);
        boolean dinner = pref.getBoolean("breakfast",false);
        boolean lunch = pref.getBoolean("lunch",false);
        boolean snack = pref.getBoolean("snack",false);

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

    private String getTodayData() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    private void onAddButtonClick() {
        btnSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirestore();
                //save activity to sharedPreferences
                SavePref savePref = new SavePref();
                savePref.createSharedPreferencesFiles(ShowFoodBeforeAddedActivity.this,"activity");
                savePref.saveData("FROM_ACTIVITY", "ShowFoodBeforeAddedActivity");
                startActivity(new Intent(ShowFoodBeforeAddedActivity.this, FragmentNavigationActivity.class));
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SearchFoodsActivity.class));
        finish();
    }
}