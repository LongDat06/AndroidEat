package ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.R;

import Interface.ItemClickListener;

public class RecomendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_img;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RecomendViewHolder(@NonNull View itemView) {
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