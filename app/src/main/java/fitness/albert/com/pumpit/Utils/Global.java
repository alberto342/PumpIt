package fitness.albert.com.pumpit.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

import fitness.albert.com.pumpit.Api.RestApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Global {
    public static final String BASE_URL = "https://trackapi.nutritionix.com/v2/";
    public static final String NUTRIENTS = "natural/nutrients";

    public static final String x_app_id ="fa8678e7";
    public static final String x_app_key ="153a6d177ad9532a8c610eb59f6455a3";



    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Log.e("Network Testing", "***Available***");
            return true;
        }
        // Log.e("Network Testing", "***Not Available***");
        return false;
    }


    public static void showProgressBar(Context context) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

    }


    public static RestApi initRetrofit() {
        // For logging request & response (Optional)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(RestApi.class);
    }


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings
                        .Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings
                    .Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void setSoftKeyBoard(Activity activity, boolean mode) {

        if (activity == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService
                (activity.INPUT_METHOD_SERVICE);

        if (mode) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        } else {
            if (inputMethodManager != null) {
                if (activity == null)
                    return;
                if (activity.getCurrentFocus() == null)
                    return;
                if (activity.getCurrentFocus().getWindowToken() == null)
                    return;
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);


            }
        }
    }
}
