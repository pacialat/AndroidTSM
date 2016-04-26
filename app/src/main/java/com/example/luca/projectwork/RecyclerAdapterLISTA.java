package com.example.luca.projectwork;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Luca on 24/04/2016.
 */
public class RecyclerAdapterLISTA extends  RecyclerView.Adapter<RecyclerViewHolderLISTA>{
    
    Map<Integer, ArrayList<String>> mappa;

    Context context;
    LayoutInflater inflater;
    JSONObject risposta;


    public RecyclerAdapterLISTA(Context context, JSONObject risposta) {
        this.risposta = risposta;
        mappa = new TreeMap<>();

        try {
            risposta();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.context = context;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public RecyclerViewHolderLISTA onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.item_list_lista, parent, false);

        RecyclerViewHolderLISTA viewHolder=new RecyclerViewHolderLISTA(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderLISTA holder, int position) {
        holder.titolo.setText(mappa.get(position).get(0));
        holder.indirizzo.setText(mappa.get(position).get(1));
        holder.telefono.setText(mappa.get(position).get(2));
       //holder.imageView.setOnClickListener(clickListener);
        //holder.imageView.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            RecyclerViewHolderLISTA vholder = (RecyclerViewHolderLISTA) v.getTag();
            int position = vholder.getPosition();

            Toast.makeText(context, "This is position " + position, Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int getItemCount() {
        return mappa.size();
    }

    private void risposta() throws JSONException {
        int lunghezza = risposta.getJSONArray("data").length();
        final JSONArray pezzo = risposta.getJSONArray("data");
        for (int i = 0; i < lunghezza; i++){
            ArrayList<String> obj = new ArrayList<>();
            obj.add(pezzo.getJSONObject(i).getString("name"));
            obj.add(pezzo.getJSONObject(i).getString("address"));
            obj.add(pezzo.getJSONObject(i).getString("phone"));
            mappa.put(i, obj);
        }
    }
}