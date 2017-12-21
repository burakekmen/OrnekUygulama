package com.burakekmen.ornekuygulama.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.burakekmen.ornekuygulama.R;

/****************************
 * Created by Burak EKMEN   |
 * 20.12.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class RcListViewHolder extends RecyclerView.ViewHolder {

    public ImageView resim=null;

    public RcListViewHolder(View itemView) {
        super(itemView);

        resim = itemView.findViewById(R.id.list_item_resim);
    }
}
