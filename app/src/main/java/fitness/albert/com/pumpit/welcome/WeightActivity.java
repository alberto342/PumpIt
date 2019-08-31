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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.model.UserRegister;
import fitness.albert.com.pumpit.R;

public class WeightActivity extends AppCompatActivity {

    private PrefsUtils prefsUtils = new PrefsUtils();
    private NumberPicker pKg, pGram;
    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        getSupportActionBar().hide();
        init();
        prefsUtils.createSharedPreferencesFiles(this, PrefsUtils.SETTINGS_PREFERENCES_FILE);
        valueChangedListener();
        btnNextPressed();
    }


    private void valueChangedListener() {
        pKg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

                // saveKg = newVal;
            }
        });

        pGram.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                //saveGram = newVal;
            }
        });
        UserRegister user = new UserRegister();
        user.setWeight(user.customFloatNum(pKg.getValue(), pGram.getValue()));
    }

    private void init() {
        TextView textPoint = findViewById(R.id.text_point);
        textPoint.setText(".");

        pKg = findViewById(R.id.p_kg);
        pGram = findViewById(R.id.p_gram);
        next = findViewById(R.id.next_btn);

        int color = Color.WHITE;
        setNumberPickerTextColor(pKg, color);
        setNumberPickerTextColor(pGram, color);

        pKg.setMinValue(20);
        pKg.setMaxValue(300);

        pGram.setMinValue(0);
        pGram.setMaxValue(99);
    }

    @SuppressLint("LongLogTag")
    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        try {
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
        } catch (NoSuchFieldException e) {
            Log.w("setNumberPickerTextColor", e);
        } catch (IllegalAccessException e) {
            Log.w("setNumberPickerTextColor", e);
        } catch (IllegalArgumentException e) {
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

    private void btnNextPressed() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister user = new UserRegister();
                prefsUtils.saveData("weight", user.customFloatNum(pKg.getValue(), pGram.getValue()));
                startActivity(new Intent(WeightActivity.this, BodyFatActivity.class));
            }
        });
    }


}
