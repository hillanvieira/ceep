package br.com.alura.ceep.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final int delay = sharedPref.getInt("delaySplashScreen", 2000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goTomainActivity(editor, delay);
            }
        }, delay);

    }

    private void goTomainActivity(SharedPreferences.Editor editor, int delay) {

        if(delay == 2000) {
            editor.putInt("delaySplashScreen", 500);
            editor.commit();
        }

        Intent mainScreen =
                new Intent(this,
                        ListaNotasActivity.class);
        startActivity(mainScreen);

    }

}
