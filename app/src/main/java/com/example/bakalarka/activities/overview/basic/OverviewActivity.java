package com.example.bakalarka.activities.overview.basic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.bakalarka.activities.MainActivity;
import com.example.bakalarka.R;
import com.example.bakalarka.activities.overview.OverviewRiskyRoomsActivity;
import com.example.bakalarka.data.room.RoomController;
import com.example.bakalarka.data.room.Room;
import com.example.bakalarka.activities.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

// Zobrazenie miestností
public class OverviewActivity extends BaseActivity {
    static public boolean ACTIVE = false;
    public static OverviewFragmentPager allRoomsFragmentPager;
    public static OverviewFragmentPager riskyRoomsFragmentPager;
    RoomController roomController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert savedInstanceState != null;
        boolean risk = savedInstanceState.getBoolean(OverviewRiskyRoomsActivity.RISK_KEY);
        int page = savedInstanceState.getInt("page");
        System.out.println("Id in overview: "+page);
        setContentView(R.layout.activity_overview);

        this.roomController = new RoomController();

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.content_pager);
        //Toolbar mToolbar = findViewById(R.id.toolbar);

        // Vytvorenie toľko fragmentov koľko je izieb
        if (risk){
            // Vytvorenie fragmentPagera vďaka ktorému je možné preklikávať alebo presúvať medzi rôznymi izbami
            OverviewActivity.riskyRoomsFragmentPager = new OverviewFragmentPager(getSupportFragmentManager());

            List<Room> rooms = roomController.getRiskyRooms();

            for(int i = 0; i< rooms.size(); i++){
                // Do každého fragmentu sú odosielané údaje o ktorú izbu ide
                Bundle args = new Bundle();
                int id = RoomController.rooms.get(i).getId();
                args.putInt("room", id);
                OverviewActivity.riskyRoomsFragmentPager.addFragment(OverviewFragment.newInstance(args));
            }

            viewPager.setAdapter(riskyRoomsFragmentPager);
        }else{
            OverviewActivity.allRoomsFragmentPager = new OverviewFragmentPager(getSupportFragmentManager());
            List<Room> rooms = RoomController.rooms;
            for(int i = 0; i< rooms.size(); i++){
                // Do každého fragmentu sú odosielané údaje o ktorú izbu ide
                Bundle args = new Bundle();
                args.putInt("room", RoomController.rooms.get(i).getId());
                OverviewActivity.allRoomsFragmentPager.addFragment(OverviewFragment.newInstance(args));
            }

            viewPager.setAdapter(allRoomsFragmentPager);
        }

        if (page != 0){
            System.out.println(page);
            viewPager.setCurrentItem(roomController.getPositionInList(page));
        }

        //updateOverview();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

/*
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                (SwipeRefreshLayout.OnRefreshListener) () -> {
                    Intent intent;
                    if (risk){
                        intent = new Intent(getApplicationContext(), OverviewRiskyRoomsActivity.class);
                    }else{
                        intent = new Intent(getApplicationContext(), OverviewAllRoomsActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }
        );
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        ACTIVE = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        ACTIVE = false;
    }
}
