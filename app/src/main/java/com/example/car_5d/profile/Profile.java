package com.example.car_5d.profile;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Profile {
    private String name;
    private int imageView;

    public Profile(String name, int imageView) {
        this.name = name;
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public int getImageView() {
        return imageView;
    }
}
