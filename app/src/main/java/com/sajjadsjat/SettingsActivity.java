package com.sajjadsjat;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.sajjadsjat.databinding.SettingsActivityBinding;
import com.sajjadsjat.utils.H;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        com.sajjadsjat.databinding.SettingsActivityBinding binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.etEmailUsername.setText(prefs.getString("email_username", ""));
        binding.etEmailPassword.setText(prefs.getString("email_password", ""));

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