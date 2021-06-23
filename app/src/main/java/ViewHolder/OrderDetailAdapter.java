package ViewHolder;


import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.R;

import java.util.List;

import Model.Order;

class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView name, quatity, price;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quatity = itemView.findViewById(R.id.product_quatity);
        price = itemView.findViewById(R.id.product_price);

    }
}
public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {
    @NonNull
    List<Order> myOrder;

    public OrderDetailAdapter(@NonNull List<Order> myOrder) {
        this.myOrder = myOrder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrder.get(position);
        holder.name.setText(String.format("Name : %s",order.getProductName()));
        holder.quatity.setText(String.format("Quantity : %s",order.getQuantity()));
        holder.price.setText(String.format("price : %s",order.getPrice()));
    }

    @Override
    public int getItemCount() {
        return myOrder.size();
    }
}
