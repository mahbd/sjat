package com.sajjadsjat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.sajjadsjat.databinding.ActivityMainBinding;
import com.sajjadsjat.utils.H;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Button backupButton = binding.navView.getHeaderView(0).findViewById(R.id.btn_send_mail);
        backupButton.setOnClickListener(v -> {
            backupButton.setEnabled(false);
            backup();
        });

        if (prefs.getLong("last_backup", 0) + 60 * 60 * 24 < H.datetimeToTimestamp(LocalDateTime.now())) {
            binding.navView.getHeaderView(0).setBackground(AppCompatResources.getDrawable(this, R.drawable.red_side_nav_bar));
            backupButton.setEnabled(true);
        }

        if (prefs.getLong("last_backup", 0) + 60 * 10 > H.datetimeToTimestamp(LocalDateTime.now())) {
            backupButton.setEnabled(false);
        }

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_clients, R.id.nav_ovens)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_address) {
            navController.navigate(R.id.nav_address);
        } else if (item.getItemId() == R.id.action_employees) {
            navController.navigate(R.id.nav_employees);
        } else if (item.getItemId() == R.id.action_units) {
            navController.navigate(R.id.nav_prices);
        } else if (item.getItemId() == R.id.action_backup_restore) {
            Intent intent = new Intent(this, BackupActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void backup() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            Realm realm = Realm.getDefaultInstance();
            File realmFile = new File(realm.getPath());

            File documentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            documentDir = new File(documentDir, "sjat");

            if (!documentDir.exists()) {
                documentDir.mkdirs();
            }
            int day = LocalDateTime.now().getDayOfMonth();
            int month = LocalDateTime.now().getMonthValue();
            String backupFileName = "sjat-" + day + "-" + month + ".realm";
            File backupFile = new File(documentDir, backupFileName);

            try {
                FileInputStream inputStream = new FileInputStream(realmFile);
                FileOutputStream outputStream = new FileOutputStream(backupFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
                inputStream.close();
                Toast.makeText(this, "Successful " + documentDir.getPath(), Toast.LENGTH_SHORT).show();
                String backupMail = prefs.getString("backup_mail", "mahmudula2000@gmail.com");
                H.sendEmail(this, backupMail, "Backup", "", backupFile);
                long lastBackup = H.datetimeToTimestamp(LocalDateTime.now());
                prefs.edit().putLong("last_backup", lastBackup).apply();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to backup files", Toast.LENGTH_SHORT).show();
            }
        }
    }
}