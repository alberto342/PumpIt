package com.albert.fitness.pumpit.fragment.profile.account_settings;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.albert.fitness.pumpit.fragment.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String TAG = "ChangePasswordFragment";
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private String currentPassword, newPassword, confirmPassword;
    private ImageView btnDoneConfirmPassword;
    private ProgressBar progressBar;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        confirmClicked();

    }



    private void init(View view) {
        etCurrentPassword = view.findViewById(R.id.et_current_password);
        etNewPassword = view.findViewById(R.id.et_new_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        btnDoneConfirmPassword = view.findViewById(R.id.btn_done_cofirm_password);
        progressBar = view.findViewById(R.id.progress_password);
    }

    private void confirmClicked() {
        btnDoneConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCurrentPassword.getText().toString().isEmpty() && etNewPassword.getText().toString().isEmpty() && etConfirmPassword.getText().toString().isEmpty()) {
                    Fragment accountFragment = new AccountFragment();
                    loadFragment(accountFragment);
                }

                if (checkPassword()) {
                    updatePassword();
                }
            }
        });
    }


    private void updatePassword() {

        progressBar.setVisibility(View.VISIBLE);
        btnDoneConfirmPassword.setImageResource(R.mipmap.emty_btn);

        currentPassword = etCurrentPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider

                //Checks if the email and password are correct in the account
                .getCredential(getEmailRegister(), currentPassword);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            String newPass = etNewPassword.getText().toString();

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Password updated");
                                                progressBar.setVisibility(View.INVISIBLE);
                                                btnDoneConfirmPassword.setImageResource(R.mipmap.done);
                                                Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                Fragment accountFragment = new AccountFragment();
                                                loadFragment(accountFragment);
                                            } else {
                                                Log.d(TAG, "Error password not updated", task.getException());
                                                errorMessage(etCurrentPassword, "The password is invalid or the user does not have a password.");
                                                progressBarInvisible();

                                            }
                                        }
                                    });
                        }
                        Log.d(TAG, "User re-authenticated." + task.getException());
                        if (!task.isSuccessful()) {
                            errorMessage(etCurrentPassword, "The password is invalid or the user does not have a password.");
                            progressBarInvisible();
                        }


//                        if(task.getException().toString() == "The password is invalid or the user does not have a password.") {
//                            errorMessage(etCurrentPassword, "The password is invalid or the user does not have a password.");
//                            progressBarInvisible();
//                        }


                    }
                });
    }


    private String getEmailRegister() {
        String email;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
            Log.d(TAG, "GetCurrentUser Successfully");
            return email;
        } else {
            return null;
        }
    }


    public boolean checkPassword() {

        currentPassword = etCurrentPassword.getText().toString();
        newPassword = etNewPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();


        if (currentPassword.isEmpty()) {
            errorMessage(etCurrentPassword, "Enter your password");
            return false;
        }

        if (newPassword.isEmpty()) {
            errorMessage(etNewPassword, "Enter new password");
            return false;
        }


        if (confirmPassword.isEmpty()) {
            errorMessage(etConfirmPassword, "Confirm password");
            return false;
        }


        if (newPassword.length() < 6 && newPassword.length() > 1) {
            errorMessage(etNewPassword, "Password too short, enter minimum 6 characters!");
            return false;
        }


        if (confirmPassword.length() < 6 && confirmPassword.length() > 1) {
            errorMessage(etConfirmPassword, "Password too short, enter minimum 6 characters!");
            return false;
        }


        if (!newPassword.equals(confirmPassword)) {
            errorMessage(etConfirmPassword, "Those password didn't match. Try again");
            return false;
        }
        return true;
    }

    public void errorMessage(EditText text, String message) {
        String strUserName = text.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            text.setError(message);
        }
        text.setError(message);
    }


    public void progressBarInvisible() {
        progressBar.setVisibility(View.INVISIBLE);
        btnDoneConfirmPassword.setImageResource(R.mipmap.done);
    }

    private void goToAccount() {
        startActivity(new Intent(getActivity(), ProfileFragment.class));
        getActivity().getSupportFragmentManager().popBackStack();
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}