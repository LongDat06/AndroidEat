package ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.R;

import Interface.ItemClickListener;
import Model.Commen;

public class OrderViewHolder extends RecyclerView.ViewHolder  {
    public TextView txtOrderID,txtOrderStatus, txtOrderPhone, txtOrderAddress;

    public Button  btnRemove, btnDetail ;


    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderID = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);


        btnRemove = itemView.findViewById(R.id.btn_remove);
        btnDetail = itemView.findViewById(R.id.btn_detail);



    }





}
