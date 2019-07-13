package fitness.albert.com.pumpit.fragment.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.Model.TDEECalculator;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.fragment.profile.AccountSettings.AccountFragment;
import fitness.albert.com.pumpit.fragment.profile.ProfileChange.ChangeProfileFragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String TAG = "ProfileFragment";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nameTv, bmiTv, myWeightTv, myGoalWeightTv, bmiResultTv;
    private ImageView profileImg, btnAccountSetting, btnProfile, btnMyGoals, btnDiary,
            btnWaterTracker, btnNotification, btnUnits, btnDataExport, btnHelp, btnAbout;
    private ProgressBar progressBar;
    private UserRegister userRegister = new UserRegister();
    private PrefsUtils prefsUtils = new PrefsUtils();


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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        init(view);

        if (mAuth.getCurrentUser() != null) {
            loadData();
        }
        setHasOptionsMenu(true);
        clickOnBtn();
    }

    private void init(View view) {
        nameTv = view.findViewById(R.id.txt_name);
        bmiTv = view.findViewById(R.id.txt_bmi);
        myWeightTv = view.findViewById(R.id.my_weight);
        myGoalWeightTv = view.findViewById(R.id.goal_weight);
        profileImg = view.findViewById(R.id.profile_pic);
        bmiResultTv = view.findViewById(R.id.bmiResult);
        btnAccountSetting = view.findViewById(R.id.btn_account_setting);
        btnProfile = view.findViewById(R.id.btn_profile);
        btnMyGoals = view.findViewById(R.id.btn_my_goals);
        btnDiary = view.findViewById(R.id.btn_diary);
        btnWaterTracker = view.findViewById(R.id.btn_water_tracker);
        btnNotification = view.findViewById(R.id.btn_notification);
        btnUnits = view.findViewById(R.id.btn_units);
        btnDataExport = view.findViewById(R.id.btn_data_export);
        btnHelp = view.findViewById(R.id.btn_help);
        btnAbout = view.findViewById(R.id.btn_about);
        progressBar = view.findViewById(R.id.pb_profile);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.m_setting) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            Log.d(TAG, "onOptionsItemSelected:  Menu selected");
        }
        return true;
    }


    private void clickOnBtn() {
        btnAccountSetting.setOnClickListener(onClickListener);
        btnProfile.setOnClickListener(onClickListener);
        btnMyGoals.setOnClickListener(onClickListener);
        btnDiary.setOnClickListener(onClickListener);
        btnWaterTracker.setOnClickListener(onClickListener);
        btnNotification.setOnClickListener(onClickListener);
        btnUnits.setOnClickListener(onClickListener);
        btnDataExport.setOnClickListener(onClickListener);
        btnHelp.setOnClickListener(onClickListener);
        btnAbout.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_account_setting:
                    Fragment AccountFragment = new AccountFragment();
                    loadFragment(AccountFragment);
                    break;

                case R.id.btn_profile:
                    Fragment profileFragment = new ChangeProfileFragment();
                    loadFragment(profileFragment);
                    break;

                case R.id.btn_my_goals:
                    Fragment myGoalsFragment = new ChangeMyGoalsFragment();
                    loadFragment(myGoalsFragment);
                    break;

                case R.id.btn_diary:
                    Fragment diaryFragment = new ChangeDiaryFragment();
                    loadFragment(diaryFragment);
                    break;

                case R.id.btn_water_tracker:
                    Fragment waterTrackerFragment = new WaterTrackerFragment();
                    loadFragment(waterTrackerFragment);
                    break;

                case R.id.btn_notification:
                    break;

                case R.id.btn_units:
                    Fragment unitsFragment = new UnitsFragment();
                    loadFragment(unitsFragment);
                    break;

                case R.id.btn_data_export:
                    Fragment dataExportFragment = new DataExportFragment();
                    loadFragment(dataExportFragment);
                    break;

                case R.id.btn_help:
                    break;

                case R.id.btn_about:
                    break;

            }
        }
    };

    private void loadData() {
        FireBaseInit fireBaseInit = new FireBaseInit(getActivity());
        fireBaseInit.setIntoPrefs();
        prefsUtils.createSharedPreferencesFiles(getActivity(), "user");
        if (prefsUtils.getString("firstName", " ").equals(" ")) {
            loadFromFb();
            loadFromPrefs();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            loadFromPrefs();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void loadFromFb() {
        db.collection("users").document(FireBaseInit.getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userRegister = documentSnapshot.toObject(UserRegister.class);
                        saveIntoPrefs();
                        Log.d(TAG, "TestGetUserProfile: " + documentSnapshot.getData());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Field to receive user data");

                    }
                });
    }

    private void loadFromPrefs() {
        final TDEECalculator cal = new TDEECalculator();
        String firstName = prefsUtils.getString("firstName", "");
        String lestName = prefsUtils.getString("lestName", "");
        String activityLevel = prefsUtils.getString("activityLevel", "");
        String bodyFat = prefsUtils.getString("bodyFat", "");
        String fatTarget = prefsUtils.getString("fatTarget", "");
        String programSelect = prefsUtils.getString("programSelect", "");
        float weight = prefsUtils.getFloat("weight", 0f);
        int height = prefsUtils.getInt("height", 0);
        boolean isMale = prefsUtils.getBoolean("isMale", false);

        nameTv.setText(firstName + " " + lestName);
        cal.setHeight((double) height);
        cal.setWeight((double) weight);
        cal.setBmi(cal.getHeight(), cal.getWeight());
        bmiTv.setText("BMI: " + (cal.getBmi()) + "\n" + cal.bmiTable(cal.getBmi()));
        myWeightTv.setText(("Start: " + String.format("%.2f", cal.getWeight()) + "kg"));
    }

    private void saveIntoPrefs() {
        prefsUtils.saveData("firstName", userRegister.getFirstName());
        prefsUtils.saveData("lestName", userRegister.getLestName());
        prefsUtils.saveData("activityLevel", userRegister.getActivityLevel());
        prefsUtils.saveData("bodyFat", userRegister.getBodyFat());
        prefsUtils.saveData("fatTarget", userRegister.getFatTarget());
        prefsUtils.saveData("height", userRegister.getHeight());
        prefsUtils.saveData("weight", userRegister.getWeight());
        prefsUtils.saveData("programSelect", userRegister.getMyProgram());
        prefsUtils.saveData("isMale", userRegister.isMale());
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

        // add img to the server
        if (requestCode == 1 && resultCode == RESULT_OK) {
            saveUserNameInServer(data.getData());
        }
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


    private void saveUserNameInServer(final Uri data) {
        userRegister.setImagesRefPath("/profileimage.jpeg");
    }
}
