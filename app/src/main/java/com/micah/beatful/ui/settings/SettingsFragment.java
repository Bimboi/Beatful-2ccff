package com.micah.beatful.ui.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.micah.beatful.R;

import java.util.Date;
import java.util.Objects;

public class SettingsFragment extends Fragment {
    Button btnGreen;
    Button btnBlue;
    TextView txtCaption1;
    Boolean fancyPrefChosen = false;
    View myLayout1Vertical;
    final int mode = Activity.MODE_PRIVATE;
    final String MYPREFS = "MyPreferences_001";

    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor myEditor;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        myLayout1Vertical = root.findViewById(R.id.linLayout1Vertical);
        txtCaption1 = root.findViewById(R.id.txtCaption1);
        txtCaption1.setText("This is what the \n"
                + "applied theme looks like \n"
                + "after you choose your preference");

        mySharedPreferences = requireActivity().getSharedPreferences(MYPREFS, 0);

        if (mySharedPreferences != null && mySharedPreferences.contains("backColor")) {
            applySavedPreferences();
        } else {
            Toast.makeText(requireContext(), "No Preferences Found", Toast.LENGTH_LONG).show();
        }
        btnGreen = root.findViewById(R.id.btnGreen);
        btnGreen.setOnClickListener(this::onClick);
        btnBlue = root.findViewById(R.id.btnBlue);
        btnBlue.setOnClickListener(this::onClick);
        return root;
    }

    // conditional method call not recommended
    public void onClick(View view){
        myEditor = mySharedPreferences.edit();
        myEditor.clear();

        if(view.getId() == btnGreen.getId()){
            myEditor.putInt("backColor", Color.DKGRAY);
            myEditor.putInt("textSize", 16);
            myEditor.putString("textStyle", "bold");
            myEditor.putInt("layoutColor", Color.BLACK);
        }
        else{
            myEditor.putInt("backColor", Color.BLUE);
            myEditor.putInt("textSize", 20);
            myEditor.putString("textStyle", "italic");
            myEditor.putInt("layoutColor", Color.CYAN);
        }
        myEditor.apply();
        applySavedPreferences();
    }

    @Override
    public void onPause() {
        myEditor = mySharedPreferences.edit();
        myEditor.putString("DateLastExecution", new Date().toLocaleString());
        myEditor.apply();
        super.onPause();
    }

    public void applySavedPreferences(){
        int backColor = mySharedPreferences.getInt("backColor", Color.RED);
        int textSize = mySharedPreferences.getInt("textSize", 12);
        String textStyle = mySharedPreferences.getString("textStyle", "bold");
        int layoutColor = mySharedPreferences.getInt("layoutColor", Color.CYAN);
        String msg = "Color: " + backColor + "\n" + "Size: " + textSize + "\n" + "Style: " + textStyle;
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();

        txtCaption1.setBackgroundColor(backColor);
        txtCaption1.setTextSize(textSize);
        if(Objects.requireNonNull(textStyle).compareTo("bold")==0){
            txtCaption1.setTypeface(Typeface.SERIF, Typeface.BOLD);
        }
        else{
            txtCaption1.setTypeface(Typeface.SERIF, Typeface.ITALIC);
        }
        myLayout1Vertical.setBackgroundColor(layoutColor);
    }
}
