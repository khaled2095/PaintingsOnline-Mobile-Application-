package com.example.paintingsonline.Database.DataSource;

import com.example.paintingsonline.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource
{

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource)
    {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource)
    {
        if (instance == null)
        {
            instance = new CartRepository(iCartDataSource);
        }

        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return iCartDataSource.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int CountCartItems() {
        return iCartDataSource.CountCartItems();
    }

    @Override
    public int sumPrice() {
        return iCartDataSource.sumPrice();
    }

    @Override
    public void emptycart() {
        iCartDataSource.emptycart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }
}
