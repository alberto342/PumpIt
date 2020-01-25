package com.albert.fitness.pumpit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils {

    public static final String DEFAULT_PLAN = "default_plan";
    public static final String EXERCISE = "exercise";
    public static final String DEFAULT_EXERCISE = "defaultExercise";
    public static final String START_WORKOUT = "start_workout";
    public static final String SETTINGS_PREFERENCES_FILE = "fitness.albert.com.pumpit_preferences";
    public static final String ACTIVITY_LEVEL = "ACTIVITY_LEVEL";
    public static final String HAVE_WORKOUT_EVENT = "have_workout_event";
    public static final String HAVE_NUTRITION_EVENT = "have_nutrition_event";
    public static final String DONE_EXERCISE = "done_exercise";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;


    public PrefsUtils() {
    }

    public PrefsUtils(Context context, String fileName) {
        this.sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void createSharedPreferencesFiles(Context context, String fileName) {
        // this.context = context;
        this.sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    public void saveData(String key, String stringObj) {
        editor = sharedPref.edit();
        try {
            editor.putString(key, stringObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }


    public void saveData(String key, boolean booleanObj) {
        editor = sharedPref.edit();
        try {
            editor.putBoolean(key, booleanObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }


    public void saveData(String key, float floatObj) {
        editor = sharedPref.edit();
        try {
            editor.putFloat(key, floatObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public void saveData(String key, int intObj) {
        editor = sharedPref.edit();
        try {
            editor.putInt(key, intObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }


    public boolean getBoolean(String keyValue, boolean defValue) {
        return sharedPref.getBoolean(keyValue, defValue);
    }

    public String getString(String keyValue, String defValue) {
        return sharedPref.getString(keyValue, defValue);
    }

    public Float getFloat(String keyValue, Float defValue) {
        return sharedPref.getFloat(keyValue, defValue);
    }

    public int getInt(String keyValue, int defValue) {
        return sharedPref.getInt(keyValue, defValue);
    }


    @SuppressLint("ApplySharedPref")
    public void removeAll(Context context, String fileName) {
        SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


    @SuppressLint("ApplySharedPref")
    public void removeSingle(Context context, String fileName, String keyName) {
        SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        settings.edit().remove(keyName).commit();
    }
}
