package com.sajjadsjat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sajjadsjat.utils.H;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);

        EditText emailUsername = findViewById(R.id.et_email_username);
        EditText emailPassword = findViewById(R.id.et_email_password);

        emailUsername.setText(prefs.getString("email_username", ""));
        emailPassword.setText(prefs.getString("email_password", ""));

        emailUsername.addTextChangedListener(H.createAfterTextChanged(s -> {
            prefs.edit().putString("email_username", s.toString()).apply();
        }));

        emailPassword.addTextChangedListener(H.createAfterTextChanged(s -> {
            prefs.edit().putString("email_password", s.toString()).apply();
        }));
    }
}