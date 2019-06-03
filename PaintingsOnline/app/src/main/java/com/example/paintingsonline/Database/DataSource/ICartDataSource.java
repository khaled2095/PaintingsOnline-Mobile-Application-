package com.example.paintingsonline.Database.DataSource;

import com.example.paintingsonline.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource
{
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int CountCartItems();
    int checkIfPaintingExists(int paintingId);
    int sumPrice();
    void emptycart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
