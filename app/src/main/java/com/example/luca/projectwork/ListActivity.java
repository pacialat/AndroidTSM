package com.example.luca.projectwork;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends AppCompatActivity
        implements Dati.MyResponse, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    Dati dati;
    JSONObject listaNegozi;
    RecyclerAdapterLISTA adapter;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pg = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        pg.setMessage("Caricamento dati...");
        pg.show();

        try {
            Snackbar.make(findViewById(android.R.id.content), "Benvenuto " + Singleton.LOGIN_RESPONSE.getJSONObject("data").getString("name"), Snackbar.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//inizio listActivity
        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (Singleton.LISTA_NEGOZI == null){
            dati.richiestaListaNegozi();
        } else {
            pg.cancel();
            showList(Singleton.LISTA_NEGOZI);
        }

        listaNegozi = Singleton.LISTA_NEGOZI;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchActivity(String guid) {
        Bundle b = new Bundle();
        b.putString("ID", guid);
        Intent intent = new Intent(ListActivity.this, DetailActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void showList(JSONObject data){
        adapter = new RecyclerAdapterLISTA(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyLogin(boolean success, boolean successData, String session) {

    }

    @Override
    public void notifyShopsData(boolean success, JSONObject data) {
        pg.cancel();
        listaNegozi = data;
        showList(data);
    }

    @Override
    public void notifyDetails(boolean success, JSONObject data) {
        //non usato
    }
}
