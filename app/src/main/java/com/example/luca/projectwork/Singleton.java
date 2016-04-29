package com.example.luca.projectwork;

import org.json.JSONObject;

/**
 * Created by Luca on 23/04/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    static String SESSION = null;
    static String GUID_PRODUCT = null;
    static JSONObject LISTA_NEGOZI = null;
    static JSONObject LOGIN_RESPONSE = null;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
