package com.example.paintingsonline.Home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomePaintingsAdapter extends RecyclerView.Adapter<HomePaintingsAdapter.PaintingViewHolder> implements Filterable
{
    private Context mctx;
    private List<Paintings> paintingsList;
    private List<Paintings> paintingsListFull;
    private HomeActivity mParentClass;


    public HomePaintingsAdapter(Context mctx, List<Paintings> paintingsList, HomeActivity pclass)
    {
        this.mctx = mctx;
        this.paintingsList = paintingsList;
        paintingsListFull = new ArrayList<>(paintingsList);
        this.mParentClass = pclass;
    }


    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mctx);
        View view = layoutInflater.inflate(R.layout.row_home_item, viewGroup, false);
        return new PaintingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder paintingViewHolder, int i)
    {
        final Paintings paintings = paintingsList.get(i);
        paintingViewHolder.paintingName.setText(paintings.getName());
        paintingViewHolder.paintingSize.setText("Size " + paintings.getpSize());
        paintingViewHolder.paintingDesc.setText(paintings.getDescription());
        paintingViewHolder.owner.setText(paintings.getpOwner());

        paintingViewHolder.i1.setImageUrl(paintings.getImage(), MySingleton.getInstance(mctx).getImageLoader());


        if(paintings.getQuantity() < 1)
        {
            paintingViewHolder.cartb.setText("OUT OF STOCK");
            paintingViewHolder.cartb.setEnabled(false);
        }
        else
        {
            paintingViewHolder.cartb.setEnabled(true);
            paintingViewHolder.cartb.setText("$ " + String.valueOf(paintings.getPrice()));
            paintingViewHolder.cartb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    //Add to SQLite Room Persistance
                    //Create New Checkout Item
                    try {

                        Cart cart = new Cart();
                        cart.paintingid = paintings.getId();
                        cart.paintingimg = paintings.getImage();
                        cart.paintingname = paintings.getName();
                        cart.stock = paintings.getQuantity();
                        cart.price = paintings.getPrice();
                        cart.qty = 1;

                        CartDatabase cartd = CartDatabase.getInstance(mctx);
                        CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

                        //if there is no count of an identical painting
                        if (cartRepository.checkIfPaintingExists(paintings.getId()) == 0) {
                            cartRepository.insertToCart(cart);
                            Toast.makeText(mctx, "Saved Item to Checkout", Toast.LENGTH_SHORT).show();
                            mParentClass.updateCart();
                        } else {
                            Toast.makeText(mctx, "Item already in the cart", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("cart", new Gson().toJson(cart));

                        // onPaintListener.onPaintClick();

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(mctx, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    @Override
    public int getItemCount()
    {
        return paintingsList.size();
    }

    @Override
    public Filter getFilter() {
        return paintingFilter;
    }


    private Filter paintingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Paintings> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(paintingsListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Paintings p : paintingsListFull)
                {
                    if (p.getName().toLowerCase().contains(filterPattern) || p.getDescription().toLowerCase().contains(filterPattern) || p.getpOwner().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(p);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            paintingsList.clear();
            paintingsList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };



    /*View Holder*/
    class PaintingViewHolder extends RecyclerView.ViewHolder
    {

        NetworkImageView i1;
        TextView paintingName, paintingSize, paintingDesc, owner;
        Button cartb;

        public PaintingViewHolder(@NonNull View itemView)
        {
            super(itemView);

            i1 = itemView.findViewById(R.id.imgpaintings);
            paintingName = itemView.findViewById(R.id.painting_Name);
            paintingSize = itemView.findViewById(R.id.painting_Size);
            paintingDesc = itemView.findViewById(R.id.paintdescription);
            owner = itemView.findViewById(R.id.owner);
            cartb = itemView.findViewById(R.id.c);
        }

    }
}
