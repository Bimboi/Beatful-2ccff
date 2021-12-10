package com.micah.beatful.ui.extras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.micah.beatful.HostActivity;
import com.micah.beatful.R;
import com.micah.beatful.ui.account.LoginActivity;

//import java.util.Objects;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        String username = prefs.getString("userID", null);
        String email = prefs.getString("email", null);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, LoginActivity.class);

            if (username != null && email != null) {
                i = new Intent(Splash.this, HostActivity.class);
            }

            startActivity(i);
            // close this activity

            finish();

        },3*1000);
    }
}
