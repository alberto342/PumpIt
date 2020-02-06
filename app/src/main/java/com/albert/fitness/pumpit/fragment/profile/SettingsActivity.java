package com.albert.fitness.pumpit.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.albert.fitness.pumpit.welcome.WelcomeActivity;
import com.albert.fitness.pumpit.workout.CustomPlanActivity;
import com.google.firebase.auth.FirebaseAuth;

import fitness.albert.com.pumpit.R;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, Preference.OnPreferenceClickListener {

    private static final String TAG = "settingsActivityTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TAG));
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                () -> {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                        setTitle(R.string.title_activity_settings);
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, final Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        //   args);

        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());

        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }


    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


    public static class AccountFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.account_preferences, rootKey);
        }
    }

    public static class ChangeEmailFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.account_preferences, rootKey);
        }
    }

    public static class ProfileFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_preferences, rootKey);
        }
    }

    public static class MyGoalFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.my_goal_preferences, rootKey);
        }
    }


    public static class DiaryFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.diary_preferences, rootKey);
        }
    }

    public static class WorkoutTrackerFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.workout_tracker_preferences, rootKey);
        }
    }

    public static class WaterTrackerFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.water_tracker_preferences, rootKey);
        }
    }

    public static class NotificationSettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.notification_settings_preferences, rootKey);
        }
    }

    public static class UnitsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.units_preferences, rootKey);
        }
    }

    public static class DataExportFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.data_export_preferences, rootKey);
        }
    }

    public static class HelpFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
            setPreferencesFromResource(R.xml.help_preferences, rootKey);
        }
    }

    public static class AboutPumpItFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.help_preferences, rootKey);
        }
    }


    public static class MessagesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.messages_preferences, rootKey);


        }
    }


    public static class LogOutFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), WelcomeActivity.class));
            getActivity().finish();
        }
    }


    public static class SyncFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            startActivity(new Intent(getActivity(), CustomPlanActivity.class));
        }
    }


}
