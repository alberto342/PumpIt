package com.albert.fitness.pumpit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albert.fitness.pumpit.model.WorkoutPlanObj;
import com.albert.fitness.pumpit.workout.WorkoutActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import fitness.albert.com.pumpit.R;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WorkoutPlanObj> plansList;
    public static int posit;

    public WorkoutPlanAdapter(Context mContext, List<WorkoutPlanObj> plansList) {
        this.mContext = mContext;
        this.plansList = plansList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_workout_plans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindViews((ViewHolder) holder, position);
    }


    private void bindViews(final ViewHolder holder, final int position) {
        final String TAG = "WorkoutPlanAdapter";

        holder.tvPlanName.setText(plansList.get(position).getRoutineName());
        holder.tvGeneral.setText(plansList.get(position).getDaysWeek());

        Log.d(TAG, "Plan name: " + plansList.get(position).getRoutineName() + " Workout day Name: " + plansList.get(position).getDaysWeek());

        holder.itemPlanSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                posit = position;

                plansList.get(position).getPlanId();

                Intent i = new Intent(mContext, WorkoutActivity.class);
                i.putExtra("id", plansList.get(position).getPlanId());
                i.putExtra("workoutSize", plansList.get(position).getDaysWeekPosition());
                i.putExtra("routineName", plansList.get(position).getRoutineName());
                i.putExtra("difficultyLevel", plansList.get(position).getDifficultyLevel());
                mContext.startActivity(i);

                //  getWorkPlanId(position);
               // mContext.startActivity(new Intent(mContext, WorkoutActivity.class));
            }
        });

        String type = plansList.get(position).getRoutineType();

        switch (type) {
            case "General Fitness":
                Picasso.get()
                        .load(R.mipmap.ic_general_fitness)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.ivPlanIcon);
                break;
            case "Bulking":
                Picasso.get()
                        .load(R.mipmap.ic_bulking)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.ivPlanIcon);
                break;
            case "Cutting":
                Picasso.get()
                        .load(R.mipmap.ic_scale)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.ivPlanIcon);
                break;
            case "Sport Specific":
                Picasso.get()
                        .load(R.mipmap.ic_sport_specific)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.ivPlanIcon);
        }
    }


    //Get workoutName item id
//   PrefsUtils prefsUtils = new PrefsUtils();
//   prefsUtils.createSharedPreferencesFiles(mContext, "exercise");
//   prefsUtils.saveData("planName", task.getResult().getDocuments().get(position).getId());


    @Override
    public int getItemCount() {
        return plansList.size();
    }


    public List<WorkoutPlanObj> getData() {
        return plansList;
    }


//    public void restoreItem(WorkoutPlans item, int position) {
//        plansList.add(position, item);
//        notifyItemInserted(position);
//    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //   private ImageView ivImage;
        private TextView tvPlanName, tvGeneral;
        private LinearLayout itemPlanSelected;
        private ImageView ivPlanIcon;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            //      ivImage = rowView.findViewById(R.id.iv_plan_icon);
            tvPlanName = rowView.findViewById(R.id.tv_workout_plan_name);
            itemPlanSelected = rowView.findViewById(R.id.item_plan_selected);
            tvGeneral = rowView.findViewById(R.id.tv_general);
            ivPlanIcon = rowView.findViewById(R.id.iv_plan_icon);
        }

        @Override
        public void onClick(View v) {

        }
    }
}