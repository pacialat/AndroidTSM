package com.example.luca.projectwork;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity implements Dati.MyResponse{
    RecyclerView recyclerView;
    Dati dati;
    RecyclerAdapterDETTAGLI adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        Bundle vBundle = getIntent().getExtras();
        Singleton.GUID_PRODUCT = vBundle.getString("ID");
        dati.richiestaDettagli(vBundle.getString("ID"));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    public void notifyShopsData(boolean success, JSONObject data) {
        //non usata in questa classe
    }

    @Override
    public void notifyDetails(boolean success, JSONObject data) {
        showRecycleView(data);
        try {
            Log.d("RISPOSTA DETTAGLI", "" + success + ", " + data.getJSONObject("data").getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
