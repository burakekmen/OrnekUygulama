package com.burakekmen.ornekuygulama.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.burakekmen.ornekuygulama.R;
import com.burakekmen.ornekuygulama.models.PhotoModel;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/****************************
 * Created by Burak EKMEN   |
 * 20.12.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class RcListAdapter extends RecyclerView.Adapter<RcListViewHolder> {

    private Context context = null;
    private ArrayList<PhotoModel> resimListesi = null;

    public RcListAdapter(Context context, ArrayList<PhotoModel> resimListesi){
        this.context = context;
        this.resimListesi = resimListesi;
    }

    @Override
    public RcListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RcListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RcListViewHolder holder, final int position) {
        final PhotoModel secilenResim = getItem(position);

        holder.resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())
                    resmiGoruntule(position);
                else
                    Toast.makeText(context, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.with(context).load(secilenResim.getUrl()).into(holder.resim);
    }

    private void resmiGoruntule(int position){
        final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.preview_image);
        PhotoView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);

        nagDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    nagDialog.dismiss();
                }
                return true;
            }
        });

        Window window = nagDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setBackgroundDrawableResource(R.color.resim_arkaplan);

        Picasso.with(context).load(resimListesi.get(position).getUrl()).into(ivPreview);
        nagDialog.show();
    }

    @Override
    public int getItemCount() {
        return resimListesi.size();
    }

    private PhotoModel getItem(int position){
        return resimListesi.get(position);
    }

    public void addAll(List<PhotoModel> yeniListe)
    {
        resimListesi.addAll(yeniListe);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
