package ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androidfood.Cart;
import com.example.androidfood.DishViewedActivity;
import com.example.androidfood.FavoritesActivity;
import com.example.androidfood.FoodDetail;
import com.example.androidfood.FoodList;
import com.example.androidfood.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Database.Database;
import Interface.ItemClickListener;
import Model.Commen;
import Model.DishViewed;
import Model.Favorites;
import Model.Order;

class DishViewedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_img;
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setFood_name(TextView food_name) {
        this.food_name = food_name;
    }

    public void setFood_img(ImageView food_img) {
        this.food_img = food_img;
    }

    public DishViewedViewHolder(@NonNull View itemView) {
        super(itemView);

        food_name = (TextView)itemView.findViewById(R.id.food_name);
        food_img = (ImageView)itemView.findViewById(R.id.food_img);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAbsoluteAdapterPosition(),false);
    }
}


public class DishViewedAdapter extends RecyclerView.Adapter<DishViewedViewHolder>{

    private List<DishViewed> dishVieweds = new ArrayList<>();
    private DishViewedActivity dishViewedActivity;


    public DishViewedAdapter(List<DishViewed> dishVieweds, DishViewedActivity dishViewedActivity) {
        this.dishVieweds = dishVieweds;
        this.dishViewedActivity = dishViewedActivity;
    }

    @NonNull
    @Override
    public DishViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(dishViewedActivity);
        View itemView = inflater.inflate(R.layout.food_viewed_item,parent,false);
        return new DishViewedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewedViewHolder holder, int position) {
        holder.food_name.setText(dishVieweds.get(position).getFoodName());
        Picasso.with(dishViewedActivity.getBaseContext()).load(dishVieweds.get(position).getFoodImg()).resize(410,200).centerCrop().into(holder.food_img);

        final DishViewed local = dishVieweds.get(position);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent fooddetail = new Intent(dishViewedActivity, FoodDetail.class);
                fooddetail.putExtra("foodId",dishVieweds.get(position).getFoodId());
                dishViewedActivity.startActivity(fooddetail);

            }
        });

    }

    public void removeItem(int position){
        dishVieweds.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return dishVieweds.size();
    }



}