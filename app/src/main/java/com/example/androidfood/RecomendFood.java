package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Database.Database;
import Interface.ItemClickListener;
import Model.Commen;
import Model.DishViewed;
import Model.Favorites;
import Model.Food;
import Model.Recomend;
import ViewHolder.FoodViewHolder;
import ViewHolder.RecomendViewHolder;

public class RecomendFood extends AppCompatActivity {

    TextView food_name,food_name1;
    ImageView food_img,food_img1,share,farvorite;
    String menuid="";
    String name="";
    String img="";
    String phone="";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;
    Database localDB ;
    FirebaseDatabase database;
    DatabaseReference foodlist;
    FirebaseRecyclerAdapter<Recomend, RecomendViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend_food);

        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Food");

        food_name = findViewById(R.id.food_name);
        food_img = findViewById(R.id.food_img);
        farvorite = findViewById(R.id.fav);

        lstFoods = findViewById(R.id.lstFood);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);

        food_img1 = findViewById(R.id.img_food);
        food_name1 = findViewById(R.id.name_food);
        String user = Commen.currentUser.getPhone();
        name = getIntent().getStringExtra("name");
        img = getIntent().getStringExtra("img");
        phone = getIntent().getStringExtra("phone");
        food_name1.setText(name);
        Picasso.with(getBaseContext()).load(img).resize(410,200).into(food_img1);
        if(getIntent() != null)
            menuid = getIntent().getStringExtra("menuId");
        if(!menuid.isEmpty() && menuid != null){
            loadrecomendfood(menuid);
        }
        
        
    }

    private void loadrecomendfood(String menuid) {
        Query a = foodlist.orderByChild("menuId").equalTo(menuid);
        FirebaseRecyclerOptions<Recomend> options = new FirebaseRecyclerOptions.Builder<Recomend>()
                .setQuery(a,Recomend.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Recomend, RecomendViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull RecomendViewHolder recomendViewHolder, int i, @NonNull Recomend recomend) {
                recomendViewHolder.food_name.setText(recomend.getName());
                Picasso.with(getBaseContext()).load(   recomend.getImg()).resize(410,200).into(recomendViewHolder.food_img);




                recomendViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
//                        DateFormat df = new SimpleDateFormat("mm");
//                        String date = df.format(Calendar.getInstance().getTime());
//                        DishViewed dishViewed = new DishViewed();
//                        dishViewed.setFoodId(adapter.getRef(i).getKey());
//                        dishViewed.setFoodImg(recomend.getImg());
//                        dishViewed.setFoodMenu(recomend.getMenuId());
//                        dishViewed.setFoodPrice(recomend.getPrice());
//                        dishViewed.setFoodName(recomend.getName());
//                        dishViewed.setUserPhone(Commen.currentUser.getPhone());
//                        dishViewed.setTime(date);
//
//                        if(!localDB.isDishViewed(adapter.getRef(i).getKey(),  Commen.currentUser.getPhone())){
//                            localDB.addToDishViewed(dishViewed);
//                        }

                        Intent fooddetail = new Intent(RecomendFood.this,FoodDetail.class);
                        fooddetail.putExtra("foodId",adapter.getRef(position).getKey());
                        startActivity(fooddetail);

                    }
                });
            }

            @NonNull
            @Override
            public RecomendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recomend_item,parent,false);
                return new RecomendViewHolder(view);
            }
        };
        adapter.startListening();
        lstFoods.setAdapter(adapter);

    }

}