package fitness.albert.com.pumpit.fragment.logsFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import fitness.albert.com.pumpit.Adapter.FinishWorkoutAdapter;
import fitness.albert.com.pumpit.Model.FinishTraining;
import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.SwipeHelper;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogWorkoutFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "LogWorkoutFragment";
    private List<FinishTraining> finishTrainingList = new ArrayList<>();
    private RecyclerView rvWorkout;

    public LogWorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getWorkoutFromFb(LogFragment.date);
        swipe();
        Log.d(TAG, "date of exercise: " + LogFragment.date);
    }

    private void init(View view) {
        rvWorkout = view.findViewById(R.id.rv_log_workout);
    }



    private void getWorkoutFromFb(String date) {

        db.collection(FinishTraining.TRAINING_LOG).document(FireBaseInit.getEmailRegister())
                .collection(date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult() != null) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        FinishTraining finishTraining = task.getResult().getDocuments().get(i).toObject(FinishTraining.class);
                        finishTrainingList.add(finishTraining);
                        initRecyclerView();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "field get data " + e);
            }
        });
    }

    private void initRecyclerView() {
        FinishWorkoutAdapter finishWorkoutAdapter;

        Log.d(TAG, "initRecyclerView: init FinishWorkout recyclerView" + rvWorkout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvWorkout.setLayoutManager(layoutManager);
        finishWorkoutAdapter = new FinishWorkoutAdapter(getActivity(), finishTrainingList);
        rvWorkout.setAdapter(finishWorkoutAdapter);
    }


    private void swipe() {
        new SwipeHelper(getContext(), rvWorkout) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        // R.color.colorAccent,
                        Color.parseColor("#d50000"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                            //    deleteItem(pos);
                              //  deleteFromFb(pos);
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        0,
                        //R.color.md_green_500,
                        Color.parseColor("#4caf50"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                            }
                        }
                ));
            }
        };
    }
}
