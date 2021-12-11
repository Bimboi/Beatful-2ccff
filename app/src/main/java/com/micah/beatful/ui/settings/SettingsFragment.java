package com.micah.beatful.ui.settings;

import android.annotation.SuppressLint;
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
    Button btnApply;
    TextView txtCaption1;
    View myLayout1Vertical;
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
            setDefaultTheme();
            Toast.makeText(requireContext(), "Using Default Preference", Toast.LENGTH_LONG).show();
        }

        btnGreen = root.findViewById(R.id.btnGreen);
        btnGreen.setOnClickListener(view -> setDefaultTheme());
        btnBlue = root.findViewById(R.id.btnBlue);
        btnBlue.setOnClickListener(view -> setDiffTheme());
        btnApply = root.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(view -> requireActivity().recreate());
        return root;
    }

    public void setDefaultTheme() {
        myEditor = mySharedPreferences.edit();
        myEditor.putInt("backColor", Color.parseColor("#FDA376CA")); // purple_500
        myEditor.putString("textStyle", "normal");
        myEditor.apply();
        applySavedPreferences();
    }

    public void setDiffTheme() {
        myEditor = mySharedPreferences.edit();
        myEditor.putInt("backColor", Color.parseColor("#069386")); // teal_500
        myEditor.putString("textStyle", "italic");
        myEditor.apply();
        applySavedPreferences();
    }

    @Override
    public void onPause() {
        myEditor = mySharedPreferences.edit();
        myEditor.putString("DateLastExecution", String.valueOf(new Date()));
        myEditor.apply();
        super.onPause();
    }

    public void applySavedPreferences() {
        int backColor = mySharedPreferences.getInt("backColor", Color.parseColor("#FDA376CA"));
        String textStyle = mySharedPreferences.getString("textStyle", "normal");
        String msg = "Color Value: " + backColor + "\n" + "Style: " + textStyle;
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();

        txtCaption1.setBackgroundColor(backColor);
        txtCaption1.setTextSize(16);
        if (Objects.requireNonNull(textStyle).compareTo("normal") == 0) {
            txtCaption1.setTypeface(Typeface.SERIF, Typeface.NORMAL);
            txtCaption1.setTextColor(Color.WHITE);
        } else {
            txtCaption1.setTypeface(Typeface.SERIF, Typeface.ITALIC);
            txtCaption1.setTextColor(Color.BLACK);
        }
    }
}
