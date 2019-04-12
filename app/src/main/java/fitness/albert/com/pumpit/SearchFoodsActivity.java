package fitness.albert.com.pumpit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fitness.albert.com.pumpit.Adapter.FoodListAdapter;
import fitness.albert.com.pumpit.Api.RestApi;
import fitness.albert.com.pumpit.Model.FoodListResponse;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.RetroRequest.FoodRequest;
import fitness.albert.com.pumpit.Utils.Global;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFoodsActivity extends AppCompatActivity {

    EditText edQuery;
    RecyclerView rvList;
    Button btnSearch;
    Button btnSaveAllFood;
    TextView tvEmpty;

    FoodListAdapter foodListAdapter;

    RestApi api;
    public static ArrayList<Foods> mListItem = new ArrayList<>();
    private Foods foods = new Foods();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "SearchFoodsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foods);
        api = Global.initRetrofit();
        findViews();
    }

    private void findViews() {
        tvEmpty = findViewById(R.id.tvEmpty);
        edQuery = findViewById(R.id.edQuery);
        rvList = findViewById(R.id.rvList);
        btnSearch = findViewById(R.id.btnSearch);
        btnSaveAllFood = findViewById(R.id.btn_save_all_food);

        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFoodList();
            }
        });

        btnSaveAllFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveAll();
            }
        });
    }


    private void getFoodList() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        FoodRequest foodRequest = null;
        final String search = edQuery.getText().toString().trim();

        if (search.length() == 0 || search.isEmpty() || search.equals("")) {
            Toast.makeText(this,"Please Enter food name",Toast.LENGTH_SHORT).show();
        } else {
            foodRequest = new FoodRequest(search);
        }

        Call<FoodListResponse> call = api.foodList(Global.x_app_id, Global.x_app_key, foodRequest);

        call.enqueue(new Callback<FoodListResponse>() {
            @Override
            public void onResponse(Call<FoodListResponse> call, Response<FoodListResponse> response) {
                progressdialog.hide();
                if (response.body() != null) {
                    if (response.body().getFoods().size() > 0) {
                        tvEmpty.setVisibility(View.GONE);


                        if(response.body().getFoods().size() > 1) {
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
            public void onFailure(Call<FoodListResponse> call, Throwable t) {
                progressdialog.hide();
                t.printStackTrace();
            }
        });
    }





    private void saveAll() {

        for(int i =0 ; i< mListItem.size(); i++) {

            try {
                arrayListIntoClass();
                db.collection(Foods.NUTRITION)
                        .document(getEmailRegister()).collection(Foods.BREAKFAST)
                        .document(getTodayDate()).collection(Foods.FRUIT).add(mListItem.get(i))

                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(SearchFoodsActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    //get Meal from SharedPreferences file
    private String getMeal() {

        SharedPreferences pref = getSharedPreferences(Foods.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean breakfast = pref.getBoolean("DINNER", false);
        boolean dinner = pref.getBoolean("BREAKFAST",false);
        boolean lunch = pref.getBoolean("LUNCH",false);
        boolean snack = pref.getBoolean("SNACK",false);

        if (breakfast) {
            return "BREAKFAST";
        }
        if (dinner) {
            return "DINNER";
        }
        if (lunch) {
            return "LUNCH";
        }
        if (snack) {
            return "SNACK";
        } else {
            return null;
        }
    }

    private void arrayListIntoClass() {

        for(int i = 0 ; i<mListItem.size(); i++) {
            foods.setFood_name(mListItem.get(i).getFood_name());
            foods.setImgUrl(mListItem.get(i).getImgUrl());
        }
    }


    private String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    public String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }


    public int getListSize() {
       return mListItem.size();
    }

}
