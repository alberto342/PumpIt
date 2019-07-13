package fitness.albert.com.pumpit.WelcomeActivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;

public class AgeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ImageView next;
    private TextView mDisplayDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        getSupportActionBar().hide();
        init();
        btnNextPressed();
    }

    private void init() {
        next = findViewById(R.id.next_preesed);
        mDisplayDate = findViewById(R.id.tv_my_age);

        ImageView btnSelectedAge = findViewById(R.id.btn_set_date);
        btnSelectedAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }


    private void btnNextPressed() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgeActivity.this, HeightActivity.class));
            }
        });
    }


    public void showDatePickerDialog() {
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
        String date = month+1 + "/" + dayOfMonth + "/" + year;
        PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(this, UserRegister.SharedPreferencesFile);
        prefsUtils.saveData("dateOfBirth", date);
        mDisplayDate.setText(getAge(year, month, dayOfMonth));

        if(!date.isEmpty()) {
            next.setVisibility(View.VISIBLE);
        }
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        @SuppressLint("UseValueOf")
        Integer ageInt = new Integer(age);
        return ageInt.toString();
    }
}
