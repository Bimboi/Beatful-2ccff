package com.micah.beatful.ui.account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.micah.beatful.R;
import com.micah.beatful.ui.extras.Splash;
import com.micah.beatful.ui.home.BeatPlayer;

import java.util.Objects;

public class SignOutFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);
        TextView textProceed = root.findViewById(R.id.textSignOutProceed);
        TextView textCancel = root.findViewById(R.id.textSignOutCancel);

        textProceed.setOnClickListener(view -> {
            resetPersonalData();
            stopMedia();
            startActivity(new Intent(requireContext(), Splash.class));
            dismiss();
        });
        textCancel.setOnClickListener(view -> dismiss());
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void resetPersonalData() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }
    }

    private void stopMedia () {
        MediaPlayer player = BeatPlayer.getMediaPlayer();
        try {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
