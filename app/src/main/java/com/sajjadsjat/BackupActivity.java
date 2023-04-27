package com.sajjadsjat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sajjadsjat.databinding.ActivityBackupBinding;
import com.sajjadsjat.utils.Generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import io.realm.Realm;

public class BackupActivity extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBackupBinding binding = ActivityBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBackup.setOnClickListener(v -> backup());
        binding.btnRestore.setOnClickListener(v -> restore());

        binding.btnGenerateAddress.setOnClickListener(v -> {
            binding.btnGenerateAddress.setEnabled(false);
            for (int i = 0; i < 100; i++) {
                Generator.generateRandomAddress();
            }
            binding.btnGenerateAddress.setEnabled(true);
            Toast.makeText(this, "Addresses generated", Toast.LENGTH_SHORT).show();
        });

        binding.btnGenerateClient.setOnClickListener(v -> {
            binding.btnGenerateClient.setEnabled(false);
            for (int i = 0; i < 1000; i++) {
                Generator.generateRandomClient();
            }
            binding.btnGenerateClient.setEnabled(true);
            Toast.makeText(this, "Clients generated", Toast.LENGTH_SHORT).show();
        });

        binding.btnGenerateRecord.setOnClickListener(v -> {
            binding.btnGenerateRecord.setEnabled(false);
            for (int i = 0; i < 10000; i++) {
                Generator.generateRandomRecord(this);
            }
            binding.btnGenerateRecord.setEnabled(true);
            Toast.makeText(this, "Records generated", Toast.LENGTH_SHORT).show();
        });

        binding.btnGeneratePayment.setOnClickListener(v -> {
            binding.btnGeneratePayment.setEnabled(false);
            for (int i = 0; i < 10000; i++) {
                Generator.generateRandomPayment(this);
            }
            binding.btnGeneratePayment.setEnabled(true);
            Toast.makeText(this, "Payments generated", Toast.LENGTH_SHORT).show();
        });

        binding.btnClearDatabase.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                r.deleteAll();
            });
            Toast.makeText(this, "Database cleared", Toast.LENGTH_SHORT).show();
        });
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
                onBackPressed();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to backup files", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void restore() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Realm realm = Realm.getDefaultInstance();
            Uri uri = data.getData();
            try {
                FileOutputStream outputStream = new FileOutputStream(realm.getPath());
                System.out.println("DEBUG Manual:::: " + uri.getPath() + " " + uri.getEncodedPath());
                InputStream inputStream = getContentResolver().openInputStream(uri);

                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                Toast.makeText(this, "Successfully Restored files", Toast.LENGTH_SHORT).show();
                onBackPressed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}