package fitness.albert.com.pumpit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.Adapter.BreakfastListAdapter;
import fitness.albert.com.pumpit.Adapter.DinnerListAdapter;
import fitness.albert.com.pumpit.Adapter.FirestoreFoodListAdapter;
import fitness.albert.com.pumpit.Adapter.LunchListAdapter;
import fitness.albert.com.pumpit.Adapter.SnacksListAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SwipeToDeleteCallback;
import fitness.albert.com.pumpit.Model.UserRegister;

public class ShowAllNutritionActivity extends AppCompatActivity {

    private RecyclerView rvListBreakfast, rvListLunch, rvListDinner, rvListSnacks;
    private List<Foods> foodListBreakfast = new ArrayList<>();
    private List<Foods> foodListLunch = new ArrayList<>();
    private List<Foods> foodListDinner = new ArrayList<>();
    private List<Foods> foodListSnacks = new ArrayList<>();
    private TextView tvTotalBreakfast, tvTotalLunch, tvTotalDinner, tvTotalSnacks;
    private float kcal, fat, protein, carbs;
    private FirestoreFoodListAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private final String TAG = "ShowAllNutritionActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String nutritionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_nutrition);

        init();
    }

    private void init() {
        rvListBreakfast = findViewById(R.id.rv_breakfast);
        rvListLunch = findViewById(R.id.rv_lunch);
        rvListDinner = findViewById(R.id.rv_dinner);
        rvListSnacks = findViewById(R.id.rv_snacks);

        tvTotalBreakfast = findViewById(R.id.tv_total_breakfast);
        tvTotalLunch = findViewById(R.id.tv_total_lunch);
        tvTotalDinner = findViewById(R.id.tv_total_dinner);
        tvTotalSnacks = findViewById(R.id.tv_total_snacks);
        constraintLayout = findViewById(R.id.coordinator_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMealFromFs(Foods.BREAKFAST, rvListBreakfast, foodListBreakfast, tvTotalBreakfast);
        getMealFromFs(Foods.LUNCH, rvListLunch, foodListLunch, tvTotalLunch);
        getMealFromFs(Foods.DINNER, rvListDinner, foodListDinner, tvTotalDinner);
        getMealFromFs(Foods.SNACK, rvListSnacks, foodListSnacks, tvTotalSnacks);

        enableSwipeToDeleteAndUndo(rvListBreakfast, foodListBreakfast, Foods.BREAKFAST);
        enableSwipeToDeleteAndUndo(rvListDinner, foodListDinner, Foods.DINNER);
        enableSwipeToDeleteAndUndo(rvListLunch, foodListLunch, Foods.LUNCH);
        enableSwipeToDeleteAndUndo(rvListSnacks, foodListSnacks, Foods.SNACK);
    }

    private void getMealFromFs(final String keyValue, final RecyclerView recyclerView, final List<Foods> foodList, final TextView totalMeal) {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get NUTRITION from firestone
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(keyValue).document(UserRegister.getTodayData())
                .collection(Foods.All_NUTRITION).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //hide ProgressDialog
                        progressdialog.hide();

                        if (task.isSuccessful() && task.getResult() != null) {

                            nutritionName = keyValue;

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);

                                initRecyclerView(recyclerView, foodList, keyValue);
                                //Disable RecyclerView scrolling
                                recyclerView.setNestedScrollingEnabled(false);

                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //set NUTRITION to float
                                kcal += foodList.get(i).getNf_calories();
                                carbs += foodList.get(i).getNf_total_carbohydrate();
                                fat += foodList.get(i).getNf_total_fat();
                                protein += foodList.get(i).getNf_protein();
                            }
                            totalMeal.setText(String.format(Locale.US, "Total: %.2f" + " Kcal.  " + "%.2f" + " Carbs.  " + "%.2f" + " Protein.  " + "%.2f" + " Fat.  ", kcal, carbs, protein, fat));

                            kcal = carbs = fat = protein = 0;

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    private void deleteFromFirebase(final String keyValue, final String foodName, final int qty) {
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(keyValue).document(UserRegister.getTodayData())
                .collection(Foods.All_NUTRITION).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);

                                assert foods != null;
                                if (foodName.equals(foods.getFood_name()) && qty == foods.getServing_qty()) {

                                    String id = task.getResult().getDocuments().get(i).getId();

                                    //delete from firebase
                                    db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                                            .collection(keyValue).document(UserRegister.getTodayData())
                                            .collection(Foods.All_NUTRITION).document(id).delete();

                                    Log.d(TAG, "DocumentSnapshot " + task.getResult().getDocuments().get(i).getId() + " successfully deleted!");
                                    return;
                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }


    @SuppressLint("LongLogTag")
    private void initRecyclerView(RecyclerView recyclerView, List<Foods> foodList, String nutrition) {

        BreakfastListAdapter breakfastAdapter;
        LunchListAdapter lunchAdapter;
        DinnerListAdapter dinnerAdapter;
        SnacksListAdapter snacksAdapter;

        Log.d(TAG, "initRecyclerView: init food recyclerView" + recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        switch (nutrition) {
            case Foods.BREAKFAST:
                breakfastAdapter = new BreakfastListAdapter(this, foodList);
                recyclerView.setAdapter(breakfastAdapter);
                break;
            case Foods.LUNCH:
                lunchAdapter = new LunchListAdapter(this, foodList);
                recyclerView.setAdapter(lunchAdapter);
                break;
            case Foods.DINNER:
                dinnerAdapter = new DinnerListAdapter(this, foodList);
                recyclerView.setAdapter(dinnerAdapter);
                break;
            case Foods.SNACK:
                snacksAdapter = new SnacksListAdapter(this, foodList);
                recyclerView.setAdapter(snacksAdapter);
        }
    }

    // Swipe to delete item
    private void enableSwipeToDeleteAndUndo(final RecyclerView recyclerView, final List<Foods> foodList, final String keyValue) {
        final SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

                mAdapter = new FirestoreFoodListAdapter(ShowAllNutritionActivity.this, foodList);

                final int position = viewHolder.getAdapterPosition();
                final Foods item = mAdapter.getData().get(position);

                deleteFromFirebase(keyValue, item.getFood_name(), item.getServing_qty());

                foodList.remove(position);
                recyclerView.removeViewAt(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, foodList.size());

                final Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    //Without this method Data will be double
    public void onPause() {
        super.onPause();
        this.foodListBreakfast.clear();
        this.foodListDinner.clear();
        this.foodListLunch.clear();
        this.foodListSnacks.clear();
    }
}
