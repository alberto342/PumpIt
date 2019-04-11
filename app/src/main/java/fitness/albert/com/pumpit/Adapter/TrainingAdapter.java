package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import fitness.albert.com.pumpit.Model.Training;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.helper.BitmapFromAssent;

public class TrainingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Training> trainingList;

    public TrainingAdapter(Context mContext, List<Training> trainingList) {
        this.mContext = mContext;
        this.trainingList = trainingList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_training, parent, false);
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

        Bitmap bitmap = BitmapFromAssent.getBitmapFromAsset(mContext, trainingList.get(position).getImgName());

        int set = trainingList.get(position).getSetNumber();
        int rep = trainingList.get(position).getRepNumber();

        Glide.with(mContext)
                .load(bitmap)
                .into(holder.ivTraining);

        holder.trainingName.setText(trainingList.get(position).getExerciseName());
        holder.tvSetAndRep.setText(String.format(Locale.US,"%d SET(s) of %d REPs", set, rep));
        holder.tvRestTime.setText(String.format(Locale.US, "Rest: %d + s", trainingList.get(position).getRestTime()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }



    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivTraining;
        private TextView trainingName, tvSetAndRep, tvRestTime;
        private LinearLayout layout;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            this.ivTraining = rowView.findViewById(R.id.iv_training_thm);
            this.trainingName = rowView.findViewById(R.id.tv_training_name);
            this.tvSetAndRep = rowView.findViewById(R.id.tv_set_and_rep);
            this.tvRestTime = rowView.findViewById(R.id.tv_rest_time);
            this.layout = rowView.findViewById(R.id.item_training_selected);
        }

        @Override
        public void onClick(View v) {

        }
    }




}
