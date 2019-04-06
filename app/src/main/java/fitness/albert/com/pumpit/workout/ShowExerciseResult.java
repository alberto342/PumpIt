package fitness.albert.com.pumpit.workout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.ExerciseAdapter.ExerciseAdapter;
import fitness.albert.com.pumpit.Model.Exercise;
import fitness.albert.com.pumpit.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ShowExerciseResult extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Realm realm;
    RecyclerView recyclerView;
    private List<Exercise> realmList = new ArrayList<>();
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_result);

        //SETUP REEALM
        RealmConfiguration config = new RealmConfiguration.Builder().name("gym.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        initRecyclerView();
    }


    private void initRecyclerView() {

        //SETUP RECYCLERVIEW
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        realm = Realm.getDefaultInstance();

        recyclerView(this, realmList, AddExerciseActivity.categorySelected, AddExerciseActivity.category2Selected);
    }


    public void recyclerView(Context context, List<Exercise> exerciseList, String category, String category2) {

        final String TAG = "ShowExerciseResult";
        RealmQuery<Exercise> query = realm.where(Exercise.class);

        if (category.contains("All")) {
            query.findAll();
        } else if (category2.contains("null")) {
            query.equalTo("category", category);
        } else {
            query.equalTo("category_2", category2);
        }

        RealmResults<Exercise> result = query.findAll();

        exerciseList.addAll(result);

        exerciseAdapter = new ExerciseAdapter(context, exerciseList);
        recyclerView.setAdapter(exerciseAdapter);

        Log.d(TAG, "initRecyclerView: init recyclerView" + recyclerView + "\n" +
                "IMAGE TEST: " + realmList.get(0).getImg_name());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_search_exercise, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realm.close();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        List<Exercise> newList = new ArrayList<>();

        for (Exercise name : realmList) {
            if (name.getName().toLowerCase().contains(userInput)) {
                newList.add(name);
            }
        }
        exerciseAdapter.updateList(newList);
        return true;
    }

    @Override
    public void onBackPressed() {
        AddExerciseActivity.categorySelected = "null";
        AddExerciseActivity.category2Selected = "null";
        finish();
    }


}
