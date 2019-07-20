package fitness.albert.com.pumpit.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

public class HeightActivity extends AppCompatActivity {

    private PrefsUtils prefsUtils = new PrefsUtils();
    private NumberPicker pCentimeter;
    private ImageView next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        getSupportActionBar().hide();
        init();
        prefsUtils.createSharedPreferencesFiles(this, UserRegister.SharedPreferencesFile);

        //Set a value change listener for NumPicker
        valueChangedListener();
        btnNextPressed();
    }

    private void init() {
        pCentimeter = findViewById(R.id.p_centimeter);
        next = findViewById(R.id.next_btn);
        int color = Color.WHITE;
        setNumberPickerTextColor(pCentimeter, color);

        this.pCentimeter.setMinValue(100);
        this.pCentimeter.setMaxValue(300);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        this.pCentimeter.setWrapSelectorWheel(true);
    }
    

    @SuppressLint("WrongConstant")
    private void valueChangedListener() {
        pCentimeter.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
            }
        });
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
               prefsUtils.saveData("height", pCentimeter.getValue());
                startActivity(new Intent(HeightActivity.this, WeightActivity.class));
            }
        });
    }
}
