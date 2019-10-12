package com.albert.fitness.pumpit.fragment.profile.account_settings;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.albert.fitness.pumpit.model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitness.albert.com.pumpit.R;
import com.albert.fitness.pumpit.model.nutrition.Foods;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeEmailFragment extends Fragment {


    private String TAG = "ChangeEmailFragment";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText etMyEmail, etCurrentPass;
    private ImageView btnDoneConfirmEmail;
    private ProgressBar progressBar;
    private TextView emailSend, confirmEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();





    public ChangeEmailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        btnClick();
        getUserEmail();
    }


    private void init(View view) {
        etMyEmail = view.findViewById(R.id.tv_my_email2);
        btnDoneConfirmEmail = view.findViewById(R.id.btn_done_confirm_email);
        etCurrentPass = view.findViewById(R.id.et_your_password);
        progressBar = view.findViewById(R.id.progress_email);
        emailSend = view.findViewById(R.id.tv_email_send);
        confirmEmail = view.findViewById(R.id.tv_confirm_email);
    }

    private void btnClick() {
        btnDoneConfirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(etMyEmail.getText().toString().isEmpty() && etCurrentPass.getText().toString().isEmpty() || emailSend.getVisibility() == View.VISIBLE) {
                        Fragment accountFragment = new AccountFragment();
                        loadFragment(accountFragment);
                    }


                    if(checkPasswordAndEmail()) {
                        reAuthenticateUser();
                        updateEmail();
                    }
            }
        });
    }



    //set the email to the hint
    private void getUserEmail() {
        String userEmail = getEmailRegister();
        etMyEmail.setHint(userEmail);
    }


    private void reAuthenticateUser() {
// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(getEmailRegister(), etCurrentPass.getText().toString());


// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated." + task.getException());


                        progressBar.setVisibility(View.INVISIBLE);
                        btnDoneConfirmEmail.setImageResource(R.mipmap.done);


                        if(!task.isSuccessful()) {
                            errorMessage(etCurrentPass,"The password is invalid or the user does not have a password");
                        }
                    }
                });
    }



    private String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }


    private void updateEmail() {
        progressBar.setVisibility(View.VISIBLE);
        btnDoneConfirmEmail.setImageResource(R.mipmap.emty_btn);

        final DocumentReference userRef =  db.collection(UserRegister.FIRE_BASE_USERS).document(getEmailRegister());

        final DocumentReference newUserRef = db.collection(UserRegister.FIRE_BASE_USERS).document(etMyEmail.getText().toString());

        final DocumentReference oldNutrition = db.collection(Foods.NUTRITION).document(getEmailRegister());

        final DocumentReference newNutrition = db.collection(Foods.NUTRITION).document(etMyEmail.getText().toString());

        Log.d(TAG, "users -> emailRegister: " + userRef.getId() + "New emailRegister: " + etMyEmail.getText().toString());

        Log.d(TAG, "NUTRITION-> email: " + oldNutrition.getId() + "New NUTRITION: " + etMyEmail.getText().toString());


        user.updateEmail(etMyEmail.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            btnDoneConfirmEmail.setImageResource(R.mipmap.done);
                            Log.d(TAG, "User email address updated.");
                            moveFirestoreDocument(userRef, newUserRef);
                            moveFirestoreDocument(oldNutrition, newNutrition);
                            sendVerificationEmail();

                        }
                    }
                });
    }



    public boolean checkPasswordAndEmail() {

        String currentEmail = etMyEmail.getText().toString();
        String currentPass = etCurrentPass.getText().toString();

        if (currentEmail.isEmpty()) {
            errorMessage(etMyEmail, "Enter your Email");
            return false;
        }

        if (currentPass.isEmpty()) {
            errorMessage(etCurrentPass, "Enter your password");
            return false;
        }


        if (currentPass.length() < 6 && currentPass.length() > 1) {
            errorMessage(etCurrentPass, "Password too short, enter minimum 6 characters!");
            return false;
        }

        if (!isEmailValid(currentEmail)) {
            errorMessage(etMyEmail, "Please enter a valid email address");
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



    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            emailSend.setVisibility(View.VISIBLE);
                            confirmEmail.setVisibility(View.VISIBLE);

                            // after email is sent just logout the user and finish this activity
//                            FirebaseAuth.getInstance().signOut();

                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            emailSend.setText("Email not send");

                        }
                    }
                });
    }

    private void successChangeEmail() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_reset_email, null);

        dialogBuilder.setView(dialogView);

        Button btnBack = dialogView.findViewById(R.id.btn_reset_confirm_email);
        final AlertDialog dialog = dialogBuilder.create();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment accountFragment = new AccountFragment();
                loadFragment(accountFragment);
                dialog.dismiss();
            }
        });
        dialog.show();
    }






    //need to change on firebase the email to the new email address
    public void moveFirestoreDocument(final DocumentReference fromPath, final DocumentReference toPath) {
        fromPath.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        toPath.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        fromPath.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }



    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }




}
