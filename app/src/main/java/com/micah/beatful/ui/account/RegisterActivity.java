package com.micah.beatful.ui.account;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.micah.beatful.HostActivity;
import com.micah.beatful.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText mfullname, memail, mpassword, mnumber;
    Button mregister;
    TextView mcreateText;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mfullname = findViewById(R.id.full_name_text);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password);
        mnumber = findViewById(R.id.numbertext);
        mregister = findViewById(R.id.login);
        mcreateText = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HostActivity.class));
            finish();
        }

        mregister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fullname = mfullname.getText().toString().trim();
                        String email = memail.getText().toString().trim();
                        String password = mpassword.getText().toString().trim();

                        if (TextUtils.isEmpty(fullname)) {
                            memail.setError("Name is Required.");
                            return;
                        }

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

                        progressBar.setVisibility(View.VISIBLE);

                        // register the user in firebase

                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "User is Created.", Toast.LENGTH_SHORT).show();
                                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                                FirebaseUser user = fAuth.getCurrentUser();
                                                String userID = "beatful-user";
                                                String email1 = "undefined-email";

                                                FirebaseUser user1 = fAuth.getCurrentUser();
                                                if (user1 != null) {
                                                    userID = user1.getUid();
                                                    email1 = user1.getEmail();
                                                }

                                                SharedPreferences prefs = getSharedPreferences("Credentials", MODE_PRIVATE);
                                                SharedPreferences.Editor edit = prefs.edit();
                                                edit.putString("userID", userID);
                                                edit.putString("email", email1);
                                                edit.apply();
                                                startActivity(new Intent(getApplicationContext(), HostActivity.class));
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Error Login!" +
                                                        Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error Register!" +
                                            Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }
        );

        mcreateText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }
        );
    }
}
