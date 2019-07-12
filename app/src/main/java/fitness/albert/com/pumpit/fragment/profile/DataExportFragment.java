package fitness.albert.com.pumpit.fragment.profile;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataExportFragment extends Fragment {


    public DataExportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_export, container, false);
    }

}
