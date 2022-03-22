package com.mumo.newsapp;

import com.mumo.newsapp.models.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * This class is used to initialize the local box storage for this app
 * It contains two methods, one called when the Application launches to 'init' the Box
 * the other method used across the App to access and use the Box
 * Setup and usage examples can be found on the link: https://github.com/objectbox/objectbox-java
 */
public class ObjectBox {

    private static BoxStore boxStore;

    static void init(App context){
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get(){
        return boxStore;
    }
}
