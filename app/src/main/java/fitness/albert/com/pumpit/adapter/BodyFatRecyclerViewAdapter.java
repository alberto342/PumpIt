package fitness.albert.com.pumpit.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fitness.albert.com.pumpit.R;
import fitness.albert.com.pumpit.model.PrefsUtils;

public class BodyFatRecyclerViewAdapter extends RecyclerView.Adapter<BodyFatRecyclerViewAdapter.ViewHolder> {

    private OnBodyFatListener mOnBodyFatListener;
    private static final String TAG = "BodyFatRecyclerViewAdapter";

    //vars
    private List<String> bodyFat;
    private List<Integer> mImageUrls;
    private Context mContext;

    public BodyFatRecyclerViewAdapter(Context context, List<String> bodyFat, List<Integer> image, OnBodyFatListener onBodyFatListener) {
        this.bodyFat = bodyFat;
        this.mImageUrls = image;
        this.mContext = context;
        this.mOnBodyFatListener = onBodyFatListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view, mOnBodyFatListener);
    }

    @SuppressLint({"LongLogTag", "SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        final PrefsUtils prefsUtils = new PrefsUtils();
        prefsUtils.createSharedPreferencesFiles(mContext, PrefsUtils.SETTINGS_PREFERENCES_FILE);

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText("BODY FAT: " + bodyFat.get(position));

//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(mContext, bodyFat.get(position), Toast.LENGTH_SHORT).show();
//
//                if(mContext instanceof BodyFatActivity) {
//                    prefsUtils.saveData("body_fat", bodyFat.get(position));
//                    isSelected = true;
//
//                }
//
//                if(mContext instanceof FatTargetActivity) {
//                    prefsUtils.saveData("fat_target",bodyFat.get(position));
//                }
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public interface OnBodyFatListener {
        void onBodyFatClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        OnBodyFatListener onBodyFatListener;
        ImageView image;
        TextView name;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView, OnBodyFatListener onBodyFatListener) {
            super(itemView);
            this.image = itemView.findViewById(R.id.image_view);
            this.name = itemView.findViewById(R.id.tv_body_fat);
            this.relativeLayout = itemView.findViewById(R.id.rl_body_fat);
            this.onBodyFatListener = onBodyFatListener;
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            onBodyFatListener.onBodyFatClick(getAdapterPosition());
        }
    }



}