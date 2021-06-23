package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Database.Database;
import Interface.ItemClickListener;
import Model.Commen;
import Model.Favorites;
import Model.Food;
import ViewHolder.FoodViewHolder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodlist;

    String categoryId="";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggesList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    Database localDB ;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Food");

        localDB = new Database(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null){
                    loadListFood(categoryId);
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(getIntent() != null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if(!categoryId.isEmpty() && categoryId != null){
                    loadListFood(categoryId);
                }
            }
        });



        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your food");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggesList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggesList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {

        Query a = foodlist.orderByChild("name").equalTo(text.toString());
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(a,Food.class)
                .build();

        searchAdapter =  new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(   food.getImg()).resize(410,200).into(foodViewHolder.food_img);

                if(localDB.isFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone()))
                    foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);

                foodViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites = new Favorites();
                        favorites.setFoodId(adapter.getRef(i).getKey());
                        favorites.setFoodImg(food.getImg());
                        favorites.setFoodMenu(food.getMenuId());
                        favorites.setFoodPrice(food.getPrice());
                        favorites.setFoodName(food.getName());
                        favorites.setUserPhone(Commen.currentUser.getPhone());

                        if(!localDB.isFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone())){
                            localDB.addToFavorites(favorites);
                            foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(FoodList.this,""+food.getName()+" was added to Favorites Food",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            localDB.removeFromFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone());
                            foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(FoodList.this,""+food.getName()+" was remmove to Favorites Food",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        Intent fooddetail = new Intent(FoodList.this,FoodDetail.class);
                        fooddetail.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(fooddetail);

                    }
                });
            }
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodlist.orderByChild("menuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot:snapshot.getChildren()){
                            Food item = postSnapshot.getValue(Food.class);
                            suggesList.add(item.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadListFood(String categoryId) {

        Query a = foodlist.orderByChild("menuId").equalTo(categoryId);
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(a,Food.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(   food.getImg()).resize(410,200).into(foodViewHolder.food_img);

                if(localDB.isFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone()))
                    foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                foodViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites = new Favorites();
                        favorites.setFoodId(adapter.getRef(i).getKey());
                        favorites.setFoodImg(food.getImg());
                        favorites.setFoodMenu(food.getMenuId());
                        favorites.setFoodPrice(food.getPrice());
                        favorites.setFoodName(food.getName());
                        favorites.setUserPhone(Commen.currentUser.getPhone());

                        if(!localDB.isFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone())){
                            localDB.addToFavorites(favorites);
                            foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(FoodList.this,""+food.getName()+" was added to Favorites Food",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            localDB.removeFromFavorites(adapter.getRef(i).getKey(), Commen.currentUser.getPhone());
                            foodViewHolder.fav_img.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(FoodList.this,""+food.getName()+" was remmove to Favorites Food",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        Intent fooddetail = new Intent(FoodList.this,FoodDetail.class);
                        fooddetail.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(fooddetail);

                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//        searchAdapter.stopListening();
//    }
}