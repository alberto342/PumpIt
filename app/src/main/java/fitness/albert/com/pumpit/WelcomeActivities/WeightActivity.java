package fitness.albert.com.pumpit.WelcomeActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.Model.UserRegister;

public class WeightActivity extends AppCompatActivity {

    private SharedPreferences SPSaveTheCounter;
    private SharedPreferences.Editor editor;

    private NumberPicker pKg, pGram;
    private ImageView next;
    private int color = Color.WHITE;
    private TextView textPoint;
    private int saveKg;
    private int saveGram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        getSupportActionBar().hide();

        init();

        createSharedPreferencesFiles();

        valueChangedListener();

        btnNextPressed();
    }



    private void valueChangedListener() {
        pKg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

                saveKg = newVal;
            }
        });

        pGram.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                saveGram = newVal;
            }
        });
        UserRegister user = new UserRegister();
        user.setWeight(user.customFloatNum(pKg.getValue(), pGram.getValue()));
    }

    private void init() {
        textPoint = findViewById(R.id.text_point);
        textPoint.setText(".");

        pKg = findViewById(R.id.p_kg);
        pGram = findViewById(R.id.p_gram);
        next = findViewById(R.id.next_btn);

        setNumberPickerTextColor(pKg, color);
        setNumberPickerTextColor(pGram, color);

        pKg.setMinValue(20);
        pKg.setMaxValue(300);

        pGram.setMinValue(0);
        pGram.setMaxValue(99);
    }

    @SuppressLint("LongLogTag")
    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        try{
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
        }
        catch(NoSuchFieldException e){
            Log.w("setNumberPickerTextColor", e);
        }
        catch(IllegalAccessException e){
            Log.w("setNumberPickerTextColor", e);
        }
        catch(IllegalArgumentException e){
            Log.w("setNumberPickerTextColor", e);
        }

        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(color);
        }
        numberPicker.invalidate();
    }

    private void btnNextPressed() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister user = new UserRegister();
                sharedPreferencesSaveData("weight", user.customFloatNum(pKg.getValue(), pGram.getValue()));
                startActivity(new Intent(WeightActivity.this, BodyFatActivity.class));
            }
        });
    }



    @SuppressLint("WrongConstant")
    private void createSharedPreferencesFiles() {
        SPSaveTheCounter = getSharedPreferences("userInfo",MODE_NO_LOCALIZED_COLLATORS);
    }

    private void sharedPreferencesSaveData(String key, float floatObj) {
        editor = SPSaveTheCounter.edit();
        try {
            editor.putFloat(key, floatObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}
