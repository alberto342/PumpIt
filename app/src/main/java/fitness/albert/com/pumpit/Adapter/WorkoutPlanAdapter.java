package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import fitness.albert.com.pumpit.Model.Foods;
import fitness.albert.com.pumpit.Model.WorkoutPlans;
import fitness.albert.com.pumpit.R;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WorkoutPlans> plansList;
    private final String TAG = "WorkoutPlanAdapter";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public WorkoutPlanAdapter(Context mContext, List<WorkoutPlans> plansList) {
        this.mContext = mContext;
        this.plansList = plansList;
    }


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

        holder.tvPlanName.setText(plansList.get(position).getRoutineName());
        holder.tvGeneral.setText(plansList.get(position).getDaysWeek());


//        Picasso.get()
//                .load()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .into(holder.ivImage);


    }


    @Override
    public int getItemCount() {
        return plansList.size();
    }


    public void removeItem(int position) {
        notifyItemRemoved(position);
    }


    public void restoreItem(Foods item, int position) {
        notifyItemInserted(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView tvPlanName, tvGeneral;
        private LinearLayout itemPlanSelected;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.iv_plan_icon);
            tvPlanName = rowView.findViewById(R.id.tv_workout_plan_name);
            itemPlanSelected = rowView.findViewById(R.id.item_food_selected);
            tvGeneral = rowView.findViewById(R.id.tv_general);
        }

        @Override
        public void onClick(View v) {

        }
    }
}