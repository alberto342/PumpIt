package fitness.albert.com.pumpit.Adapter.ExerciseAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fitness.albert.com.pumpit.R;


public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
    ImageView image;
    LinearLayout layout;

    public ExerciseViewHolder(View itemView) {
        super(itemView);

        nameTxt =  itemView.findViewById(R.id.tv_exercise_name);
        image  = itemView.findViewById(R.id.iv_image);
        layout = itemView.findViewById(R.id.item_food_selected);
    }
}
