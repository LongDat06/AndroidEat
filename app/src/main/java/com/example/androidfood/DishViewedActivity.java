package com.example.androidfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import Database.Database;
import Model.Commen;
import Model.DishViewed;

import ViewHolder.DishViewedAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DishViewedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Database database;

    SwipeRefreshLayout mSwipeRefreshLayout;
    DishViewedAdapter adapter;
    List<DishViewed> dishVieweds = new ArrayList<>();
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/cf.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_dish_viewed);
        recyclerView = findViewById(R.id.recycler_dishviewed);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database= new Database(this);
        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDishViewed();
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadDishViewed();

            }
        });
    }

    private void loadDishViewed() {
        dishVieweds = new Database(this).getDishViewed(Commen.currentUser.getPhone());
        adapter = new DishViewedAdapter(dishVieweds,this);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
        List<DishViewed> list = new Database(this).getDishViewed(Commen.currentUser.getPhone());
        if(list.size() > 0) {
            for (DishViewed dishViewed : list) {
                DateFormat df = new SimpleDateFormat("mm");
                String date = df.format(Calendar.getInstance().getTime());
                String A = dishViewed.getTime();
//            Integer B =Integer.valueOf(date) - Integer.valueOf(A)  ;
                if (Integer.valueOf(date) - Integer.valueOf(A) > 0) {
                    database.removeDishViewed(dishViewed.getFoodId(), dishViewed.getUserPhone());
                }
            }
        }
    }
}