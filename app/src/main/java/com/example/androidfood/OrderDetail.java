package com.example.androidfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import Model.Commen;
import ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {

    TextView orderid, orderphone,orderaddress,ordertotal;
    String order_id_value="";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderid = findViewById(R.id.order_id);
        orderphone = findViewById(R.id.order_phone);
        orderaddress = findViewById(R.id.order_address);
        ordertotal = findViewById(R.id.order_total);

        lstFoods = findViewById(R.id.lstFood);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);

        if(getIntent() != null) {
            order_id_value = getIntent().getStringExtra("OrderId");
        }
        orderid.setText(order_id_value);
        orderphone.setText(Commen.currentRequest.getPhone());
        orderaddress.setText(Commen.currentRequest.getAddress());
        ordertotal.setText(Commen.currentRequest.getTotal());

        OrderDetailAdapter adapter = new OrderDetailAdapter(Commen.currentRequest.getFood());
        adapter.notifyDataSetChanged();
        lstFoods.setAdapter(adapter);
    }
}