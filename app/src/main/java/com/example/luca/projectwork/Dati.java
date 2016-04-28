package com.example.luca.projectwork;

import android.util.Log;

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
        public void notifyLogin(boolean success, boolean successData, String session);
        public void  notifyShopsData(boolean success, JSONObject data);
        public void  notifyDetails(boolean success, JSONObject data);
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



    //fa la richiesta per il login
    public void richiestaLogin(String utente, String password){
        RequestParams params = new RequestParams();
        params.add("email", utente);
        params.add("password", password);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post("http://its-bitrace.herokuapp.com/api/public/v2/login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    Singleton.SESSION = response.getJSONObject("data").getString("session");
                    mListener.notifyLogin(true, response.getBoolean("success"), Singleton.SESSION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                mListener.notifyLogin(false, false, null);
                Log.d("FAIL", "FAILURE");
                error.printStackTrace();

            }
        });
    }

    // fa la richiesta per ricevere la lista dei negozi
    public void richiestaListaNegozi(){
        RequestParams params = new RequestParams();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("x-bitrace-session", Singleton.SESSION);
        asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    Singleton.LISTA_NEGOZI = response;
                    mListener.notifyShopsData(true, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FAIL", "FAILURE");
                mListener.notifyShopsData(false, null);
                error.printStackTrace();

            }
        });
    }

    //fa la richiesta per ricevere i dettagli di un negozio
    public void richiestaDettagli(String GUID){
        RequestParams params = new RequestParams();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("x-bitrace-session", Singleton.SESSION);
        asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores/" + GUID, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    mListener.notifyDetails(true, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("FAIL", "FAILURE");
                mListener.notifyDetails(false, null);
                error.printStackTrace();

            }
        });
    }


}
