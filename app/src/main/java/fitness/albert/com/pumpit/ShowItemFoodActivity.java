package fitness.albert.com.pumpit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.UserRegister;
import me.himanshusoni.quantityview.QuantityView;

public class ShowItemFoodActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener {

    private Spinner spinnerServingUnit;
    private QuantityView quantityViewCustom;
    private TextView tvEnergy, tvCarbs, tvProtein, tvFat;
    private ImageView foodItem;
    private UserRegister user = new UserRegister();
    private List<Foods> foodList = new ArrayList<>();
    private float kcal, fat, protein, carbs;
    private String  qty;
    private SavePref savePref = new SavePref();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_food);
        init();

        addItemsOnSpinner();
        getDataFromFirbase();
    }


    private void init() {
        spinnerServingUnit = findViewById(R.id.spinner_serving);
        tvEnergy = findViewById(R.id.tv_energy_item);
        tvCarbs = findViewById(R.id.tv_carbs_item);
        tvProtein = findViewById(R.id.tv_protein_item);
        tvFat = findViewById(R.id.tv_fat_item);
        foodItem = findViewById(R.id.iv_food_item);

        quantityViewCustom = findViewById(R.id.quantity_view);
        quantityViewCustom.setOnQuantityChangeListener(this);
    }



    private void getDataFromFirbase() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();


        FireBaseInit.getInstance(this).db.collection(Foods.nutrition)
                .document(FireBaseInit.fireBaseInit.getEmailRegister()).collection(Foods.breakfast)
                .document(user.getTodayData()).collection(Foods.fruit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressdialog.hide();
                        if (task.isSuccessful()) {
                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);

                                if(getFoodName().equals(foodList.get(i).getFood_name())) {

                                    Picasso.get()
                                            .load(foodList.get(i).getImgUrl())
                                            .placeholder(R.mipmap.ic_launcher)
                                            .error(R.mipmap.ic_launcher)
                                            .into(foodItem);


                                    //Save to String
                                    kcal = foodList.get(i).getNf_calories();
                                    fat = foodList.get(i).getNf_total_fat();
                                    protein = foodList.get(i).getNf_protein();
                                    carbs = foodList.get(i).getNf_total_carbohydrate();
                                    qty = foodList.get(i).getServing_qty();
                                }
                            }

                            tvEnergy.setText(String.format("%.2f", kcal));
                            tvFat.setText(String.format("%.2f", fat));
                            tvProtein.setText(String.format("%.2f", protein));
                            tvCarbs.setText(String.format("%.2f", carbs));
                            quantityViewCustom.setQuantity(Integer.parseInt(qty));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }


    //Calculation the quantity + -
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        if(newQuantity == 0) {
            newQuantity = 1;
        }
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
//        String servingUnit = foods.getServing_unit();
        list.add("list");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServingUnit.setAdapter(dataAdapter);
    }




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


    private String getFoodName() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            return String.valueOf(bundle.get("foodName"));
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        savePref.removeAll(this,"passMeal");
        finish();
        super.onBackPressed();
    }
}
