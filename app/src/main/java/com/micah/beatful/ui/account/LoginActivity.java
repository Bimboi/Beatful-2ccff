package com.micah.beatful.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.micah.beatful.HostActivity;
import com.micah.beatful.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText memail, mpassword;
    Button mlogin;
    TextView mcreateText;
    ProgressBar progressBar2;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        progressBar2 = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mlogin = findViewById(R.id.login);
        mcreateText = findViewById(R.id.createText);

        mlogin.setOnClickListener(
                view -> {
                    String email = memail.getText().toString().trim();
                    String password = mpassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        memail.setError("Email is Required.");
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        mpassword.setError("Password is Required.");
                        return;
                    }

                    if (password.length() < 6) {
                        mpassword.setError("Password must be >= 6 Characters");
                        return;
                    }

                    progressBar2.setVisibility(View.VISIBLE);

                    //authenticate the user

                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            String userID = "beatful-user";
                            String email1 = "undefined-email";

                            if (user != null) {
                                userID = user.getUid();
                                email1 = user.getEmail();
                            }

                            SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putString("userID", userID);
                            edit.putString("email", email1);
                            edit.apply();
                            startActivity(new Intent(getApplicationContext(), HostActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error!" +
                                    Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    });
                }
        );

        mcreateText.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class))
        );
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
