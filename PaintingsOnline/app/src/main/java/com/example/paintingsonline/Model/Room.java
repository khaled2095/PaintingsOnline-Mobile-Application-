package com.example.paintingsonline.Model;

public class Room
{
    private int roomid;
    private String roomName;
    private String roomimageURL;


    public Room(int roomid, String roomName, String roomimageURL)
    {
        this.roomid = roomid;
        this.roomName = roomName;
        this.roomimageURL = roomimageURL;
    }


    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomimageURL() {
        return roomimageURL;
    }

    public void setRoomimageURL(String roomimageURL) {
        this.roomimageURL = roomimageURL;
    }


    @Override
    public String toString()
    {
        return roomName;
    }
}
