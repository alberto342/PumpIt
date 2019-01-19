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

import java.lang.reflect.Field;

import fitness.albert.com.pumpit.R;

public class AgeActivity extends AppCompatActivity {

    private NumberPicker np;
    private ImageView next;

    private SharedPreferences SPSaveTheCounter;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);

        getSupportActionBar().hide();

        init();

        createSharedPreferencesFiles();

        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                sharedPreferencesSaveData("age", newVal);

            }
        });

        btnNextPressed();
    }

    private void init() {
        np = findViewById(R.id.np);
        next = findViewById(R.id.next_preesed);

        setNumberPickerTextColor(np, Color.WHITE);

        //Populate NumPicker values from minimum and maximum value range
        //Set the minimum value of NumPicker
        np.setMinValue(14);
        //Specify the maximum value/number of NumPicker
        np.setMaxValue(75);
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
                startActivity(new Intent(AgeActivity.this, HeightActivity.class));
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void createSharedPreferencesFiles() {
        SPSaveTheCounter = getSharedPreferences("userInfo",MODE_NO_LOCALIZED_COLLATORS);
    }

    private void sharedPreferencesSaveData(String key, int intObj) {
        editor = SPSaveTheCounter.edit();
        try {
            editor.putInt(key, intObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}
