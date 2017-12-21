package com.burakekmen.ornekuygulama.ui.activitys;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.burakekmen.ornekuygulama.R;
import com.burakekmen.ornekuygulama.adapters.UrlAdapter;
import com.burakekmen.ornekuygulama.ui.fragments.PhotosFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnFiltreSifirla= null;
    private LinearLayout activity_main_layout=null;

    private boolean geriTusunaCiftBasma = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init(){
        btnFiltreSifirla = findViewById(R.id.activity_main_btnFiltreSifirla);
        btnFiltreSifirla.setOnClickListener(this);

        activity_main_layout = findViewById(R.id.activity_main_layout);

        Snackbar snackBar = Snackbar.make(activity_main_layout, "Resimler Yükleniyor...", Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if(query.equals(""))
                query = null;

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlAdapter.PREF_SEARCH_QUERY, query)
                    .apply();

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.gallery_fragment);
            if (fragment != null) {
                ((PhotosFragment) fragment).yenile();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_btnFiltreSifirla:

                String query = PreferenceManager
                        .getDefaultSharedPreferences(this)
                        .getString(UrlAdapter.PREF_SEARCH_QUERY, null);

                if(query != null){
                    PreferenceManager.getDefaultSharedPreferences(this)
                            .edit()
                            .putString(UrlAdapter.PREF_SEARCH_QUERY, null)
                            .apply();

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.gallery_fragment);
                    if (fragment != null) {
                        ((PhotosFragment) fragment).yenile();
                    }

                    Snackbar snackBar = Snackbar.make(activity_main_layout, "Filtre Sıfırlama Başarılı!", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                }else
                {
                    Snackbar snackBar = Snackbar.make(activity_main_layout, "İlk Önce Arama Yapınız!", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!geriTusunaCiftBasma) {
            this.geriTusunaCiftBasma = true;

            Snackbar snackbar = Snackbar.make(activity_main_layout, "Çıkış için tekrar Geri tuşuna basınız!", Snackbar.LENGTH_SHORT);
            snackbar.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    geriTusunaCiftBasma = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }
}
