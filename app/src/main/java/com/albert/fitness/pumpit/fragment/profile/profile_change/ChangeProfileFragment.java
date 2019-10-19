package com.albert.fitness.pumpit.fragment.profile.profile_change;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.albert.fitness.pumpit.model.UserRegister;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.albert.fitness.pumpit.utils.PrefsUtils;

import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeProfileFragment extends Fragment {

    private TextView tvFirstName, tvLastName, tvGender, tvAge, tvHeight;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
   private UserRegister user = new UserRegister();
    private String TAG = "ChangeProfileFragment";


    public ChangeProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        getUserData();
    }




    private void init(View view) {
        tvFirstName = view.findViewById(R.id.tv_p_first_name);
        tvLastName = view.findViewById(R.id.tv_p_last_name);
        tvGender = view.findViewById(R.id.tv_p_gender);
        tvAge = view.findViewById(R.id.tv_p_age);
        tvHeight = view.findViewById(R.id.tv_p_height);


        tvFirstName.setOnClickListener(onClickListener);
        tvLastName.setOnClickListener(onClickListener);
        tvGender.setOnClickListener(onClickListener);
        tvAge.setOnClickListener(onClickListener);
        tvHeight.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.tv_p_first_name:
                    break;

                case R.id.tv_p_last_name:
                    break;

                case R.id.tv_p_gender:
                    break;


                case R.id.tv_p_age:
                    break;

                case R.id.tv_p_height:


            }
        }
    };


    private void getUserData() {
        final ProgressDialog progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(getActivity(), PrefsUtils.SETTINGS_PREFERENCES_FILE);
        final String dayOfBirth = prefsUtils.getString("date_of_birth", "");

        db.collection(UserRegister.FIRE_BASE_USERS).document(getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserRegister.class);

                        tvFirstName.setText(user.getFirstName());
                        tvLastName.setText(user.getLestName());
                        tvAge.setText( String.valueOf(user.date(dayOfBirth)));

                        String male = (user.isMale()) ? "female" : "male";
                        tvGender.setText(male);
                        tvHeight.setText(String.valueOf(user.getHeight()));

                        Log.d(TAG, "Success get data" + documentSnapshot.getData());
                        progressdialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    public String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }


}