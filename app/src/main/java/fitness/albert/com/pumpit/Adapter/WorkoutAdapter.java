package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fitness.albert.com.pumpit.Model.Workout;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.TrainingActivity;

public class WorkoutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Workout> workoutList;
    private final String TAG = "WorkoutAdapter";
    public static String workoutDayName;
    public static int pos;

    public WorkoutAdapter(Context mContext, List<Workout> workoutList) {
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

       holder.tvDay.setText(workoutList.get(position).getWorkoutDay());
       holder.tvDate.setText(workoutList.get(position).getDate());
       holder.tvExerciseName.setText(workoutList.get(position).getWorkoutDayName());
       holder.tvExerciseCount.setText(String.valueOf(workoutList.get(position).getNumOfExercise()));
       holder.layout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               pos = position;

               workoutDayName = workoutList.get(position).getWorkoutDayName();

               mContext.startActivity(new Intent(mContext, TrainingActivity.class));

               Log.d(TAG, "Workout day name: " + workoutDayName);
           }
       });
    }



    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvExerciseCount, tvExerciseName, tvDay, tvDate;
        private LinearLayout layout;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            this.tvDate = rowView.findViewById(R.id.tv_recently);
            this.tvExerciseCount = rowView.findViewById(R.id.tv_exercise_conut);
            this.tvExerciseName = rowView.findViewById(R.id.tv_exercise_name_work);
            this.tvDay = rowView.findViewById(R.id.tv_day);
            this.layout = rowView.findViewById(R.id.ll_wokout_selected);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
