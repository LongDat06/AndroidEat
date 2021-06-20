package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Model.Commen;
import Model.Food;
import Model.Order;
import Model.Request;
import ViewHolder.FoodViewHolder;
import ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference requests = database.getReference("Requests");
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        recyclerView = findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        if(getIntent() == null)
            loadOrder(Commen.currentUser.getPhone());
//        else
//            loadOrder(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrder(String phone) {
        Query orderStatus = requests.orderByChild("phone").equalTo(phone);
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(orderStatus, Request.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Request request) {
                orderViewHolder.txtOrderID.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(Commen.convertCodeToStatus(request.getStatus()));
                orderViewHolder.txtOrderAddress.setText(request.getAddress());
                orderViewHolder.txtOrderPhone.setText(request.getPhone());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout,parent,false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}

