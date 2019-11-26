package com.example.paintingsonline.Model;

public class Category
{
    private int id;
    private String categoryName;
    private String imageURL;


    public Category()
    {
    }

    public Category(int id, String categoryName)
    {
        this.id = id;
        this.categoryName = categoryName;

    }

    public Category(int id, String categoryName, String imageURL)
    {
        this.categoryName = categoryName;
        this.imageURL = imageURL;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
