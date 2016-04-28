package com.example.luca.projectwork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends AppCompatActivity implements Dati.MyResponse{

    Dati dati;
    EditText username, password;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        Button login = (Button)findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dati.richiestaLogin("" + username.getText(), base64);
                Log.d("PASSWORD SHA", base64);
                //valori = "email="+email+"&password="+base64;
            }
        });
    }



    @Override
    public void notifyLogin(boolean success, boolean successData, String session) {
        Log.d("LOGIN", "success" + success + "data: " + successData);
        if (success){
            if (successData){
                Intent i = new Intent(LoginActivity.this, ListActivity.class);
                startActivity(i);
            }
        }
    }

    @Override
    public void notifyShopsData(boolean success, JSONObject data) {

    }

    @Override
    public void notifyDetails(boolean success, JSONObject data) {

    }
}
