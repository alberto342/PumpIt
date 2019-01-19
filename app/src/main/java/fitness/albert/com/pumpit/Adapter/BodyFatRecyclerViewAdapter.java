package fitness.albert.com.pumpit.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.WelcomeActivities.BodyFatActivity;
import fitness.albert.com.pumpit.WelcomeActivities.FatTargetActivity;

public class BodyFatRecyclerViewAdapter extends RecyclerView.Adapter<BodyFatRecyclerViewAdapter.ViewHolder> {

    private ImageView next;

    private static final String TAG = "BodyFatRecyclerViewAdapter";
    private SharedPreferences SPSaveTheCounter;
    private SharedPreferences.Editor editor;


    //vars
    private List<String> bodyFat;
    private List<String> mImageUrls;
    private Context mContext;

    public BodyFatRecyclerViewAdapter(Context context, List<String> bodyFat, List<String> imageUrls) {

        this.bodyFat = bodyFat;
        this.mImageUrls = imageUrls;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

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

                createSharedPreferencesFiles();

                if(mContext instanceof BodyFatActivity) {
                    sharedPreferencesSaveData("bodyFat", bodyFat.get(position));
                }

                if(mContext instanceof FatTargetActivity) {
                    sharedPreferencesSaveData("fatTarget", bodyFat.get(position));
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


    @SuppressLint("WrongConstant")
    private void createSharedPreferencesFiles() {
        SPSaveTheCounter = mContext.getSharedPreferences("userInfo",Context.MODE_NO_LOCALIZED_COLLATORS);
    }

    private void sharedPreferencesSaveData(String key, String mName) {
        editor = SPSaveTheCounter.edit();
        try {
            editor.putString(key, mName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}