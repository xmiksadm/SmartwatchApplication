package com.example.bakalarka.activities.overview;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.bakalarka.activities.overview.basic.OverviewActivity;

public class OverviewRiskyRoomsActivity extends OverviewActivity {
    public static final String RISK_KEY = "risk";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState == null){
            savedInstanceState = new Bundle();
        }
        savedInstanceState.putBoolean(RISK_KEY, true);
        super.onCreate(savedInstanceState);
        //updateOverview();
    }
/*
    private void updateOverview(){
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                for (Fragment fragment: riskyRoomsFragmentPager.mFragments){
                    OverviewFragment overviewFragment = (OverviewFragment)fragment;
                    overviewFragment.updateFragment();
                }
            }
        };
        handler.postDelayed(r, 2*60*1000);
    }

 */
}
