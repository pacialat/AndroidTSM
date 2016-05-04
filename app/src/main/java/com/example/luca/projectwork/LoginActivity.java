package com.example.luca.projectwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity implements Dati.MyResponse{

    Dati dati;
    EditText username, password;
    String pw;
    ProgressDialog pg;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        username.setText("");
        moveTaskToBack(true);
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Jesse");

        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        username = (EditText)findViewById(R.id.username);
        username.setText("");
        password = (EditText)findViewById(R.id.password);

        pg = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        pg.setMessage("Autenticazione in corso...");



        Button login = (Button)findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pg.show();
                String user  = "" + username.getText();
                String userNoSpace = user.replaceAll("\\s", "");

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
                dati.richiestaLogin("" + userNoSpace, base64, getApplicationContext());
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
    public void notifyShopsData(boolean success, boolean successData, JSONObject data) {

    }

    @Override
    public void notifyDetails(boolean success, boolean successData, JSONObject data) {

    }
}
