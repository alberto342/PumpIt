package fitness.albert.com.pumpit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitness.albert.com.pumpit.fragment.FragmentNavigationActivity;

public class LoginActivity extends AppCompatActivity { //implements GoogleApiClient.OnConnectionFailedListener

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private ImageView btnLogin, btnForgetPassword, btnGoogleSign;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, FragmentNavigationActivity.class));
            finish();
        }

        findViews();
        setOnClickListener();
    }

    private void findViews() {
        inputEmail = findViewById(R.id.edit_text_email);
        inputPassword = findViewById(R.id.edit_text_password);
        btnLogin = findViewById(R.id.login_btn);
        btnForgetPassword = findViewById(R.id.btn_forgetten_password);
        btnGoogleSign = findViewById(R.id.btn_google);

        progressBar = findViewById(R.id.progress_login);

        btnLogin.setImageResource(R.mipmap.btn_login_active);
    }

    private void setOnClickListener() {
        btnLogin.setOnClickListener(onClickListener);
        btnForgetPassword.setOnClickListener(onClickListener);
        btnGoogleSign.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn:
                    loginOnClick();
                    break;

                case R.id.btn_forgetten_password:
                    resetPassword();
                    break;

                case R.id.btn_google:
                    break;
            }
        }
    };


    private boolean alert() {
        String emailString = inputEmail.getText().toString().trim();
        String passwordString = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailString)) {
            errorMessage(inputEmail, "Enter email address!");
            return false;
        } else if (!(isEmailValid(emailString))) {
            errorMessage(inputEmail, "Please use an email address from a reputable email");
            return false;
        }

        if (TextUtils.isEmpty(passwordString)) {
            errorMessage(inputPassword, "Enter password!");
            return false;
        }

        return true;
    }


    private void loginOnClick() {
        btnLogin.setImageResource(R.mipmap.emty_login_btn);
        progressBar.setVisibility(View.VISIBLE);


        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        alert();

        //authenticate user
        try {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.setVisibility(View.INVISIBLE);
                            if (!task.isSuccessful() && isEmailValid(email)) {
                                errorMessage(inputEmail, "Email not exists" + task.getException());
                                if (password.length() < 6) {
                                    errorMessage(inputPassword, "Your password is too short!");
                                }
                            } else {
                                checkIfEmailVerified();
                                nextActivity();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void errorMessage(EditText text, String message) {
        String strUserName = text.getText().toString();

        btnLogin.setImageResource(R.mipmap.btn_login);

        if (TextUtils.isEmpty(strUserName)) {
            text.setError(message);
        }
        text.setError(message);
    }

    private void nextActivity() {
        startActivity(new Intent(this, FragmentNavigationActivity.class));
        finish();
    }


    private void resetPassword() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.activity_reset_password, null);


        dialogBuilder.setView(dialogView);

        final EditText editEmail = dialogView.findViewById(R.id.email);
        final Button btnReset = dialogView.findViewById(R.id.btn_reset_password);
        final Button btnBack = dialogView.findViewById(R.id.btn_back);

        final ProgressBar progressBar1 = dialogView.findViewById(R.id.progress_login);
        //dialogBuilder.setTitle("Send Photos");
        final AlertDialog dialog = dialogBuilder.create();
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                                progressBar1.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar1.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void googleSignIn() {

    }


    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            finish();
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        } else {
            errorMessage(inputEmail, "Please Verify Your Email Address");
            sleepTimer();
            FirebaseAuth.getInstance().signOut();
        }
    }

    public void sleepTimer() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
