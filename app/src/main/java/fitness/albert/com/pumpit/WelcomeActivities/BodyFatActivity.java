package fitness.albert.com.pumpit.WelcomeActivities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.BodyFatRecyclerViewAdapter;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

public class BodyFatActivity extends AppCompatActivity {

    private static final String TAG = "BodyFatActivity";
    private ImageView next;
    private List<String> bodyFat = new ArrayList<>();
    private List<String> mImageUrls = new ArrayList<>();
    private boolean isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat);

        getSupportActionBar().hide();

        this.next = findViewById(R.id.next_btn);

        getImages();

        nextActivity();
    }



    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        loadPreferences();

        if(isMale) {
            getMaleImg();
        } else {
            getFemaleImg();
        }
        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        BodyFatRecyclerViewAdapter adapter = new BodyFatRecyclerViewAdapter(this, bodyFat, mImageUrls);
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
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_6_7.png?alt=media&token=8254ea4a-2e0e-4935-a2e7-f08c31ec5ec1");
        bodyFat.add("4 - 9%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_10_12.png?alt=media&token=ac54086d-c3ea-4ce0-aacf-233c6bc8a0eb");
        bodyFat.add("9 - 14%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_15.png?alt=media&token=25f7aa22-60f3-45fa-ada2-9a5c0ae3b322");
        bodyFat.add("14 - 19%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_20.png?alt=media&token=ceb7762a-d2fe-4cf3-bb81-9f3fdae25b65");
        bodyFat.add("19 - 24%");


        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_25.png?alt=media&token=29009b68-c19f-49fb-a634-d40c6c343bd0");
        bodyFat.add("24 - 29%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2Fmale_fat_35.png?alt=media&token=9f1ae331-3252-4a97-8dfa-013a59ed397e");
        bodyFat.add("30 - 34%");


        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2F34-39.png?alt=media&token=b5204c1b-f189-4175-a6ea-2d1ede09b721");
        bodyFat.add("34 - 39%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Fmale%2F40.png?alt=media&token=1af59ee2-9bad-4084-9163-f3354dc7006c");
        bodyFat.add("40% and more");
    }


    private void getFemaleImg() {
        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_10_12.png?alt=media&token=dd5e2657-8e12-41e2-8b3b-dd300412bab1");
        bodyFat.add("7 - 12%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_15_17.png?alt=media&token=f7812458-8391-4e23-88a0-35aefb963a1b");
        bodyFat.add("12 - 17%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_20_22.png?alt=media&token=f0602312-d1b6-4a31-88e1-2a3f2438a6c4");
        bodyFat.add("17 - 22%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_25.png?alt=media&token=23cbe207-d811-4208-90f9-df5ab7ca63c2");
        bodyFat.add("22 - 27%");


        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_30.png?alt=media&token=dee6f9d8-c8d6-45e3-8582-b6b32b17bb48");
        bodyFat.add("27 - 32%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_35.png?alt=media&token=68f92b9f-f046-4f68-940c-6c3eb462ec66");
        bodyFat.add("32 - 37%");


        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_40.png?alt=media&token=0db33212-dae9-470d-bbb8-787825bc597e");
        bodyFat.add("37 - 42%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_45.png?alt=media&token=11ebe14d-4f96-4f92-8c81-114631dc4d51");
        bodyFat.add("42 - 47%");

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/pumpit-fitenns392.appspot.com/o/BodyFat%2Ffemale%2Ffemale_fat_50.png?alt=media&token=f76769bd-5f90-4450-8497-1907af10d32a");
        bodyFat.add("47 - 52%");
    }


    private void loadPreferences() {
        SharedPreferences pref = getSharedPreferences(UserRegister.SharedPreferencesFile, Context.MODE_PRIVATE);
        this.isMale = pref.getBoolean("isMale", Boolean.parseBoolean("null"));
    }



}
