package com.burakekmen.ornekuygulama.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.burakekmen.ornekuygulama.R;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout splash_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        splash_layout = findViewById(R.id.activity_splash_layout);

        if(isOnline()) {
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        }else
        {
            Snackbar snackBar = Snackbar.make(splash_layout, "İnternet Bağlantınızı Kontrol Ediniz!", Snackbar.LENGTH_LONG);
            snackBar.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
