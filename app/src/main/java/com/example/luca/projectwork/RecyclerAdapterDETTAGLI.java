package com.example.luca.projectwork;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class RecyclerAdapterDETTAGLI extends  RecyclerView.Adapter<RecyclerViewHolderDETTAGLI> {

    Map<Integer, String> name = new TreeMap<>();
    JSONObject data;
    Context context;
    LayoutInflater inflater;
    ArrayList<String> listaDescrizioni;

    public RecyclerAdapterDETTAGLI(Context context, JSONObject obj) {
        listaDescrizioni = new ArrayList<>();
        data = obj;
        try {
            risposta();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerViewHolderDETTAGLI onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list_dettagli, parent, false);

        RecyclerViewHolderDETTAGLI viewHolder=new RecyclerViewHolderDETTAGLI(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderDETTAGLI holder, int position) {

        holder.tv1.setText(listaDescrizioni.get(position));
        holder.tv2.setText(name.get(position));
        holder.imageView.setOnClickListener(clickListener);
        holder.imageView.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewHolderDETTAGLI vholder = (RecyclerViewHolderDETTAGLI) v.getTag();
            int position = vholder.getPosition();

            Toast.makeText(context,"This is position "+position,Toast.LENGTH_LONG ).show();

        }
    };

    @Override
    public int getItemCount() {
        return name.size();
    }

    private void riempiDescizioni(){
        listaDescrizioni.add("Nome");
        listaDescrizioni.add("Descrizione");
        listaDescrizioni.add("Indirizzo");
        listaDescrizioni.add("Telefono");
        //listaDescrizioni.add("Telefono");

    }

    public void risposta() throws JSONException {
        riempiDescizioni();
        int i = 0;
        name.put(i, data.getJSONObject("data").getString("name"));
        i += 1;
        name.put(i, data.getJSONObject("data").getString("description"));
        i += 1;
        name.put(i, data.getJSONObject("data").getString("address"));
        i += 1;
        name.put(i, data.getJSONObject("data").getString("phone"));
    }
}