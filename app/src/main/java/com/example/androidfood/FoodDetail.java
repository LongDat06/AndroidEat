package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import Database.Database;
import Model.Commen;
import Model.Food;
import Model.Order;
import Model.Rating;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView food_name, food_price;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton,btn_rating;
    ElegantNumberButton elegantNumberButton;
    RatingBar ratingBar;
    String foodId="";

    FirebaseDatabase database;
    DatabaseReference detail_food;
    DatabaseReference ratingtbl;


    Food currentFood;
    Button btn_them,btn_showcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        img_food = findViewById(R.id.img_food1);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        floatingActionButton = findViewById(R.id.cart_floating);
        elegantNumberButton = findViewById(R.id.number_btn);
        btn_rating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar);
        btn_showcomment = findViewById(R.id.btnShowComment);

        btn_showcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetail.this,ShowComment.class);
                intent.putExtra(Commen.INTENT_FOOD_ID,foodId);
                startActivity(intent);
            }
        });

        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(FoodDetail.this,Cart.class);
                startActivity(cart);
            }
        });

        btn_them = findViewById(R.id.btn_them);
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addtoCarts(new Order(
                        foodId,
                        currentFood.getName(),
                        elegantNumberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getImg()
                ));
                Toast.makeText(FoodDetail.this , "Add to Cart", Toast.LENGTH_SHORT).show();
            }
        });


        database = FirebaseDatabase.getInstance();
        detail_food = database.getReference("Food");
        ratingtbl = database.getReference("Rating");



        if(getIntent() != null){
            foodId = getIntent().getStringExtra("foodId");
        }

        if(!foodId.isEmpty()){
            getDetailFood(foodId);
            getRatingFood(foodId);
        }



    }

    private void getRatingFood(String foodId) {
        com.google.firebase.database.Query foodRating = ratingtbl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count  = 0, sum = 0;
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot:snapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Close")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", " Very Good ", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rating this Food")
                .setDescription("Please select some star and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();
    }

    private void getDetailFood(String foodId) {
        detail_food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImg()).into(img_food);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_name.setText(currentFood.getName());
                food_price.setText(currentFood.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comment) {
        Rating rating = new Rating(Commen.currentUser.getPhone(),
                foodId,
                String.valueOf(value),
                comment);

        ratingtbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(FoodDetail.this,"Thanks you for submit rating ",Toast.LENGTH_SHORT).show();
                    }
                });

//        ratingtbl.child(Commen.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(Commen.currentUser.getPhone()).exists()){
//                    ratingtbl.child(Commen.currentUser.getPhone()).removeValue();
//                    ratingtbl.child(Commen.currentUser.getPhone()).setValue(rating);
//                }else {
//                    ratingtbl.child(Commen.currentUser.getPhone()).setValue(rating);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}