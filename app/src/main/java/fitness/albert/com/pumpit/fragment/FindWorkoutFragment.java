package fitness.albert.com.pumpit.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitness.albert.com.pumpit.Adapter.ViewPageAdapter;
import fitness.albert.com.pumpit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindWorkoutFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;


    public FindWorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter adapter = new ViewPageAdapter(getChildFragmentManager());
        adapter.addFrag(new PlanFragment());
        adapter.addFrag(new FeaturedFragment());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
