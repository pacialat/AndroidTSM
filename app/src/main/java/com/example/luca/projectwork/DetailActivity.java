package com.example.luca.projectwork;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements Dati.MyResponse{
    RecyclerView recyclerView;
    Dati dati;
    RecyclerAdapterDETTAGLI adapter;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(Singleton.SESSION != null){
            pg = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            pg.setMessage("Caricamento dati...");
            pg.show();

            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            Dati.getInstance().setListener(this);
            dati = Dati.getInstance();

            Bundle vBundle = getIntent().getExtras();
            Singleton.GUID_PRODUCT = vBundle.getString("ID");
            dati.richiestaDettagli(vBundle.getString("ID"), getApplicationContext());
        } else{
            Log.d("FINISH DETAIL", "finish detail");
            finish();
        }


    }

    private void showRecycleView(JSONObject obj){
        adapter = new RecyclerAdapterDETTAGLI(this, obj);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyLogin(boolean success, boolean successData, String session) {
        //non usata in questa classe
    }

    @Override
    public void notifyShopsData(boolean success, boolean successData, JSONObject data) {
        //non usata in questa classe
    }

    @Override
    public void notifyDetails(boolean success, boolean successData, JSONObject data) {
        pg.cancel();
        if (success){
            if (successData){
                showRecycleView(data);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Errore nel caricamento dei dati", Snackbar.LENGTH_LONG).show();
            }
        }else{
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
        }

    }
}
