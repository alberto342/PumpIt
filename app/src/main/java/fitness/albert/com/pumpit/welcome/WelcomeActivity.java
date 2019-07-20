package fitness.albert.com.pumpit.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.model.Exercise;
import fitness.albert.com.pumpit.model.PrefsUtils;
import fitness.albert.com.pumpit.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class WelcomeActivity extends AppCompatActivity {

    private Realm realm;
    private PrefsUtils prefsUtils = new PrefsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        getSupportActionBar().hide();

        Realm.init(this);


        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name("gym.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        prefsUtils.createSharedPreferencesFiles(this, "realmExisting");

        boolean getPref = prefsUtils.getBoolean("realm", false);

        if (!getPref) {
            readTxtFile();
        } else {
            startActivity(new Intent(this, GoalActivity.class));
            finish();
        }
    }

    private void readTxtFile() {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        final String TAG = "ShowExerciseResult";
        boolean realmExisting;

        List<String> imgNameList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();
        List<String> category2List = new ArrayList<>();
        List<String> exerciseList = new ArrayList<>();

        // from txtFile to list
        getTxtIntoList("img_name.txt", imgNameList);
        getTxtIntoList("category.txt", categoryList);
        getTxtIntoList("exercise.txt", exerciseList);
        getTxtIntoList("category2.txt", category2List);

        realm = Realm.getDefaultInstance();  // opens "gym.realm"


        //from list into realm
        for (int i = 0; i < exerciseList.size(); i++) {
            if (exerciseList.get(i) != null) {
                addExersise(i + 1, exerciseList.get(i), categoryList.get(i), category2List.get(i), imgNameList.get(i));
            }
        }

        try {
            RealmResults<Exercise> result = realm.where(Exercise.class).findAll();
            Log.d(TAG, "Data save successfully into: " + realm.getPath() + " size: " + result.size());
            //Check If Realm have save successfully all data
            realmExisting = true;
            prefsUtils.saveData("realm", realmExisting);
            startActivity(new Intent(this, GoalActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressdialog.hide();
    }


    private void getTxtIntoList(String fileName, List<String> listReceiveTxtFile) {
        try {
            InputStream inputreader = getAssets().open(fileName);
            BufferedReader buffType = new BufferedReader(new InputStreamReader(inputreader));

            boolean hasNextLine = true;
            while (hasNextLine) {

                String getRead = buffType.readLine();

                listReceiveTxtFile.add(getRead);
                hasNextLine = getRead != null;
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    private void addExersise(final int id, final String exerciseName, final String muscleGroup, final String specificMuscle, final String imgName) {

        try {
            realm.beginTransaction();

            Exercise name = realm.createObject(Exercise.class);

            name.setId(id);
            name.setName(exerciseName);
            name.setCategory(muscleGroup);
            name.setCategory_2(specificMuscle);
            name.setImg_name(imgName);

            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        realm.close();
    }
}
