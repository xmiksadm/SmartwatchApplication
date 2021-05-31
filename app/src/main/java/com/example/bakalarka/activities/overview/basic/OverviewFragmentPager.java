package com.example.bakalarka.activities.overview.basic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bakalarka.data.room.RoomController;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// OverviewFragmentPager obsahuje zoznam fragmentov
public class OverviewFragmentPager extends FragmentPagerAdapter {

    @NonNull
    public final List<Fragment> mFragments;

    public OverviewFragmentPager(@NonNull FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
    }

    // Vráti nadpis fragmentu podľa pozície
    @Override
    public CharSequence getPageTitle(int id) {
        return (RoomController.rooms.get(id).getRoomName());
    }

    // Vráti fragment podľa pozície
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    // Vráti počet fragmentov
    @Override
    public int getCount() {
        return mFragments.size();
    }

    // Pridanie fragmentu
    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

}
