package com.albert.fitness.pumpit.welcome;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.albert.fitness.pumpit.fragment.FragmentNavigationActivity;
import com.nightonke.jellytogglebutton.JellyToggleButton;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fitness.albert.com.pumpit.R;

import com.albert.fitness.pumpit.model.PrefsUtils;

@SuppressLint("Registered")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static String TAG = "RegisterActivity";
    private EditText inputEmail, inputName;
    private ProgressBar progressBar;
    private Boolean isMale;
    private Button btnSignUp, btnDateOfBirth, btnHeight, btnWeight;
    private JellyToggleButton switchIsMale;
    private TextView tvDate, tvHeight, tvWeight;
    private PrefsUtils prefsUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();
        prefsUtils = new PrefsUtils(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        init();
        setSwitchIsMale();
        btnDateOfBirth.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnHeight.setOnClickListener(this);
        btnWeight.setOnClickListener(this);
    }

    private void init() {
        progressBar = findViewById(R.id.pb_register);
        inputEmail = findViewById(R.id.et_email);
        inputName = findViewById(R.id.et_name);
        btnSignUp = findViewById(R.id.btn_sign_up);
        switchIsMale = findViewById(R.id.switch_is_male);
        btnDateOfBirth = findViewById(R.id.btn_set_date_of_birth);
        btnHeight = findViewById(R.id.btn_set_height);
        btnWeight = findViewById(R.id.btn_set_weight);
        tvWeight = findViewById(R.id.tv_weight);
        tvDate = findViewById(R.id.tv_date_of_birth);
        tvHeight = findViewById(R.id.tv_height);
    }


    public void errorMessage(EditText text, String message) {
        String strUserName = text.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            text.setError(message);
        }
        text.setError(message);
    }


    public boolean isEmpty() {
        progressBar.setVisibility(View.GONE);

        if (inputEmail.getText().toString().isEmpty()) {
            errorMessage(inputEmail, "Enter email address");
            return false;
        }

        if (inputName.getText().toString().isEmpty()) {
            errorMessage(inputName, "Enter Name");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                setBtnSignUp();
                break;
            case R.id.btn_set_date_of_birth:
                showDatePickerDialog();
                break;
            case R.id.btn_set_height:
                setHeightDialog();
                break;
            case R.id.btn_set_weight:
                setWeightDialog();
        }
    }

    private void setSwitchIsMale() {
        switchIsMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isMale = b;
            }
        });
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + 1 + "/" + dayOfMonth + "/" + year;
        prefsUtils.saveData("date_of_birth", date);
        tvDate.setText(date);
    }


    private void setHeightDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.layout_height, null);
        dialogBuilder.setView(dialogView);

        final NumberPicker pCentimeter = dialogView.findViewById(R.id.p_centimeter);
        final Button btnOk = dialogView.findViewById(R.id.next_ok);
        final ImageView btnClose = dialogView.findViewById(R.id.iv_close);

        int color = Color.WHITE;
        setNumberPickerTextColor(pCentimeter, color);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        pCentimeter.setMinValue(100);
        pCentimeter.setMaxValue(300);
        pCentimeter.setWrapSelectorWheel(true);

        pCentimeter.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
            }
        });

        final AlertDialog dialog = dialogBuilder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tvHeight.setText(pCentimeter.getValue() + " cm");
                prefsUtils.saveData("height", pCentimeter.getValue());
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setWeightDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.layout_weight, null);
        dialogBuilder.setView(dialogView);

        final NumberPicker pKg = dialogView.findViewById(R.id.p_kg);
        final NumberPicker pGram = dialogView.findViewById(R.id.p_gram);
        final ImageView btnClose = dialogView.findViewById(R.id.iv_close_weight);
        final Button btnOk = dialogView.findViewById(R.id.ok_weight);

        int color = Color.WHITE;
        setNumberPickerTextColor(pKg, color);
        setNumberPickerTextColor(pGram, color);

        pKg.setMinValue(20);
        pKg.setMaxValue(300);
        pGram.setMinValue(0);
        pGram.setMaxValue(99);

        pKg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
            }
        });

        pGram.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
            }
        });
        final AlertDialog dialog = dialogBuilder.create();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tvWeight.setText(pKg.getValue() + "." + pGram.getValue() + " kg");
                prefsUtils.saveData("weight", pKg.getValue() + "." + pGram.getValue());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            Log.w("setNumberPickerTextColor", e);
        }

        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText)
                ((EditText) child).setTextColor(color);
        }
        numberPicker.invalidate();
    }

    private void setBtnSignUp() {
       progressBar.setVisibility(View.VISIBLE);
       btnSignUp.setText("");
        if (!isEmailValid(inputEmail.getText().toString())) {
            errorMessage(inputEmail, "Please enter a valid email address");
            progressBar.setVisibility(View.GONE);
            btnSignUp.setText(R.string.sign_up);
            return;
        }

        //create user
        if(isEmpty()) {
            saveLastIntoPrefs();
            nextActivity();
        } else {
            progressBar.setVisibility(View.GONE);
            btnSignUp.setText(R.string.sign_up);
        }

    }

    private void saveLastIntoPrefs() {
        prefsUtils.saveData("is_male", isMale);
        prefsUtils.saveData("name", inputName.getText().toString());
        prefsUtils.saveData("email", inputEmail.getText().toString());
    }

    private void nextActivity() {
        startActivity(new Intent(this, FragmentNavigationActivity.class));
        finish();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
