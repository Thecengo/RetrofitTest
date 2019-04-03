package com.example.retrofittest;

import com.google.gson.annotations.SerializedName;

public class Post {
    private Integer id;
    private int userId;
    private String title;
    @SerializedName("body")
    private String Text;

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        Text = text;
    }

    public Integer getId() {
        return id;
    }



    public int getUserId() {
        return userId;
    }



    public String getTitle() {
        return title;
    }



    public String getText() {
        return Text;
    }


}
