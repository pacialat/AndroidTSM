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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements Dati.MyResponse{

    static final String OUTPUT = "output";
    String email, password, valori;
    Dati dati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();
        final EditText editEmail = (EditText)findViewById(R.id.editEmail);
        final EditText editPassword = (EditText)findViewById(R.id.editPassword);
        Button btnInvia = (Button)findViewById(R.id.btnInvia);
        btnInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editEmail != null ){
                    email = editEmail.getText().toString();
                    Log.i(OUTPUT, "email : " +email);
                    if (editPassword != null) {
                        password = editPassword.getText().toString();
                        Log.i(OUTPUT, "password : " +password);

                        try {
                            Log.i(OUTPUT, "entro nel try");
                            MessageDigest md = null;
                            md = MessageDigest.getInstance("SHA-512");
                            md.update(password.getBytes());
                            byte byteData[] = md.digest();
                            String base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
                            valori = "email="+email+"&password="+base64;
                            Log.i(OUTPUT, valori);

                            dati.richiestaLogin(email, base64);
                        } catch (NoSuchAlgorithmException e) {
                        }
                    }
                }
            }
        });
    }

    @Override
    public void notifyLogin(boolean success, boolean successData, String session) {
        if (success){
            if (successData) {
                if (session != null) {
                    //Intent intent = new Intent(this, ListActivity.class);
                    Log.i(OUTPUT, "cambia activity");
                }
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