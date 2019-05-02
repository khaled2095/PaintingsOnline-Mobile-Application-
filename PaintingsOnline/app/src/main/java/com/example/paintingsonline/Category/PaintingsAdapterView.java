package com.example.paintingsonline.Category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class PaintingsAdapterView extends RecyclerView.Adapter<PaintingsAdapterView.PaintingHolder>
{
    private Context context;
    private List<Paintings> plist;
    private OnPaintListener monPaintListener;

    public PaintingsAdapterView(Context context, List<Paintings> plist, OnPaintListener onPaintListener)
    {
        this.context = context;
        this.plist = plist;
        this.monPaintListener = onPaintListener;
    }

    @NonNull
    @Override
    public PaintingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_painting_item, viewGroup, false);
        return new PaintingHolder(view, monPaintListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaintingHolder paintingHolder, final int i)
    {
        final Paintings p2 = plist.get(i);
        paintingHolder.nameText.setText(p2.getName());
        paintingHolder.priceText.setText(String.valueOf(p2.getPrice()));
        paintingHolder.descriptionText.setText(p2.getDescription());

        Glide.with(context).load(p2.getImage()).into(paintingHolder.i2);



        paintingHolder.cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                //Add to SQLite Room Persistance
                //Create New Checkout Item
                try {

                    Cart cart = new Cart();
                    cart.paintingid = p2.getId();
                    cart.paintingimg = p2.getImage();
                    cart.paintingname = p2.getName();
                    cart.price = p2.getPrice();
                    cart.qty = 1;

                    CartDatabase cartd = CartDatabase.getInstance(context);
                    CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
                    cartRepository.insertToCart(cart);

                    Log.d("cart", new Gson().toJson(cart));
                    Toast.makeText(context, "Saved Item to Checkout", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount()
    {
        return plist.size();
    }

    /*View Holder*/
    class PaintingHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView i2;
        TextView descriptionText, priceText, nameText;
        Button cartbtn;
        NotificationBadge nb;
        OnPaintListener onPaintListener;

        public PaintingHolder(@NonNull View itemView, OnPaintListener onPaintListener)
        {
            super(itemView);
            i2 = itemView.findViewById(R.id.imgpaintings2);
            nameText = itemView.findViewById(R.id.painting_Name2);
            priceText = itemView.findViewById(R.id.price2);
            cartbtn = itemView.findViewById(R.id.cartbtn);
            descriptionText = itemView.findViewById(R.id.paintDesc);
            cartbtn = itemView.findViewById(R.id.cartbtn);
            this.onPaintListener = onPaintListener;

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view)
        {
            onPaintListener.onPaintClick(getAdapterPosition());
        }
    }

    public interface OnPaintListener
    {
        void onPaintClick(int pos);
    }

}
