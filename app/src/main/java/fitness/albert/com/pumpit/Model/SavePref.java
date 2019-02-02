package fitness.albert.com.pumpit.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private Context context;

    public void createSharedPreferencesFiles(Context context, String fileName) {
        this.context = context;
        sharedPref = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
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


    public boolean getBoolean(String keyValue, boolean defValue ) {
        return sharedPref.getBoolean(keyValue, defValue);
    }

    public String getString(String keyValue, String defValue ) {
        return sharedPref.getString(keyValue, defValue);
    }

    public Float getFloat(String keyValue, Float defValue ) {
        return sharedPref.getFloat(keyValue, defValue);
    }

    public int getInt(String keyValue, int defValue ) {
        return sharedPref.getInt(keyValue, defValue);
    }




    public void removeAll(Context context, String fileName) {
        SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


    public void removeSingle(Context context, String fileName, String keyName) {
        SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        settings.edit().remove(keyName).commit();
    }





}
