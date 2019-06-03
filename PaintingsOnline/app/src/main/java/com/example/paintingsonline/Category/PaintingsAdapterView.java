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

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.google.gson.Gson;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class PaintingsAdapterView extends RecyclerView.Adapter<PaintingsAdapterView.PaintingHolder>
{

    private Context context;
    private List<Paintings> plist;
    private List<Cart> carts;
    private PaintingActivity ParentClass;



    public PaintingsAdapterView(Context context, List<Paintings> plist, PaintingActivity parentclass)
    {
        this.context = context;
        this.plist = plist;
        this.ParentClass = parentclass;
    }

    @NonNull
    @Override
    public PaintingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_painting_item, viewGroup, false);
        return new PaintingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaintingHolder paintingHolder, final int i)
    {
        final Paintings p2 = plist.get(i);
        paintingHolder.nameText.setText(p2.getName());
        paintingHolder.paintingSize.setText("Size " + p2.getpSize());
        paintingHolder.descriptionText.setText(p2.getDescription());
        paintingHolder.paintOwner.setText(p2.getpOwner());

        paintingHolder.i2.setImageUrl(p2.getImage(), MySingleton.getInstance(context).getImageLoader());

        if(p2.getQuantity() < 1)
        {
            paintingHolder.cartbtn.setText("OUT OF STOCK");
            //paintingHolder.cartbtn.setTextColor(Color.parseColor("red"));
            paintingHolder.cartbtn.setEnabled(false);
        }
        else
        {
            paintingHolder.cartbtn.setEnabled(true);
            paintingHolder.cartbtn.setText("$ " + String.valueOf(p2.getPrice()));
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
                        cart.stock = p2.getQuantity();
                        cart.price = p2.getPrice();
                        cart.qty = 1;

                        CartDatabase cartd = CartDatabase.getInstance(context);
                        CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

                        //if there is no count of an identical painting
                        if (cartRepository.checkIfPaintingExists(p2.getId()) == 0)
                        {
                            cartRepository.insertToCart(cart);

                            Toast.makeText(context, "Saved Item to Checkout", Toast.LENGTH_SHORT).show();
                            ParentClass.updateCart();

                        } else {
                            Toast.makeText(context, "Item already in the cart", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("cart", new Gson().toJson(cart));

                        // onPaintListener.onPaintClick();


                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }



    }

    @Override
    public int getItemCount()
    {
        return plist.size();
    }

    /*View Holder*/
    class PaintingHolder extends RecyclerView.ViewHolder
    {
        NetworkImageView i2;
        TextView descriptionText, paintingSize, nameText, paintOwner;
        Button cartbtn;

        public PaintingHolder(@NonNull View itemView)
        {
            super(itemView);
            i2 = itemView.findViewById(R.id.imgpaintings2);
            nameText = itemView.findViewById(R.id.painting_Name2);
            paintingSize = itemView.findViewById(R.id.painting_Size2);
            cartbtn = itemView.findViewById(R.id.cartbtn);
            descriptionText = itemView.findViewById(R.id.paintDesc);
            paintOwner = itemView.findViewById(R.id.paintingowner);


        }

    }

}
