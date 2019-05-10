package fitness.albert.com.pumpit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fitness.albert.com.pumpit.Model.SavePref;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.StartWorkoutActivity;

public class ChangePlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ChangePlanAdapter";
    private Context mContext;
    private List<WorkoutPlans> workoutPlansList;

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

                SavePref savePref = new SavePref();
                savePref.createSharedPreferencesFiles(mContext, "exercise");
                savePref.saveData("default_plan", workoutPlansList.get(position).getRoutineName());
                Log.d(TAG, "default program active");
                mContext.startActivity(new Intent(mContext, StartWorkoutActivity.class));
                ((Activity) mContext).finish();
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
