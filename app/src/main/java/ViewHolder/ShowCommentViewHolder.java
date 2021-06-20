package ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView txtUserPhone, txtUserComment;
    public RatingBar ratingBar;


    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserPhone = itemView.findViewById(R.id.txtUserPhone);
        txtUserComment = itemView.findViewById(R.id.txtUserComment);
        ratingBar = itemView.findViewById(R.id.ratingBar);

    }
}
