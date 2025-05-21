package com.example.lowcaokazji.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lowcaokazji.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnLogin, btnRegister;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        btnRegister = findViewById(R.id.buttonRegister);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String user = editUsername.getText().toString().trim();
        String pass = editPassword.getText().toString();

        String savedUser = prefs.getString("username", "");
        String savedPass = prefs.getString("password", "");
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Podaj login i hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.equals(savedUser) && pass.equals(savedPass)) {
            prefs.edit().putBoolean("logged_in", true).apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Błędny login lub hasło!", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        String user = editUsername.getText().toString().trim();
        String pass = editPassword.getText().toString();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Podaj login i hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        prefs.edit()
                .putString("username", user)
                .putString("password", pass)
                .putBoolean("logged_in", true)
                .apply();
        Toast.makeText(this, "Rejestracja zakończona!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}