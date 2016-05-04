package com.example.luca.projectwork;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Dati.MyResponse {

    //JSONObject risposta;
    String id;

    Dati dati;

    private GoogleMap mMap;

    public void launchActivity(String guid) {
        Bundle b = new Bundle();
        b.putString("ID", guid);
        Log.d("GUID", guid);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Dati.getInstance().setListener(this);
        dati = Dati.getInstance();

        //dati.richiestaLogin("tsac-2015@tecnicosuperiorekennedy.it", "AkL6KhBcibHLVGZbs/JyBJqMCGB6nDLK/0ovxGZHojt6EepTxpdfygqKsIWz3Q4FS4wyHY4cIrP1W8nHAd8F4A==");

        //new SendHttpRequest().execute();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Log.d("ONINFOWINDOWCLICK", marker.getTitle());

                try {
                    launchActivity(getID(marker.getTitle()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);

        try {
            setMarker(mMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        /*mMap.addMarker(new MarkerOptions()
                .position(new LatLng(45.9482567, 12.6785415))
                .title("Hello world"));*/
    }


    private void setMarker(GoogleMap mMap) throws JSONException {
        JSONArray posizioni = Singleton.LISTA_NEGOZI.getJSONArray("data");
        int lunghezza = Singleton.LISTA_NEGOZI.getJSONArray("data").length();
        Log.d("MARKER", "" + lunghezza);
        double sommaLat = 0;
        double sommaLong = 0;

        for(int i = 0; i < lunghezza; i++){
            JSONObject obj = posizioni.getJSONObject(i);
            double latitude = Double.parseDouble(obj.getString("latitude"));
            double longitude = Double.parseDouble(obj.getString("longitude"));
            sommaLat += latitude;
            sommaLong += longitude;
            try {
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(obj.getString("name"))
                                .snippet(obj.getString("address"))
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sommaLat/lunghezza, sommaLong/lunghezza), 2));

    }

    public String getID(String title) throws JSONException {
        JSONArray array = Singleton.LISTA_NEGOZI.getJSONArray("data");
        for (int i = 0; i < array.length(); i++){
            if (array.getJSONObject(i).getString("name").equals(title)){
                return array.getJSONObject(i).getString("guid");
            }
        }
        return null;
    }


    @Override
    public void notifyLogin(boolean success, boolean successData, String session) {
        /*Log.d("RISPOSTALOGIN", "" + success + " sux: " + successData + " " + session);
        dati.richiestaListaNegozi();*/
    }

    @Override
    public void notifyShopsData(boolean success, boolean successData, JSONObject data) {
        /*Singleton.LISTA_NEGOZI = data;
        try {
            setMarker(mMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("RISPOSTADATI", "" + success);*/
    }

    @Override
    public void notifyDetails(boolean success, boolean successData, JSONObject data) {
        //non usato in questa classe
    }
}
