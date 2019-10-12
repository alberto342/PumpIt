package com.albert.fitness.pumpit.nutrition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.albert.fitness.pumpit.adapter.BreakfastListAdapter;
import com.albert.fitness.pumpit.utils.FireBaseInit;
import com.albert.fitness.pumpit.model.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import fitness.albert.com.pumpit.R;

import com.albert.fitness.pumpit.adapter.DinnerListAdapter;
import com.albert.fitness.pumpit.adapter.FirestoreFoodListAdapter;
import com.albert.fitness.pumpit.adapter.LunchListAdapter;
import com.albert.fitness.pumpit.adapter.SnacksListAdapter;
import com.albert.fitness.pumpit.model.nutrition.Foods;
import com.albert.fitness.pumpit.utils.SwipeToDeleteCallback;

public class ShowAllNutritionActivity extends AppCompatActivity {

    private final String TAG = "ShowAllNutritionActivity";
    private RecyclerView rvListBreakfast, rvListLunch, rvListDinner, rvListSnacks;
    private List<Foods> foodListBreakfast = new ArrayList<>();
    private List<Foods> foodListLunch = new ArrayList<>();
    private List<Foods> foodListDinner = new ArrayList<>();
    private List<Foods> foodListSnacks = new ArrayList<>();
    private TextView tvTotalBreakfast, tvTotalLunch, tvTotalDinner, tvTotalSnacks;
    private float kcal, fat, protein, carbs;
    private FirestoreFoodListAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout breakfastContainer, lunchContainer, dinnerContainer, snacksContainer;
    public static String nutritionName;
    private RoundCornerProgressBar progressCarbs, progressProtien, progressFat;
    private UserRegister user = new UserRegister();
    private TextView tvCarbs, tvProtien, tvFat;
    private int calculationGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_nutrition);
        setTitle("Nutrition");

        init();
        datePicker();
        getUserDataAndSetGoal();

        getMealFromFs(Foods.BREAKFAST, rvListBreakfast, foodListBreakfast, tvTotalBreakfast, UserRegister.getTodayDate());
        getMealFromFs(Foods.LUNCH, rvListLunch, foodListLunch, tvTotalLunch, UserRegister.getTodayDate());
        getMealFromFs(Foods.DINNER, rvListDinner, foodListDinner, tvTotalDinner, UserRegister.getTodayDate());
        getMealFromFs(Foods.SNACK, rvListSnacks, foodListSnacks, tvTotalSnacks, UserRegister.getTodayDate());

        enableSwipeToDeleteAndUndo(rvListBreakfast, foodListBreakfast, Foods.BREAKFAST);
        enableSwipeToDeleteAndUndo(rvListDinner, foodListDinner, Foods.DINNER);
        enableSwipeToDeleteAndUndo(rvListLunch, foodListLunch, Foods.LUNCH);
        enableSwipeToDeleteAndUndo(rvListSnacks, foodListSnacks, Foods.SNACK);
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
        tvCarbs = findViewById(R.id.tv_carbs_all_nutrition);
        tvProtien = findViewById(R.id.tv_protein_all_nutrition);
        tvFat = findViewById(R.id.tv_fat_all_nutrition);
        progressCarbs = findViewById(R.id.pb_carbs);
        progressProtien = findViewById(R.id.pb_protein);
        progressFat = findViewById(R.id.pb_fat);
        breakfastContainer = findViewById(R.id.breakfast_container);
        lunchContainer = findViewById(R.id.lunch_container);
        dinnerContainer = findViewById(R.id.dinner_container);
        snacksContainer = findViewById(R.id.snacks_container);
    }


    private void datePicker() {
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar calendarView = new HorizontalCalendar.Builder(this, R.id.calendar_view_custom)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        calendarView.setCalendarListener(new HorizontalCalendarListener() {
            @SuppressLint({"LongLogTag", "WrongConstant"})
            @Override
            public void onDateSelected(Calendar date, int position) {

                restData();

                ProgressDialog progressdialog = new ProgressDialog(ShowAllNutritionActivity.this);
                progressdialog.setMessage("Please Wait....");
                progressdialog.show();

                String newDate;
                int day = date.get(Calendar.DAY_OF_MONTH);
                int year = date.get(Calendar.YEAR);

                if (day < 10) {
                    newDate = "0" + day + "-" + monthAdded(date.get(Calendar.MONDAY)) + "-" + year;
                } else {
                    newDate = day + "-" + monthAdded(date.get(Calendar.MONDAY)) + "-" + year;
                }
                Log.d(TAG, "Date Selected: " + newDate);


                getMealFromFs(Foods.BREAKFAST, rvListBreakfast, foodListBreakfast, tvTotalBreakfast, newDate);
                getMealFromFs(Foods.LUNCH, rvListLunch, foodListLunch, tvTotalLunch, newDate);
                getMealFromFs(Foods.DINNER, rvListDinner, foodListDinner, tvTotalDinner, newDate);
                getMealFromFs(Foods.SNACK, rvListSnacks, foodListSnacks, tvTotalSnacks, newDate);

                progressdialog.hide();
            }
        });
    }

    private void restData() {
        kcal = fat = protein = carbs = 0;
        progressCarbs.setProgress(0);
        progressFat.setProgress(0);
        progressProtien.setProgress(0);
        getUserDataAndSetGoal();
        foodListBreakfast.clear();
        foodListDinner.clear();
        foodListLunch.clear();
        foodListSnacks.clear();
        breakfastContainer.setVisibility(View.GONE);
        lunchContainer.setVisibility(View.GONE);
        dinnerContainer.setVisibility(View.GONE);
        snacksContainer.setVisibility(View.GONE);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMealFromFs(final String keyValue, final RecyclerView recyclerView,
                               final List<Foods> foodList, final TextView totalMeal, String date) {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        //get NUTRITION from firestone
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(keyValue).document(date)
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

                                visibleNutrition(keyValue);

                                initRecyclerView(recyclerView, foodList, keyValue);
                                //Disable RecyclerView scrolling
                                recyclerView.setNestedScrollingEnabled(false);

                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //set NUTRITION to float
                                kcal += foodList.get(i).getNfCalories();
                                carbs += foodList.get(i).getNfTotalCarbohydrate();
                                fat += foodList.get(i).getNfTotalFat();
                                protein += foodList.get(i).getNfProtein();
                            }
                            totalMeal.setText(String.format(Locale.US, "Total: %.2f" + " Kcal.  " + "%.2f" + " Carbs.  " + "%.2f" + " Protein.  " + "%.2f" + " Fat.  ", kcal, carbs, protein, fat));

                            kcal = carbs = fat = protein = 0;

                            updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
                            updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
                            updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);
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


    private void visibleNutrition(String nutritionType) {
        switch (nutritionType) {
            case Foods.BREAKFAST:
                breakfastContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.LUNCH:
                lunchContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.DINNER:
                dinnerContainer.setVisibility(View.VISIBLE);
                break;
            case Foods.SNACK:
                snacksContainer.setVisibility(View.VISIBLE);
        }
    }


    private void deleteFromFirebase(final String keyValue, final String foodName, final int qty) {
        db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                .collection(keyValue).document(UserRegister.getTodayDate())
                .collection(Foods.All_NUTRITION).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);

                                assert foods != null;
                                if (foodName.equals(foods.getFoodName()) && qty == foods.getServingQty()) {

                                    String id = task.getResult().getDocuments().get(i).getId();

                                    //delete from firebase
                                    db.collection(Foods.NUTRITION).document(FireBaseInit.getEmailRegister())
                                            .collection(keyValue).document(UserRegister.getTodayDate())
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
    private void initRecyclerView(RecyclerView recyclerView, final List<Foods> foodList, String nutrition) {

        Log.d(TAG, "initRecyclerView: init food recyclerView" + recyclerView);

        @SuppressLint("WrongConstant")
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        switch (nutrition) {
            case Foods.BREAKFAST:
                BreakfastListAdapter breakfastAdapter = new BreakfastListAdapter(this, foodList);
                recyclerView.setAdapter(breakfastAdapter);
                breakfastAdapter.notifyDataSetChanged();
                break;
            case Foods.LUNCH:
                LunchListAdapter lunchAdapter = new LunchListAdapter(this, foodList);
                recyclerView.setAdapter(lunchAdapter);
                lunchAdapter.notifyDataSetChanged();
                break;
            case Foods.DINNER:
                DinnerListAdapter dinnerAdapter = new DinnerListAdapter(this, foodList);
                recyclerView.setAdapter(dinnerAdapter);
                dinnerAdapter.notifyDataSetChanged();
                break;
            case Foods.SNACK:
                SnacksListAdapter snacksAdapter = new SnacksListAdapter(this, foodList);
                recyclerView.setAdapter(snacksAdapter);
                snacksAdapter.notifyDataSetChanged();

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

                deleteFromFirebase(keyValue, item.getFoodName(), item.getServingQty());

                foodList.remove(position);
                recyclerView.removeViewAt(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, foodList.size());

                final Snackbar snackbar = Snackbar
                        .make(constraintLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mAdapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    private void getUserDataAndSetGoal() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(UserRegister.FIRE_BASE_USERS).document(FireBaseInit.getEmailRegister()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserRegister.class);

                        assert user != null;
                        calculationGoal = user.thermicEffect(user.getActivityLevel());

                        tvCarbs.setText(String.format(Locale.getDefault(), "%.0fg of %dg", carbs, calculationGoal / 2));
                        tvProtien.setText(String.format(Locale.getDefault(), "%.0fg of %dg", protein, calculationGoal * 20 / 100));
                        tvFat.setText(String.format(Locale.getDefault(), "%.0fg of %dg", fat, calculationGoal * 30 / 100));

                        updateProgressColor(progressCarbs, carbs, calculationGoal / 2);
                        updateProgressColor(progressProtien, protein, calculationGoal * 20 / 100);
                        updateProgressColor(progressFat, fat, calculationGoal * 30 / 100);

                        progressdialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void updateProgressColor(RoundCornerProgressBar roundCornerProgressBar, float nutrition, int calculationGoal) {

        float fractionToPercent = nutrition / calculationGoal * 100;
        roundCornerProgressBar.setProgress(fractionToPercent);
        float progress = roundCornerProgressBar.getProgress();
        if (progress <= 50) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.yellow_dolly));
        } else if (progress > 75 && progress <= 100) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_green_600));
        } else if (progress > 100) {
            roundCornerProgressBar.setProgressColor(getResources().getColor(R.color.md_red_500));
        }
    }

    private String monthAdded(int month) {
        List<String> monthName = new ArrayList<>();
        monthName.add("Jan");
        monthName.add("Feb");
        monthName.add("Mar");
        monthName.add("Apr");
        monthName.add("May");
        monthName.add("Jun");
        monthName.add("Jul");
        monthName.add("Aug");
        monthName.add("Sep");
        monthName.add("Oct");
        monthName.add("Nov");
        monthName.add("Dec");
        return monthName.get(month);
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
