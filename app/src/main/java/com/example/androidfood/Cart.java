package com.example.androidfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Database.Database;
import Model.Commen;
import Model.Order;
import Model.Request;
import ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    RelativeLayout background;
//    Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//            background.setBackground(new BitmapDrawable(getResources(), bitmap));
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//        }
//    };
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    public TextView txtTotalPrice;
    Button btnPlace;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference requests = database.getReference("Requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.list_cart);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);



        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btn_planceorder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();
            }
        });
        


//        Picasso picasso = Picasso.with(this);
//        picasso.load(
//                R.drawable.my_bg
//        ).into(target);
//
        loadlistfood();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address: ");

        final EditText edtaddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtaddress.setLayoutParams(lp);
        alertDialog.setView(edtaddress);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Commen.currentUser.getPhone(),
                        Commen.currentUser.getName(),
                        edtaddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Order Successfull",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Cart.this,OrderStatus.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void loadlistfood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:cart){
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }



}