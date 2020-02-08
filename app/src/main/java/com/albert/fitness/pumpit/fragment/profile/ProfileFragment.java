package com.albert.fitness.pumpit.fragment.profile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.albert.fitness.pumpit.helper.ImageUtils;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.io.File;

import fitness.albert.com.pumpit.R;


public class ProfileFragment extends Fragment implements ImageUtils.ImageAttachmentListener {

    private String TAG = "ProfileFragment";
    private TextView tvName, tvBmi, tvMyWeigh, tvMyGoalWeight, tvBmiResult, tvBmiType;
    private ImageView profileImg, btnAccountSetting, btnProfile, btnMyGoals, btnDiary,
            btnWaterTracker, btnNotification, btnUnits, btnDataExport, btnHelp, btnAbout, btnSettings;
    private ProgressBar progressBar;
    private UserRegister userRegister = new UserRegister();
    private PrefsUtils prefsUtils;
    private TDEECalculator cal = new TDEECalculator();
    private ImageUtils imageutils;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  FirebaseAuth mAuth = FirebaseAuth.getInstance();
        imageutils = new ImageUtils(getActivity(), this, true);
        prefsUtils = new PrefsUtils(getContext(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
        initView(view);
        setBtnSettings();
        getProfileImgIfExist();
        loadFromPrefs();
        imgProfileLoad();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    private void initView(View view) {
        btnSettings = view.findViewById(R.id.btn_profile_settings);
        tvName = view.findViewById(R.id.txt_name);
        tvBmi = view.findViewById(R.id.txt_bmi);
        tvMyWeigh = view.findViewById(R.id.my_weight);
        tvMyGoalWeight = view.findViewById(R.id.goal_weight);
        profileImg = view.findViewById(R.id.profile_pic);
        tvBmiType = view.findViewById(R.id.tv_bmi_type);
    }

    private void imgProfileLoad() {
        profileImg.setOnClickListener(view -> imageutils.imagepicker(1));
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_setting, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.m_setting) {
//            startActivity(new Intent(getActivity(), SettingsActivity.class));
//            Log.d(TAG, "onOptionsItemSelected:  Menu selected");
//        }
//        return true;
//    }

    private void setBtnSettings() {
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            Log.d(TAG, "onOptionsItemSelected:  Menu selected");
        });
    }





    private void loadFromPrefs() {
        String fullName = prefsUtils.getString("name", "");
        String programSelect = prefsUtils.getString("program_select", "");
        String dateOfBirth = prefsUtils.getString("date_of_birth", "");
        float weight = prefsUtils.getFloat("weight", 0f);
        int height = prefsUtils.getInt("height", 0);
        String goal = prefsUtils.getString("goal_weight", "");
        tvMyGoalWeight.setText(goal.isEmpty() ? "Not Set Goal" : "Goal: " + goal + "kg");

        tvName.setText(fullName);
        cal.setHeight((double) height);
        cal.setWeight((double) weight);
        cal.setBmi(cal.getHeight(), cal.getWeight());
        tvBmi.setText("BMI: " + (cal.getBmi()));

        String type = cal.bmiTable();
        int color = cal.colorType(type);
        tvBmiType.setText(cal.bmiTable());
        tvBmiType.setTextColor(color);
        tvMyWeigh.setText(("Start: " + String.format("%.2f", cal.getWeight()) + "kg"));
    }




    private void checkFat() {
        //get Fat target
        String[] fatTarget = userRegister.getFatTarget().split(" - ");
        String fatTargetPart1 = fatTarget[0];
        String fatTargetPart2 = fatTarget[1];


        //get body fat
        String[] currentBody = userRegister.getBodyFat().split(" - ");
        String currentBodyPart1 = currentBody[0];
        String currentBodyPart2 = currentBody[1];
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Fragment", "onRequestPermissionsResult: " + requestCode);
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String imgName = "profile_img.jpg";
        profileImg.setImageBitmap(file);
        String path = Environment.getExternalStorageDirectory() + File.separator + "Pumpit/ExercisePictures" + File.separator;
        imageutils.createImage(file, imgName, path, false);
        imageutils.getImage(imgName, path);

    }

    private void getProfileImgIfExist() {
        String baseDir = Environment.getExternalStorageDirectory().getParent() + "/0/Pumpit/ExercisePictures/";
        File f = new File(baseDir + File.separator + "profile_img.jpg");

        if(f.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inTempStorage = new byte[16 * 1024];
            Bitmap bitmap = BitmapFactory.decodeFile(f.toString(), options);
            profileImg.setImageBitmap(bitmap);
            Log.d(TAG, "Success get Profile Img");
        }
    }
}
