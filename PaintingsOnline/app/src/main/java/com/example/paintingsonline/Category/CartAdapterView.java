package com.example.paintingsonline.Category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.R;

import java.util.List;

public class CartAdapterView extends RecyclerView.Adapter<CartAdapterView.cartViewHolder>
{

    private Context mctx;
    private List<Cart> carts;
    private OnCartListener monCartListener;

    public CartAdapterView(Context mctx, List<Cart> c1, OnCartListener onCartListener)
    {
        this.mctx = mctx;
        this.carts = c1;
        this.monCartListener = onCartListener;
    }


    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mctx);
        View view = layoutInflater.inflate(R.layout.row_cart_item, viewGroup, false);
        return new cartViewHolder(view, monCartListener);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder cartViewHolder, final int i)
    {
        final Cart c = carts.get(i);

        cartViewHolder.title.setText(c.paintingname);
        cartViewHolder.price.setText(new StringBuilder("$").append(c.price));
        //cartViewHolder.id.setText("ID: " + String.valueOf(c.id));
        //cartViewHolder.pid.setText("Painting ID: " + String.valueOf(c.paintingid));
        cartViewHolder.qty.setNumber(String.valueOf(c.qty));



        cartViewHolder.qty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = carts.get(i);

                if (newValue > oldValue){
                    if (c.stock > cart.qty) {
                        //view or ElegantNumberButton will automatically update to the newValue number in the view
                        cart.qty = newValue;
                        CartDatabase cartd = CartDatabase.getInstance(mctx);
                        CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
                        cartRepository.updateCart(cart);
                    } else {
                        //Otherwise, we stop that auto update if out of stock
                        view.setNumber(String.valueOf(oldValue));
                        Toast.makeText(mctx, "This Painting is out of Stock", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (cart.qty > 1) {
                        //view or ElegantNumberButton will automatically update to the newValue number in the view
                        cart.qty = newValue;
                        CartDatabase cartd = CartDatabase.getInstance(mctx);
                        CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
                        cartRepository.updateCart(cart);
                    } else {
                        //Otherwise, we stop that auto update if out of stock
                        view.setNumber(String.valueOf(oldValue));
                        Toast.makeText(mctx, "This Painting is out of Stock", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        Glide.with(mctx).load(c.paintingimg).into(cartViewHolder.productimg);

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView productimg;
        TextView title, price, id, pid;
        ElegantNumberButton qty;
        Button removebtn;
        OnCartListener onCartListener;

        public cartViewHolder(@NonNull View itemView, OnCartListener onCartListener)
        {
            super(itemView);
            productimg = itemView.findViewById(R.id.productimg);
            title = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.productprice);
            qty = itemView.findViewById(R.id.qty);
            //id = itemView.findViewById(R.id.productid);
            //pid = itemView.findViewById(R.id.pid);
            removebtn = itemView.findViewById(R.id.remove);
            this.onCartListener = onCartListener;

            // itemView.setOnClickListener(this);
            removebtn.setOnClickListener(this);


        }

        @Override
        public void onClick(View view)
        {
            onCartListener.OnDeleteClick(getAdapterPosition());
        }
    }

    public interface OnCartListener
    {
        void OnDeleteClick(int pos);
    }

}
