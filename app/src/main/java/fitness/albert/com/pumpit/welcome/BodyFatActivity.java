package fitness.albert.com.pumpit.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.adapter.BodyFatRecyclerViewAdapter;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

public class BodyFatActivity extends AppCompatActivity {

    private static final String TAG = "BodyFatActivity";
    private ImageView next;
    private List<String> bodyFat = new ArrayList<>();
    private List<Integer> mImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat);

        getSupportActionBar().hide();

        this.next = findViewById(R.id.next_btn);

        getImages();

        nextActivity();
    }


    private void getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, UserRegister.SharedPreferencesFile);

        boolean isMale = prefsUtils.getBoolean("is_male", Boolean.parseBoolean("null"));

        if (isMale) {
            getMaleImg();
        } else {
            getFemaleImg();
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        BodyFatRecyclerViewAdapter adapter = new BodyFatRecyclerViewAdapter(this, bodyFat, mImage);
        recyclerView.setAdapter(adapter);
    }

    private void nextActivity() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BodyFatActivity.this, FatTargetActivity.class));
            }
        });
    }

    private void getMaleImg() {
        mImage.add(R.drawable.male_fat_6_7);
        mImage.add(R.drawable.male_fat_10_12);
        mImage.add(R.drawable.male_fat_15);
        mImage.add(R.drawable.male_fat_20);
        mImage.add(R.drawable.male_fat_25);
        mImage.add(R.drawable.male_fat_35);
        mImage.add(R.drawable.male_fat_34_39);
        mImage.add(R.drawable.male_fat_40);
        bodyFat.add("4 - 9%");
        bodyFat.add("9 - 14%");
        bodyFat.add("14 - 19%");
        bodyFat.add("19 - 24%");
        bodyFat.add("24 - 29%");
        bodyFat.add("30 - 34%");
        bodyFat.add("34 - 39%");
        bodyFat.add("40% and more");
    }


    private void getFemaleImg() {
        mImage.add(R.drawable.female_fat_10_12);
        mImage.add(R.drawable.female_fat_15_17);
        mImage.add(R.drawable.female_fat_20_22);
        mImage.add(R.drawable.female_fat_25);
        mImage.add(R.drawable.female_fat_30);
        mImage.add(R.drawable.female_fat_35);
        mImage.add(R.drawable.female_fat_40);
        mImage.add(R.drawable.female_fat_45);
        mImage.add(R.drawable.female_fat_50);
        bodyFat.add("7 - 12%");
        bodyFat.add("12 - 17%");
        bodyFat.add("17 - 22%");
        bodyFat.add("22 - 27%");
        bodyFat.add("27 - 32%");
        bodyFat.add("32 - 37%");
        bodyFat.add("37 - 42%");
        bodyFat.add("42 - 47%");
        bodyFat.add("47 - 52%");
    }


}
