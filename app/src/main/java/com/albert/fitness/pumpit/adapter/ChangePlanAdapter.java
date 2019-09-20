package com.albert.fitness.pumpit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.FireBaseInit;
import com.albert.fitness.pumpit.workout.StartWorkoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import fitness.albert.com.pumpit.R;

import com.albert.fitness.pumpit.model.PrefsUtils;
import com.albert.fitness.pumpit.model.Workout;
import com.albert.fitness.pumpit.model.WorkoutPlans;

public class ChangePlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ChangePlanAdapter";
    private Context mContext;
    private List<WorkoutPlans> workoutPlansList;
    private PrefsUtils prefsUtils = new PrefsUtils();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ChangePlanAdapter(Context mContext, List<WorkoutPlans> workoutPlansList) {
        this.mContext = mContext;
        this.workoutPlansList = workoutPlansList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_change_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return workoutPlansList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }

    private void bindViews(final ViewHolder holder, final int position) {

        holder.tvName.setText(workoutPlansList.get(position).getRoutineName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnSelectedPlan.setImageResource(R.mipmap.ic_ok_selected);

                prefsUtils.createSharedPreferencesFiles(mContext, PrefsUtils.EXERCISE);
                prefsUtils.saveData("default_plan", workoutPlansList.get(position).getRoutineName());
                receivedIdFromFb(position);
                Log.d(TAG, "default program active");
                mContext.startActivity(new Intent(mContext, StartWorkoutActivity.class));
                ((Activity) mContext).finish();
            }
        });
    }


    private void receivedIdFromFb(int position) {
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister()).collection(Workout.WORKOUT_NAME)
                .whereEqualTo("routineName", workoutPlansList.get(position).getRoutineName()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (int i = 0; i < task.getResult().size(); i++) {
                                String id = task.getResult().getDocuments().get(i).getId();
                                prefsUtils.saveData("planName", id);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private ImageView btnSelectedPlan;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            this.tvName = rowView.findViewById(R.id.tv_change_plan_name);
            this.btnSelectedPlan = rowView.findViewById(R.id.change_selected_plan);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
