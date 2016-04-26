package com.example.luca.projectwork;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Luca on 24/04/2016.
 */
public class RecyclerViewHolderLISTA extends RecyclerView.ViewHolder {

    TextView titolo, indirizzo, telefono;

    public RecyclerViewHolderLISTA(View itemView) {
        super(itemView);

        titolo = (TextView) itemView.findViewById(R.id.list_title);
        indirizzo = (TextView) itemView.findViewById(R.id.list_address);
        telefono = (TextView)itemView.findViewById(R.id.list_phone);

    }
}
