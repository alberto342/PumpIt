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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.FirestoreFoodListAdapter;
import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.SwipeToDeleteCallback;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_nutrition);

        init();

        getMealFromFs(Foods.breakfast, rvListBreakfast, foodListBreakfast, tvTotalBreakfast);
        getMealFromFs(Foods.lunch, rvListLunch, foodListLunch, tvTotalLunch);
        getMealFromFs(Foods.dinner, rvListDinner, foodListDinner, tvTotalDinner);
        getMealFromFs(Foods.snack, rvListSnacks, foodListSnacks, tvTotalSnacks);



        enableSwipeToDeleteAndUndo(rvListBreakfast, foodListBreakfast, Foods.breakfast);
        enableSwipeToDeleteAndUndo(rvListDinner, foodListDinner, Foods.dinner);
        enableSwipeToDeleteAndUndo(rvListLunch, foodListLunch, Foods.lunch);
        enableSwipeToDeleteAndUndo(rvListSnacks, foodListSnacks, Foods.snack);
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


    private void getMealFromFs(final String keyValue, final RecyclerView recyclerView, final List<Foods> foodList, final TextView totalMeal) {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();


        //get nutrition from firestone
        db.collection(Foods.nutrition).document(getEmailRegister())
                .collection(keyValue).document(getTodayDate())
                .collection(Foods.fruit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //hide ProgressDialog
                        progressdialog.hide();

                        if(task.isSuccessful()) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);
                                foodList.add(foods);

                                initRecyclerView(recyclerView, foodList);

                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getDocuments());

                                //set nutrition to float
                                kcal += foodList.get(i).getNf_calories();
                                carbs += foodList.get(i).getNf_total_carbohydrate();
                                fat += foodList.get(i).getNf_total_fat();
                                protein += foodList.get(i).getNf_protein();
                            }

                            totalMeal.setText(String.format("Total: %.2f" + " Kcal.  " + "%.2f" + " Carbs.  " + "%.2f" + " Protein.  " + "%.2f" + " Fat.  ", kcal, carbs, protein, fat));

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



    private void deleteFromFirebase(final String keyValue, final String foodName, final String qty) {
        db.collection(Foods.nutrition).document(getEmailRegister())
                .collection(keyValue).document(getTodayDate())
                .collection(Foods.fruit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()) {

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                Foods foods = task.getResult().getDocuments().get(i).toObject(Foods.class);

                                if(foodName.equals(foods.getFood_name()) && qty.equals(foods.getServing_qty())) {

                                    String id = task.getResult().getDocuments().get(i).getId();

                                    //delete from firebase
                                    db.collection(Foods.nutrition).document(getEmailRegister())
                                            .collection(keyValue).document(getTodayDate())
                                            .collection(Foods.fruit).document(id).delete();

                                    Log.d(TAG, "DocumentSnapshot " + task.getResult().getDocuments().get(i).getId()   + " successfully deleted!");
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






    private void initRecyclerView(RecyclerView recyclerView, List<Foods> foodList) {

        Log.d(TAG, "initRecyclerView: init food recyclerView");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new FirestoreFoodListAdapter(this, foodList);
        recyclerView.setAdapter(mAdapter);
    }


    // Swipe to delete item
    private boolean enableSwipeToDeleteAndUndo(final RecyclerView recyclerView, final List<Foods> foodList, final String keyValue) {
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

        return true;
    }






    //Without this method Data will be double
    public void onPause() {
        super.onPause();
        this.foodListBreakfast.clear();
        this.foodListDinner.clear();
        this.foodListLunch.clear();
        this.foodListSnacks.clear();
    }


    public String getEmailRegister() {
        String email = null;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        return email;
    }

    public String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
