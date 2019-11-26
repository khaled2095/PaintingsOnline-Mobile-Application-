package com.example.paintingsonline.Order;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Order;
import com.example.paintingsonline.Model.UserRating;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;

import java.util.List;

public class OrderAdapterView extends RecyclerView.Adapter<OrderAdapterView.OrderViewHolder>
{
    private Context ordercontext;
    private List<Order> orderList;
    private List<UserRating> userRatings;
    private OnRatingListener onRateListener;


    public OrderAdapterView(Context ordercontext, List<Order> orderList, OnRatingListener onRateListener)
    {
        this.ordercontext = ordercontext;
        this.orderList = orderList;
        this.onRateListener = onRateListener;
    }



    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(ordercontext);
        View view = layoutInflater.inflate(R.layout.row_order, viewGroup, false);
        return new OrderViewHolder(view, onRateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, int i)
    {
        final Order o1 = orderList.get(i);
        orderViewHolder.orderprice.setText(String.valueOf(o1.getOrderPrice()));
        orderViewHolder.orderstatus.setText(o1.getOrderStatus());
        orderViewHolder.productowner.setText(o1.getPaintingOwner());



        if (o1.getPaintingOwner().equals(SharedPrefManager.getInstance(ordercontext).getUserName()))
        {

            orderViewHolder.paintingBoughtSold.setText("SOLD");
        }
        else
        {
            orderViewHolder.paintingBoughtSold.setText("BOUGHT");
        }

        orderViewHolder.orderimg.setImageUrl(o1.getOrderImage(), MySingleton.getInstance(ordercontext).getImageLoader());

        orderViewHolder.feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(Intent.EXTRA_EMAIL, new String[] {"info@paintingsonline.com.au"});
                emailintent.putExtra(Intent.EXTRA_SUBJECT, "Order Reference Number: " + String.valueOf(o1.getOrderId()));
                emailintent.putExtra(Intent.EXTRA_TEXT, "To Whom it may concern, I bought this Painting from " + o1.getPaintingOwner() + ".");
                v.getContext().startActivity(emailintent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }






    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        RatingBar ratingBar;
        NetworkImageView orderimg;
        TextView orderstatus, orderprice, productowner, paintingBoughtSold;
        Button feedback, rate;
        OnRatingListener monRatingListener;

        public OrderViewHolder(@NonNull View itemView, OnRatingListener onRatingListener)
        {
            super(itemView);

            paintingBoughtSold = itemView.findViewById(R.id.paintingbuyingmethod);
            orderimg = itemView.findViewById(R.id.orderimg);
            productowner = itemView.findViewById(R.id.showowner);
            orderprice = itemView.findViewById(R.id.showPrice);
            orderstatus = itemView.findViewById(R.id.showstatus);
            feedback = itemView.findViewById(R.id.feedback);
            rate = itemView.findViewById(R.id.rating);
            this.monRatingListener = onRatingListener;

            rate.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            monRatingListener.ratingSystem(userRatings, getAdapterPosition());
        }
    }

    public interface OnRatingListener
    {
        void ratingSystem(List<UserRating> userRatings, int position);
    }
}
