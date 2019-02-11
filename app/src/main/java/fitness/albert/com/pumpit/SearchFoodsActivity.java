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
    private ArrayList<Foods> mListItem = new ArrayList<>();
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
                        visibleButton();
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

    public void visibleButton() {
        final String search = edQuery.getText().toString().trim();
        if (getListSize() >= 1 || search.contains(" ")) {
            btnSaveAllFood.setVisibility(View.VISIBLE);
        }
    }



    private void saveAll() {
        try {
            db.collection("nutrition").document(getMeal()).collection(getTodayDate()).add(mListItem)
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
    private String getMeal() {

        SharedPreferences pref = getSharedPreferences(Foods.SharedPreferencesFile, Context.MODE_PRIVATE);

        boolean breakfast = pref.getBoolean("dinner", false);
        boolean dinner = pref.getBoolean("breakfast",false);
        boolean lunch = pref.getBoolean("lunch",false);
        boolean snack = pref.getBoolean("snack",false);

        if (breakfast) {
            return "breakfast";
        }
        if (dinner) {
            return "dinner";
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

    private String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }


    public int getListSize() {
       return mListItem.size();
    }

}
