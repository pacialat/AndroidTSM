package com.example.luca.projectwork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Luca on 20/04/2016.
 */
public class Dati {

    //interfaccia per notificare la risposta
    public interface MyResponse{
        public void notifyLogin(boolean success, boolean successData, String message) throws JSONException;
        public void  notifyShopsData(boolean success, boolean successData, JSONObject data);
        public void  notifyDetails(boolean success, boolean successData, JSONObject data);
    }

    private static Dati mInstance;
    MyResponse mListener;

    public static Dati getInstance(){
        if (mInstance == null){
            mInstance = new Dati();
        }
        return mInstance;
    }


    public void setListener(MyResponse listener){
        mListener = listener;
    }

    //controllo connessione internet
    private boolean isConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //fa la richiesta per il login
    public void richiestaLogin(String utente, String password, Context c){

        if (isConnected(c)){
            RequestParams params = new RequestParams();
            params.add("email", utente);
            params.add("password", password);

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.post("http://its-bitrace.herokuapp.com/api/public/v2/login", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject response = new JSONObject(new String(responseBody));
                        Singleton.LOGIN_RESPONSE = response;
                        if (response.getBoolean("success")){
                            Singleton.SESSION = response.getJSONObject("data").getString("session");
                            mListener.notifyLogin(true, response.getBoolean("success"), "");
                        }else{
                            mListener.notifyLogin(true, response.getBoolean("success"), response.getString("errorMessage"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {
                        mListener.notifyLogin(false, false, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("FAIL", "FAILURE");
                    error.printStackTrace();

                }
            });
        } else {
            try {
                mListener.notifyLogin(true, false, "No internet connection");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // fa la richiesta per ricevere la lista dei negozi
    public void richiestaListaNegozi(Context c){

        if (isConnected(c)){
            RequestParams params = new RequestParams();

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.addHeader("x-bitrace-session", Singleton.SESSION);
            asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        JSONObject response = new JSONObject(new String(responseBody));
                        Singleton.LISTA_NEGOZI = response;
                        if (response.getBoolean("success")){
                            mListener.notifyShopsData(true, response.getBoolean("success"), response);
                        }else{
                            mListener.notifyShopsData(true, response.getBoolean("success"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("FAIL", "FAILURE");
                    mListener.notifyShopsData(false, false, null);
                    error.printStackTrace();

                }
            });
        } else {
            mListener.notifyShopsData(false, false, null);

        }

    }

    //fa la richiesta per ricevere i dettagli di un negozio
    public void richiestaDettagli(String GUID, Context c){
        if (isConnected(c)){
            RequestParams params = new RequestParams();

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.addHeader("x-bitrace-session", Singleton.SESSION);
            asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores/" + GUID, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject response = new JSONObject(new String(responseBody));
                        if (response.getBoolean("success")){
                            mListener.notifyDetails(true, response.getBoolean("success"), response);
                        }else{
                            mListener.notifyDetails(true, response.getBoolean("success"), null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("FAIL", "FAILURE");
                    mListener.notifyDetails(false, false, null);
                    error.printStackTrace();

                }
            });
        } else {
                mListener.notifyDetails(false, false, null);

        }

    }


}
