package com.sajjadsjat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.sajjadsjat.databinding.SettingsActivityBinding;
import com.sajjadsjat.utils.H;

public class SettingsActivity extends AppCompatActivity {
    private SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText emailUsername = findViewById(R.id.et_email_username);
        EditText emailPassword = findViewById(R.id.et_email_password);

        binding.etEmailUsername.setText(prefs.getString("email_username", ""));
        emailPassword.setText(prefs.getString("email_password", ""));

        binding.etEmailPassword.addTextChangedListener(H.createAfterTextChanged(s -> {
            prefs.edit().putString("email_username", s.toString()).apply();
        }));

        binding.etHomeQueryLimit.addTextChangedListener(H.createAfterTextChanged(s -> {
            prefs.edit().putString("home_query_limit", s.toString()).apply();
        }));

        binding.etClientQueryLimit.addTextChangedListener(H.createAfterTextChanged(s -> {
            prefs.edit().putString("client_query_limit", s.toString()).apply();
        }));
    }
}