package com.starbrands.stb_carburant.ui.dashbord;


import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.starbrands.stb_carburant.Home;
import com.starbrands.stb_carburant.R;

public class DashbordFragment extends Fragment{

    private DashbordViewModel mViewModel;
    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;



    public  DashbordFragment(){
    }

    public static DashbordFragment newInstance() {
        return new DashbordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.dashbord_fragment, container, false);
        viewPager=myFragment.findViewById(R.id.viewPager);
        tabLayout=myFragment.findViewById(R.id.tabLayout);
        setUpViewPager(viewPager);

        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates for imageview
                refreshHandler.postDelayed(this, 10 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 10 * 1000);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashbordViewModel.class);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        PagerAdapter pagerAdapter=new PagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(new KeyNumbersFragment(),"Chiffres clés");
        pagerAdapter.addFragment(new StatsFragment(),"Tableaux & Statistiques");
        viewPager.setAdapter(pagerAdapter);
    }
}