package fitness.albert.com.pumpit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import fitness.albert.com.pumpit.Model.CustomExerciseName;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.workout.ExerciseDetailActivity;

public class CustomExerciseAdapter extends RecyclerView.Adapter<CustomExerciseAdapter.ViewHolder> {

    private Context mContext;
    private List<CustomExerciseName> customExerciseNameList;

    public CustomExerciseAdapter(Context mContext, List<CustomExerciseName> customExerciseNameList) {
        this.mContext = mContext;
        this.customExerciseNameList = customExerciseNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_custom_exercise, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String TAG = "CustomExerciseAdapter";
        String patch = customExerciseNameList.get(position).getPath();
        String fileName = customExerciseNameList.get(position).getFile_name();

        try {
            Bitmap bitCustom = MediaStore.Images.Media.getBitmap(mContext.getContentResolver() , Uri.parse(patch + fileName));

            Glide.with(mContext)
                    .load(bitCustom)
                    .into(viewHolder.ivImage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "patch: " + patch + " exerciseName: " + customExerciseNameList.get(position).getExercise_name());

        viewHolder.exerciseName.setText(customExerciseNameList.get(position).getExercise_name());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ExerciseDetailActivity.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivImage;
        private TextView exerciseName;

        public ViewHolder(View rowView) {
            super(rowView);
            rowView.setOnClickListener(this);

            ivImage = rowView.findViewById(R.id.cus_exercise_img);
            exerciseName = rowView.findViewById(R.id.tv_cus_exercise_name);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
