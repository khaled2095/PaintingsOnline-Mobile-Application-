package com.example.paintingsonline.Database.Local;

import com.example.paintingsonline.Database.DataSource.ICartDataSource;
import com.example.paintingsonline.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource
{
    private CartDAO cartDAO;
    private static CartDataSource instance;


    public CartDataSource(CartDAO cartDAO)
    {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO)
    {
        if (instance == null)
        {
            instance = new CartDataSource(cartDAO);
        }

        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDAO.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int CountCartItems() {
        return cartDAO.CountCartItems();
    }

    @Override
    public int sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public void emptycart() {
        cartDAO.emptycart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }
}
