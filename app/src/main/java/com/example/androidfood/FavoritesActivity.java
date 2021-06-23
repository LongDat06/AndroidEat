package com.example.androidfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Database.Database;
import Model.Commen;
import Model.Favorites;
import ViewHolder.CartAdapter;
import ViewHolder.FavoritesAdapter;


public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FavoritesAdapter adapter;

    RelativeLayout relativeLayout;
    List<Favorites> favoritesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        relativeLayout = findViewById(R.id.root_layout);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_favorites);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        loadFavoritesfood();

    }




    private void loadFavoritesfood() {

        favoritesList = new Database(this).getFavorites(Commen.currentUser.getPhone());
        adapter = new FavoritesAdapter(favoritesList,this);
        recyclerView.setAdapter(adapter);
    }


}