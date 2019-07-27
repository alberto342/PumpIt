package fitness.albert.com.pumpit.register;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitness.albert.com.pumpit.LoginActivity;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

public class EmailRegisterActivity extends AppCompatActivity {

    public static String TAG = "EmailRegisterActivity";
    private EditText inputEmail, inputName, inputPassword, inputPasswordConfirm;
    private ImageView btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int height, age;
    private String programSelect, bodyFat, fatTarget, dateOfBirth;
    private Float weight;
    private Boolean isMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        init();

        mAuth = FirebaseAuth.getInstance();
        signup();
    }

    private void init() {
        progressBar = findViewById(R.id.progress_bar);
        inputEmail = findViewById(R.id.et_email);
        inputName = findViewById(R.id.et_name);
        inputPassword = findViewById(R.id.et_password);
        inputPasswordConfirm = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_next);
    }


    public void errorMessage(EditText text, String message) {
        String strUserName = text.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            text.setError(message);
        }
        text.setError(message);
    }


    public void isEmpty() {
        progressBar.setVisibility(View.GONE);

        if (inputEmail.getText().toString().isEmpty()) {
            errorMessage(inputEmail, "Enter email address");
        }

        if (inputName.getText().toString().isEmpty()) {
            errorMessage(inputName, "Enter Name");
        }

        if (inputPassword.getText().toString().isEmpty()) {
            errorMessage(inputPassword, "Enter a password");
        }

        if (inputPasswordConfirm.getText().toString().isEmpty()) {
            errorMessage(inputPasswordConfirm, "Confirm your password");
        }
    }


    private void signup() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadPreferences();

                //Save data to userRegister
                final UserRegister userRegister = new UserRegister();

                userRegister.setEmail(inputEmail.getText().toString().trim());
                userRegister.setFullName(inputName.getText().toString().trim());
                userRegister.setHeight(height);
                userRegister.setWeight(weight);
                userRegister.setDateOfBirth(dateOfBirth);
                userRegister.setCurrentBodyFat(bodyFat);
                userRegister.setTargetBodyFat(fatTarget);
                userRegister.setMyProgram(programSelect);
                userRegister.setMale(isMale);


                final String password = inputPassword.getText().toString().trim();
                final String passwordConfirm = inputPasswordConfirm.getText().toString().trim();

                isEmpty();

                if (password.length() < 6 && password.length() > 1) {
                    errorMessage(inputPassword, "Password too short, enter minimum 6 characters!");
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    errorMessage(inputPasswordConfirm, "Those password didn't match. Try again");
                    return;
                }

                if (passwordConfirm.length() < 6 && passwordConfirm.length() > 1) {
                    errorMessage(inputPasswordConfirm, "Password too short, enter minimum 6 characters!");
                    return;
                }

                if (!userRegister.splitName(userRegister.getFullName())) {
                    errorMessage(inputName, "Please Enter your first and last name.");
                    return;
                }

                if (!isEmailValid(inputEmail.getText().toString())) {
                    errorMessage(inputEmail, "Please enter a valid email address");
                    return;
                }


                //Save data to maps
                final Map<String, Object> saveData = new IdentityHashMap<>();
                saveData.put("firstName", userRegister.getFirstName());
                saveData.put("lestName", userRegister.getLestName());
                saveData.put("isMale", userRegister.isMale());
                saveData.put("height", userRegister.getHeight());
                saveData.put("weight", userRegister.getWeight());
                saveData.put("bodyFat", userRegister.getCurrentBodyFat());
                saveData.put("fatTarget", userRegister.getTargetBodyFat());
                saveData.put("programSelect", userRegister.getMyProgram());
                saveData.put("activityLevel", "setOfTheDay");
                saveData.put("dateOfBirth", userRegister.getDateOfBirth());


                progressBar.setVisibility(View.VISIBLE);
                //create user
                try {
                    mAuth.createUserWithEmailAndPassword(userRegister.getEmail(), password)
                            .addOnCompleteListener(EmailRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        errorMessage(inputEmail, "Authentication failed." + task.getException());
                                    } else {
                                        //save data to firebase
                                        db.collection("users").document(userRegister.getEmail()).set(saveData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                        //clearSharedPreferencesDataֿ();

                                        Toast.makeText(EmailRegisterActivity.this, "Registration Succeeded, Please Verify Your Email Address", Toast.LENGTH_SHORT).show();
                                        sendVerificationEmail();
                                        if (password.equals(passwordConfirm)) {
                                            //startActivity(new Intent(EmailRegisterActivity.this, FragmentNavigationActivity.class));
                                            Intent intent = new Intent(EmailRegisterActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            errorMessage(inputPasswordConfirm, "Those password didn't match. Try again");
                                        }
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void loadPreferences() {
        SharedPreferences pref = getSharedPreferences(UserRegister.SharedPreferencesFile, Context.MODE_PRIVATE);
        this.programSelect = pref.getString("program_selected", "program_selected");
        this.dateOfBirth = pref.getString("date_of_birth", null);
        this.height = pref.getInt("height", 0);
        this.weight = pref.getFloat("weight", 0);
        this.isMale = pref.getBoolean("is_male", Boolean.parseBoolean("null"));
        this.bodyFat = pref.getString("body_fat", null);
        this.fatTarget = pref.getString("fat_target", null);
    }


    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(EmailRegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
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
}
