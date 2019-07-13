package fitness.albert.com.pumpit.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fitness.albert.com.pumpit.Model.PrefsUtils;
import fitness.albert.com.pumpit.Model.UserRegister;
import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.WelcomeActivities.BodyFatActivity;
import fitness.albert.com.pumpit.WelcomeActivities.FatTargetActivity;

public class BodyFatRecyclerViewAdapter extends RecyclerView.Adapter<BodyFatRecyclerViewAdapter.ViewHolder> {

    private ImageView next;

    private static final String TAG = "BodyFatRecyclerViewAdapter";


    //vars
    private List<String> bodyFat;
    private List<Integer> mImageUrls;
    private Context mContext;
    public static boolean isSelected;

    public BodyFatRecyclerViewAdapter(Context context, List<String> bodyFat, List<Integer> image) {
        this.bodyFat = bodyFat;
        this.mImageUrls = image;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        final PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(mContext, UserRegister.SharedPreferencesFile);


        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText("BODY FAT: " + bodyFat.get(position));


        holder.image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, bodyFat.get(position), Toast.LENGTH_SHORT).show();

                if(mContext instanceof BodyFatActivity) {
                    prefsUtils.saveData("bodyFat", bodyFat.get(position));
                    isSelected = true;

                }

                if(mContext instanceof FatTargetActivity) {
                    prefsUtils.saveData("fatTarget",bodyFat.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image_view);
            this.name = itemView.findViewById(R.id.tv_body_fat);
        }
    }



}