package com.micah.beatful;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.micah.beatful.ui.account.SignOutFragment;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class HostActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("MyPreferences_001", MODE_PRIVATE);
        String bgColor = "#FDA376CA";
        String textColor = "#FFFFFFFF";
        Drawable drawable = getDrawable(R.drawable.side_nav_bar);
        if (Objects.equals(prefs.getString("textStyle", "normal"), "normal")) {
            setTheme(R.style.Theme_BeatfulPlayer_NoActionBar);
        } else {
            setTheme(R.style.Theme_BeatfulPlayer_ButDiff_NoActionBar);
            bgColor = "#069386";
            textColor = "#FF000000";
            drawable = getDrawable(R.drawable.side_nav_bar_diff);
        }

        int bgValColor = Color.parseColor(bgColor);
        int textValColor = Color.parseColor(textColor);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(bgValColor);
        toolbar.setTitleTextColor(textValColor);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView textUserID = headerView.findViewById(R.id.textHeaderID);
        TextView textEmail = headerView.findViewById(R.id.textHeaderEmail);
        headerView.setBackground(drawable);

        prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
        String id = "User ID: " + prefs.getString("userID", "");
        textUserID.setText(id);
        textUserID.setTextColor(textValColor);
        textEmail.setText(prefs.getString("email", ""));
        textEmail.setTextColor(textValColor);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_sign_out).setOnMenuItemClickListener(menuItem -> {
            new SignOutFragment().show(getSupportFragmentManager(), "Sign Out Dialog");
            return true;
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.host, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}