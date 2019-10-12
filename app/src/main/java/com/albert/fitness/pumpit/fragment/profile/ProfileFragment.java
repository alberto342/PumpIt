package com.albert.fitness.pumpit.fragment.profile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.albert.fitness.pumpit.helper.ImageUtils;
import com.albert.fitness.pumpit.model.TDEECalculator;
import com.albert.fitness.pumpit.model.UserRegister;
import com.albert.fitness.pumpit.utils.PrefsUtils;

import java.io.File;

import fitness.albert.com.pumpit.R;


public class ProfileFragment extends Fragment implements ImageUtils.ImageAttachmentListener {

    private String TAG = "ProfileFragment";
    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView tvName, tvBmi, tvMyWeigh, tvMyGoalWeight, tvBmiResult, tvBmiType;
    private ImageView profileImg, btnAccountSetting, btnProfile, btnMyGoals, btnDiary,
            btnWaterTracker, btnNotification, btnUnits, btnDataExport, btnHelp, btnAbout;
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
        return inflater.inflate(R.layout.fragment_profile4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  FirebaseAuth mAuth = FirebaseAuth.getInstance();
        imageutils = new ImageUtils(getActivity(), this, true);
        prefsUtils = new PrefsUtils(getContext(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
        initView(view);
        loadFromPrefs();
        imgProfileLoad();
        setHasOptionsMenu(true);
        //clickOnBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void initView(View view) {


        tvName = view.findViewById(R.id.txt_name);
        tvBmi = view.findViewById(R.id.txt_bmi);
        tvMyWeigh = view.findViewById(R.id.my_weight);
        tvMyGoalWeight = view.findViewById(R.id.goal_weight);
        profileImg = view.findViewById(R.id.profile_pic);
        tvBmiType = view.findViewById(R.id.tv_bmi_type);

        //tvBmiResult = view.findViewById(R.id.bmi_result);
//        btnAccountSetting = view.findViewById(R.id.btn_account_setting);
//        btnProfile = view.findViewById(R.id.btn_profile);
//        btnMyGoals = view.findViewById(R.id.btn_my_goals);
//        btnDiary = view.findViewById(R.id.btn_diary);
//        btnWaterTracker = view.findViewById(R.id.btn_water_tracker);
//        btnNotification = view.findViewById(R.id.btn_notification);
//        btnUnits = view.findViewById(R.id.btn_units);
//        btnDataExport = view.findViewById(R.id.btn_data_export);
//        btnHelp = view.findViewById(R.id.btn_help);
//        btnAbout = view.findViewById(R.id.btn_about);
//        progressBar = view.findViewById(R.id.pb_profile);
    }

    private void imgProfileLoad() {
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageutils.imagepicker(1);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.m_setting) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            Log.d(TAG, "onOptionsItemSelected:  Menu selected");
        }
        return true;
    }


//    private void clickOnBtn() {
//        btnAccountSetting.setOnClickListener(onClickListener);
//        btnProfile.setOnClickListener(onClickListener);
//        btnMyGoals.setOnClickListener(onClickListener);
//        btnDiary.setOnClickListener(onClickListener);
//        btnWaterTracker.setOnClickListener(onClickListener);
//        btnNotification.setOnClickListener(onClickListener);
//        btnUnits.setOnClickListener(onClickListener);
//        btnDataExport.setOnClickListener(onClickListener);
//        btnHelp.setOnClickListener(onClickListener);
//        btnAbout.setOnClickListener(onClickListener);
//    }


//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_account_setting:
//                    Fragment AccountFragment = new  SettingsActivity.AccountFragment();
//                    loadFragment(AccountFragment);
//                    break;
//
//                case R.id.btn_profile:
//                    Fragment profileFragment = new ChangeProfileFragment();
//                    loadFragment(profileFragment);
//                    break;
//
//                case R.id.btn_my_goals:
//                    Fragment myGoalsFragment = new ChangeMyGoalsFragment();
//                    loadFragment(myGoalsFragment);
//                    break;
//
//                case R.id.btn_diary:
//                    Fragment diaryFragment = new ChangeDiaryFragment();
//                    loadFragment(diaryFragment);
//                    break;
//
//                case R.id.btn_water_tracker:
//                    Fragment waterTrackerFragment = new WaterTrackerFragment();
//                    loadFragment(waterTrackerFragment);
//                    break;
//
//                case R.id.btn_notification:
//                    break;
//
//                case R.id.btn_units:
//                    Fragment unitsFragment = new UnitsFragment();
//                    loadFragment(unitsFragment);
//                    break;
//
//                case R.id.btn_data_export:
//                    Fragment dataExportFragment = new DataExportFragment();
//                    loadFragment(dataExportFragment);
//                    break;
//
//                case R.id.btn_help:
//                    break;
//
//                case R.id.btn_about:
//                    break;
//
//            }
//        }
//    };




    private void loadFromPrefs() {
        String fullName = prefsUtils.getString("name", "");
        String programSelect = prefsUtils.getString("program_select", "");
        String dateOfBirth = prefsUtils.getString("date_of_birth", "");
        float weight = prefsUtils.getFloat("weight", 0f);
        int height = prefsUtils.getInt("height", 0);
        String goal = prefsUtils.getString("goal_weight", "");
        //boolean isMale = prefsUtils.getBoolean("is_male", false);

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


    private void saveIntoPrefs() {
        prefsUtils.saveData("first_name", userRegister.getFirstName());
        prefsUtils.saveData("lest_name", userRegister.getLestName());
        prefsUtils.saveData("activity_level", userRegister.getActivityLevel());
        prefsUtils.saveData("body_fat", userRegister.getBodyFat());
        prefsUtils.saveData("fat_target", userRegister.getFatTarget());
        prefsUtils.saveData("height", userRegister.getHeight());
        prefsUtils.saveData("weight", userRegister.getWeight());
        prefsUtils.saveData("program_select", userRegister.getMyProgram());
        prefsUtils.saveData("is_male", userRegister.isMale());
        prefsUtils.saveData("date_of_birth", userRegister.getDateOfBirth());
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

    private int convertingStringToInt(String txt) {
        int myNum = 0;
        try {
            myNum = Integer.parseInt(txt);
        } catch (NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        return myNum;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        String imgName = "profile_img.jpg";
        profileImg.setImageBitmap(file);
        String path = Environment.getExternalStorageDirectory() + File.separator + "Pumpit/ExercisePictures" + File.separator;
        imageutils.createImage(file, imgName, path, false);
        imageutils.getImage(imgName, path);
    }
}
