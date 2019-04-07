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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import fitness.albert.com.pumpit.Model.FireBaseInit;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.WorkoutActivity;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "WorkoutPlanAdapter";
    private Context mContext;
    private List<WorkoutPlans> plansList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String fireId;
    public static int posit;


    public WorkoutPlanAdapter(Context mContext, List<WorkoutPlans> plansList) {
        this.mContext = mContext;
        this.plansList = plansList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_workout_plans, parent, false);
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



        Log.d(TAG, "Plan name: " + plansList.get(position).getRoutineName() + " Days/Week: " + plansList.get(position).getDaysWeek());

        holder.itemPlanSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                posit = position;

                getWorkPlanId(position);
                mContext.startActivity(new Intent(mContext, WorkoutActivity.class));
            }
        });


//        Picasso.get()
//                .load()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.ivImage);


    }


    //Get firebase item id
    private void getWorkPlanId(final int position) {
        db.collection(WorkoutPlans.WORKOUT_PLANS).document(FireBaseInit.getEmailRegister())
                .collection(WorkoutPlans.WORKOUT_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            fireId = task.getResult().getDocuments().get(position).getId();

                            Log.d(TAG, "Documents: " + task.getResult().getDocuments() +
                                    "\nFireId: " + fireId);
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


    @Override
    public int getItemCount() {
        return plansList.size();
    }


//    public void removeItem(int position) {
//        notifyItemRemoved(position);
//    }


//    public void restoreItem(Foods item, int position) {
//        notifyItemInserted(position);
//    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //   private ImageView ivImage;
        private TextView tvPlanName, tvGeneral;
        private LinearLayout itemPlanSelected;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            //      ivImage = rowView.findViewById(R.id.iv_plan_icon);
            tvPlanName = rowView.findViewById(R.id.tv_workout_plan_name);
            itemPlanSelected = rowView.findViewById(R.id.item_plan_selected);
            tvGeneral = rowView.findViewById(R.id.tv_general);
        }

        @Override
        public void onClick(View v) {

        }
    }
}