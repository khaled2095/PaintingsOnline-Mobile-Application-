package com.example.paintingsonline.Order;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Order;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;

import java.util.List;

public class OrderAdapterView extends RecyclerView.Adapter<OrderAdapterView.OrderViewHolder>
{
    private Context ordercontext;
    private List<Order> orderList;


    public OrderAdapterView(Context ordercontext, List<Order> orderList)
    {
        this.ordercontext = ordercontext;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(ordercontext);
        View view = layoutInflater.inflate(R.layout.row_order, viewGroup, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i)
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




    class OrderViewHolder extends RecyclerView.ViewHolder
    {
        NetworkImageView orderimg;
        TextView orderstatus, orderprice, productowner, paintingBoughtSold, feedback;

        public OrderViewHolder(@NonNull View itemView)
        {
            super(itemView);

            paintingBoughtSold = itemView.findViewById(R.id.paintingbuyingmethod);
            orderimg = itemView.findViewById(R.id.orderimg);
            productowner = itemView.findViewById(R.id.showowner);
            orderprice = itemView.findViewById(R.id.showPrice);
            orderstatus = itemView.findViewById(R.id.showstatus);
            feedback = itemView.findViewById(R.id.feedback);
        }
    }
}
