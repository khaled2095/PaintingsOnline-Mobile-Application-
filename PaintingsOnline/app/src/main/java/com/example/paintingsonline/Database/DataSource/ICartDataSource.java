package com.example.paintingsonline.Database.DataSource;

import com.example.paintingsonline.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource
{
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int CountCartItems();
    String checkIfPaintingExists(String paintingId);
    int sumPrice();
    void emptycart();
    void updatePaintingName(String pname, String pID);
    void updatePaintingSizeAndPrice(String pSize, int pPrice, String pID);
    void updatePaintingImage(String pImage, String pID);
    void updatePaintingStock(int pStock, String pID);
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartItem(Cart cart);
}
