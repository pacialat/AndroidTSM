package com.example.luca.projectwork;

import org.json.JSONObject;

/**
 * Created by Luca on 23/04/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    static String SESSION;
    static String GUID_PRODUCT;
    static JSONObject LISTA_NEGOZI;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
