package fitness.albert.com.pumpit.workout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import fitness.albert.com.pumpit.R;

public class ShowLogExercise extends AppCompatActivity {

    private View rowView;
    private LinearLayout container;
    private TextView set;
    private EditText reps, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log_exercise);

        init();


        addLayoutSetsAndRep();

        addLayoutSetsAndRep();


    }

    private void init() {
        container = findViewById(R.id.contener_layout_log);
    }


    @SuppressLint("InflateParams")
    private void addLayoutSetsAndRep() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = inflater.inflate(R.layout.layout_log_sets_and_rep, null);
        initLayoutReps(rowView);

        container.addView(rowView, container.getChildCount() - 1);

    }

    private void initLayoutReps(View rowView) {
        set = rowView.findViewById(R.id.tv_log_sets_num);
        reps = rowView.findViewById(R.id.et_log_reps);
        weight = rowView.findViewById(R.id.et_log_weight);
    }
}
