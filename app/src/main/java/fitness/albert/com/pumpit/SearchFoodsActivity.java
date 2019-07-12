package fitness.albert.com.pumpit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import fitness.albert.com.pumpit.Adapter.CommonListAdapter;
import fitness.albert.com.pumpit.Adapter.FoodListAdapter;
import fitness.albert.com.pumpit.Api.RestApi;
import fitness.albert.com.pumpit.Model.Common;
import fitness.albert.com.pumpit.Model.CommonListResponse;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.FoodListResponse;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.RetroRequest.FoodRequest;
import fitness.albert.com.pumpit.Utils.Global;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFoodsActivity extends AppCompatActivity {

    private String TAG = "SearchFoodsActivity";
    public static ArrayList<Foods> mListItem = new ArrayList<>();
    private ArrayList<Common> commonArrayList = new ArrayList<>();
    FoodListAdapter foodListAdapter;
    CommonListAdapter commonListAdapter;
    RecyclerView rvList;
    Button btnSaveAllFood;
    TextView tvEmpty;
    RestApi api;
    private Foods foods = new Foods();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MaterialSearchBar searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_foods);

        //checkPermission();
        Objects.requireNonNull(getSupportActionBar()).hide();
        api = Global.initRetrofit();
        findViews();
    }



    @SuppressLint("WrongConstant")
    private void findViews() {
        tvEmpty = findViewById(R.id.tvEmpty);
        rvList = findViewById(R.id.rvList);
        btnSaveAllFood = findViewById(R.id.btn_save_all_food);
        searchBar = findViewById(R.id.food_search_bar);
        //   searchBar.setCardViewElevation(25);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searchBarClicked();

        btnSaveAllFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }


    private void searchBarClicked() {

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSearchConfirmed(CharSequence text) {

                if (Global.isNetworkAvailable(SearchFoodsActivity.this)) {
                    getFoodList();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        break;
                    case MaterialSearchBar.BUTTON_SPEECH:
                        openVoiceRecognizer();
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        searchBar.disableSearch();
                }
            }
        });
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getFoodList() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        FoodRequest foodRequest = null;
        final String search = searchBar.getText();

        if (search.length() == 0) {
            Toast.makeText(this, "Please Enter food name", Toast.LENGTH_SHORT).show();
        } else {
            foodRequest = new FoodRequest(search);
        }
        Call<FoodListResponse> call = api.foodList(Global.x_app_id, Global.x_app_key, foodRequest);

        call.enqueue(new Callback<FoodListResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<FoodListResponse> call, @NonNull Response<FoodListResponse> response) {
                progressdialog.hide();
                if (response.body() != null) {
                    if (response.body().getFoods().size() > 0) {
                        tvEmpty.setVisibility(View.GONE);

                        if (response.body().getFoods().size() > 1) {
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
            public void onFailure(@NonNull Call<FoodListResponse> call, @NonNull Throwable t) {
                progressdialog.hide();
                t.printStackTrace();
            }
        });
    }


    private void getCommonList() {
        FoodRequest foodRequest = null;
        final String search = searchBar.getText();

        if (search.length() == 0) {
            Toast.makeText(this, "Please Enter food name", Toast.LENGTH_SHORT).show();
        } else {
            foodRequest = new FoodRequest(search);
        }

        Call<CommonListResponse> call = api.foodListInstant(Global.x_app_id, Global.x_app_key, foodRequest);

        call.enqueue(new Callback<CommonListResponse>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(@NonNull Call<CommonListResponse> call, @NonNull Response<CommonListResponse> response) {
                if (response.body() != null) {
                    commonArrayList = response.body().getFoods();
                    Log.d(TAG, "LOLO:" + commonArrayList.get(0).getFoodName());

                    commonListAdapter = new CommonListAdapter(SearchFoodsActivity.this, commonArrayList);
                    rvList.setAdapter(commonListAdapter);
                } else {
                    Toast.makeText(SearchFoodsActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonListResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveAll() {
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, Foods.SHARED_PREFERENCES_FILE);

        boolean dinner = prefsUtils.getBoolean(Foods.DINNER, false);
        boolean breakfast = prefsUtils.getBoolean(Foods.BREAKFAST, false);
        boolean lunch = prefsUtils.getBoolean(Foods.LUNCH, false);
        // boolean snack = prefsUtils.getBoolean(Foods.SNACK,false);

        String correctNutrition;

        if (dinner) {
            correctNutrition = Foods.DINNER;
        } else if (breakfast) {
            correctNutrition = Foods.BREAKFAST;
        } else if (lunch) {
            correctNutrition = Foods.LUNCH;
        } else {
            correctNutrition = Foods.SNACK;
        }
        for (int i = 0; i < mListItem.size(); i++) {
            try {
                arrayListIntoClass();
                db.collection(Foods.NUTRITION)
                        .document(FireBaseInit.getEmailRegister()).collection(correctNutrition)
                        .document(UserRegister.getTodayData()).collection(Foods.All_NUTRITION).add(mListItem.get(i))
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


    private void openVoiceRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            a.getMessage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchBar.setText(result.get(0));
                getFoodList();
            }
        }
    }

//    private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                        Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
//                finish();
//            }
//        }
//    }

    private void arrayListIntoClass() {
        for (int i = 0; i < mListItem.size(); i++) {
            foods.setFoodName(mListItem.get(i).getFoodName());
            foods.setImgUrl(mListItem.get(i).getImgUrl());
        }
    }
}
