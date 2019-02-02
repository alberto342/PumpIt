package fitness.albert.com.pumpit.fragment.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fitness.albert.com.pumpit.Model.TDEECalculator;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String TAG;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nameTv, bmiTv, myWeightTv, myGoalWeightTv, bmiResultTv;
    private ImageView profileImg, btnAccountSetting;
    private UserRegister userRegister = new UserRegister();

    Toolbar toolbar;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        nextActivity();
    }

    private void init(View view) {
        nameTv = view.findViewById(R.id.txt_name);
        bmiTv = view.findViewById(R.id.txt_bmi);
        myWeightTv = view.findViewById(R.id.my_weight);
        myGoalWeightTv = view.findViewById(R.id.goal_weight);
        profileImg = view.findViewById(R.id.profile_pic);
        bmiResultTv = view.findViewById(R.id.bmiResult);
        btnAccountSetting = view.findViewById(R.id.btn_account_setting);
    }

    private String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }


    private void loadData() {
        final TDEECalculator cal = new TDEECalculator();

        db.collection("users").document(getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userRegister = documentSnapshot.toObject(UserRegister.class);
                        userRegister.setFullName(userRegister.getFirstName() + " " +  userRegister.getLestName());

                        cal.setHeight(documentSnapshot.getDouble("height"));
                        cal.setWeight(documentSnapshot.getDouble("weight"));

                        nameTv.setText(userRegister.getFullName());
                        cal.setBmi(cal.getHeight(), cal.getWeight());
                        bmiTv.setText("BMI: " + String.valueOf(cal.getBmi()) + "\n" + cal.bmiTable(cal.getBmi()));
                        myWeightTv.setText(String.valueOf("Start: " + String.format("%.2f", cal.getWeight()) + "kg"));


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
        if(requestCode==1&&resultCode==RESULT_OK) {
            saveUserNameInServer(data.getData());
        }
    }

    private int convertingStringToInt(String txt) {
        int myNum = 0;
        try {
            myNum = Integer.parseInt(txt);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        return myNum;
    }

    private void nextActivity() {
        btnAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AccountFragment();
                loadFragment(fragment);
            }
        });
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

//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.setting_toolbar, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        searchView.setQueryHint("Search");
//
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }




}
