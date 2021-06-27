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
import com.example.androidfood.FavoritesActivity;
import com.example.androidfood.FoodDetail;
import com.example.androidfood.FoodList;
import com.example.androidfood.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Database.Database;
import Interface.ItemClickListener;
import Model.Commen;
import Model.Favorites;
import Model.Order;

class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_img,fav;
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

    public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);

        food_name = (TextView)itemView.findViewById(R.id.food_namefav);
        food_img = (ImageView)itemView.findViewById(R.id.food_imgfav);
        fav = itemView.findViewById(R.id.fav);
        fav.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAbsoluteAdapterPosition(),false);
    }
}


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{

    private List<Favorites> favoritesList = new ArrayList<>();
    private FavoritesActivity favoritesActivity;
    private Database database;
    public FavoritesAdapter(List<Favorites> favoritesList, FavoritesActivity favoritesActivity) {
        this.favoritesList = favoritesList;
        this.favoritesActivity = favoritesActivity;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(favoritesActivity);
        View itemView = inflater.inflate(R.layout.favorites_item,parent,false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.food_name.setText(favoritesList.get(position).getFoodName());
        Picasso.with(favoritesActivity.getBaseContext()).load(favoritesList.get(position).getFoodImg()).resize(410,200).centerCrop().into(holder.food_img);

        final Favorites local = favoritesList.get(position);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent fooddetail = new Intent(favoritesActivity, FoodDetail.class);
                fooddetail.putExtra("foodId",favoritesList.get(position).getFoodId());
                favoritesActivity.startActivity(fooddetail);

            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(favoritesActivity).removeFromFavorites(favoritesList.get(position).getFoodId(),favoritesList.get(position).getUserPhone());
                removeItem(position);
                notifyItemRemoved(position);
            }
        });
    }

    public void removeItem(int position){
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }



}