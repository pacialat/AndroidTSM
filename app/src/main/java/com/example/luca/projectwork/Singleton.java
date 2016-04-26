package com.example.luca.projectwork;

/**
 * Created by Luca on 23/04/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    static String SESSION;
    static String GUID_PRODUCT;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
