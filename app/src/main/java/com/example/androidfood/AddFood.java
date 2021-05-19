package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import Model.Food;

public class AddFood extends AppCompatActivity {

    TextView txtnamefood, txtimgfood,txtprice,txtmenuid;
    Button btn_addfood;
    RelativeLayout background;
    ImageView img_logo;
    int maid = 0;
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            background.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        txtnamefood = findViewById(R.id.name_food);
        txtimgfood = findViewById(R.id.img_food);
        txtprice = findViewById(R.id.price_food);
        txtmenuid = findViewById(R.id.menu_id);
        btn_addfood = findViewById(R.id.btn_addfood);
        background = findViewById(R.id.img_backgroundaddfood);
        img_logo = findViewById(R.id.img_addfood);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_food = database.getReference("Food");

        table_food.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maid = (int) snapshot.getChildrenCount();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Food food = new Food();
        btn_addfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setName(txtnamefood.getText().toString());
                food.setImg(txtimgfood.getText().toString());
                food.setPrice(txtprice.getText().toString());
                food.setMenuId(txtmenuid.getText().toString());
                table_food.child(String.valueOf(maid+1)).setValue(food);
                Toast.makeText(AddFood.this, "Add Food Successfully", Toast.LENGTH_SHORT).show();
                Intent admin = new Intent(AddFood.this, com.example.androidfood.admin.class);
                startActivity(admin);
                finish();

            }
        });
    }
}