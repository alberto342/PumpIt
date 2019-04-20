package fitness.albert.com.pumpit.workout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.WorkoutPlanAdapter;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import it.shadowsheep.recyclerviewswipehelper.RecyclerViewSwipeHelper;

public class WorkoutPlansActivity extends AppCompatActivity
        implements RecyclerViewSwipeHelper.RecyclerViewSwipeHelperDelegate {

    private RecyclerView view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "WorkoutPlansActivity";
    public static List<WorkoutPlans> workoutPlansList;
    public static String planName;
    public static String routineName; // chack in EditCustomPlanActivity
    private WorkoutPlanAdapter workoutAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plans);

        getPlanFormFb();

        initRecyclerView();

        setupSwipeMenu();
    }


    private void setupSwipeMenu() {
        new RecyclerViewSwipeHelper(this, view, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_plans) {
            startActivity(new Intent(this, CustomPlanActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean showButton(int rowPosition, int buttonIndex) {
        return true;
    }

    @Override
    public int buttonWidth() {
        return 0;
    }

    @Override
    public void setupSwipeButtons(RecyclerView.ViewHolder viewHolder,
                                  List<RecyclerViewSwipeHelper.SwipeButton> swipeButtons) {
        //swipe delete
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,

                R.drawable.ic_delete,
                R.dimen.ic_delete_size,
                R.color.colorAccent,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        deleteItem(pos);
                        deleteFromFirebase(pos);
                    }
                }
        ));
        //swipe edit
        swipeButtons.add(new RecyclerViewSwipeHelper.SwipeButton(
                getBaseContext(),
                0,
                0,
                R.drawable.ic_edit_action,
                R.dimen.ic_delete_size,
                R.color.md_green_500,
                new RecyclerViewSwipeHelper.SwipeButton.SwipeButtonClickListener() {
                    @Override
                    public void onClick(int pos) {
                        routineName = workoutPlansList.get(pos).getRoutineName();
                        Log.d(TAG, "pos: " + pos + " Workout Name: " + workoutPlansList.get(pos).getRoutineName());

                        Intent i = new Intent(getBaseContext(), EditCustomPlanActivity.class);
                        startActivity(i);
                    }
                }
        ));
    }


    private void deleteItem(final int position) {
        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlansList);
        workoutPlansList.remove(position);
        view.removeViewAt(position);
        workoutAdapter.notifyItemRemoved(position);
        workoutAdapter.notifyItemRangeChanged(position, workoutPlansList.size());
    }


    private void deleteFromFirebase(final int position) {

        db.collection(WorkoutPlans.WORKOUT_PLANS).
                document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            final String id = task.getResult().getDocuments().get(position).getId();

                            //find all
                            db.collection(WorkoutPlans.WORKOUT_PLANS).
                                    document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                    .document(id).collection(Workout.WORKOUT_DAY_NAME).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful() && task.getResult() != null) {
                                        for(int i=0; i<task.getResult().size();i++) {
                                            String workoutDayId = task.getResult().getDocuments().get(i).getId();

                                            db.collection(WorkoutPlans.WORKOUT_PLANS).
                                                    document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                                    .document(id).collection(Workout.WORKOUT_DAY_NAME).document(workoutDayId).delete();
                                        }
                                    }
                                }
                            });

                            db.collection(WorkoutPlans.WORKOUT_PLANS).
                                    document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME)
                                    .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            });
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "isFailure: " + e);
                    }
                });
    }


    private void getPlanFormFb() {
        workoutPlansList = new ArrayList<>();

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        progressdialog.hide();

                        if (task.isSuccessful() && task.getResult() != null) {
                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                                WorkoutPlans workoutPlans = task.getResult().getDocuments().get(i).toObject(WorkoutPlans.class);
                                workoutPlansList.add(workoutPlans);

                                assert workoutPlans != null;
                                planName = workoutPlans.getRoutineName();

                                initRecyclerView();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
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


    private void initRecyclerView() {
        // RecyclerView view;

        view = findViewById(R.id.rv_workout_plans);

        Log.d(TAG, "initRecyclerView: init WorkoutPlan recyclerView" + view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutPlanAdapter(this, workoutPlansList);
        view.setAdapter(workoutAdapter);
    }
}
