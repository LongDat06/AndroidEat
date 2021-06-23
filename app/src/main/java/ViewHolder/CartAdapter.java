package ViewHolder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androidfood.Cart;
import com.example.androidfood.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Database.Database;
import Interface.ItemClickListener;
import Model.Order;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_cart_name,txt_cart_price;
    public ElegantNumberButton btn_quatity;
    private ItemClickListener itemClickListener;
    public ImageView img_cart;

    public void setTxt_cart_name(TextView txt_cart_name){
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name= itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = itemView.findViewById(R.id.cart_item_price);
        btn_quatity = itemView.findViewById(R.id.btn_quatity);
        img_cart = itemView.findViewById(R.id.img_Cart);
    }

    @Override
    public void onClick(View v) {

    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
//        holder.img_cart_count.setImageDrawable(drawable);
        holder.btn_quatity.setNumber(listData.get(position).getQuantity());
        holder.btn_quatity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for(Order item :orders)
                    total += (Integer.parseInt(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

                Locale locale = new Locale("en","US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                cart.txtTotalPrice.setText(fmt.format(total));
            }
        });

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_cart_price.setText("$"+ listData.get(position).getPrice());
        holder.txt_cart_name.setText(listData.get(position).getProductName());
        Picasso.with(cart.getBaseContext()).load(listData.get(position).getImg()).resize(70,70).centerCrop().into(holder.img_cart);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
