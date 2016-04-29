package com.example.luca.projectwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;


public class LoginActivity extends AppCompatActivity implements Dati.MyResponse{

    Dati dati;
    EditText username, password;
    String pw;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Jesse");


        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        pg = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        pg.setMessage("Autenticazione in corso...");



        Button login = (Button)findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.show();

                pw = "" + password.getText();

                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-512");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(pw.getBytes());
                byte byteData[] = md.digest();
                String base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
                dati.richiestaLogin("" + username.getText(), base64, getApplicationContext());
                Log.d("PASSWORD SHA", base64);
                //valori = "email="+email+"&password="+base64;
            }
        });
    }



    @Override
    public void notifyLogin(boolean success, boolean successData, String message) throws JSONException {
        pg.cancel();
        if (success){
            if (successData){
                Intent i = new Intent(LoginActivity.this, ListActivity.class);
                startActivity(i);
            } else {
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Errore del Server", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void notifyShopsData(boolean success, JSONObject data) {

    }

    @Override
    public void notifyDetails(boolean success, JSONObject data) {

    }
}
