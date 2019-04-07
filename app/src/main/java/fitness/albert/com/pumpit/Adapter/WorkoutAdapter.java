package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WorkoutPlans> workoutList;

    public WorkoutAdapter(Context mContext, List<WorkoutPlans> workoutList) {
        this.mContext = mContext;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_workout, parent, false);
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

       holder.tvDay.setText(workoutList.get(position).getDaysWeek());
       holder.tvExerciseName.setText(workoutList.get(position).getRoutineName());
    }



    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvExerciseCount, tvExerciseName, tvDay;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            this.tvExerciseCount = rowView.findViewById(R.id.tv_exercise_conut);
            this.tvExerciseName = rowView.findViewById(R.id.tv_exercise_name_work);
            this.tvDay = rowView.findViewById(R.id.tv_day);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
