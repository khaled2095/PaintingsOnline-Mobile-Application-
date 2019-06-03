package com.example.paintingsonline.Model;

public class Room
{

    private int RoomID;
    private String RoomName;
    private String imageURL;


    public Room(int roomID, String roomName)
    {
        RoomID = roomID;
        RoomName = roomName;
    }

    public Room(int roomID, String roomName, String imageURL)
    {
        RoomID = roomID;
        RoomName = roomName;
        this.imageURL = imageURL;
    }


    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return RoomName;
    }
}
