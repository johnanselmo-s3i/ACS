package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Paint;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

public class CreateAccount extends AppCompatActivity {

    Button createAcc;
    TextView already;
    EditText pass;
    EditText passConf;
    TextView errors;
    CheckBox showPass;
    EditText user;
    CheckBox showConfPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAcc = findViewById(R.id.createBut);
        already = findViewById(R.id.already);
        pass = findViewById(R.id.pass);
        passConf = findViewById(R.id.cconf);
        errors = findViewById(R.id.errors2);
        showPass = findViewById(R.id.showPass);
        user = findViewById(R.id.user);
        showConfPass = findViewById(R.id.showConf);

        already.setPaintFlags(already.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        already.setText("Already have an account?");

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = pass.getText().toString();
                String confPass = passConf.getText().toString();

                File file = new File(getFilesDir(),"users.json");

                everything(username, password, confPass);
            }
        });

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass.getText().length() > 0) {
                    showPass.setVisibility(View.VISIBLE);
                } else {
                    showPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        passConf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passConf.getText().length() > 0) {
                    showConfPass.setVisibility(View.VISIBLE);
                } else {
                    showConfPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.setSelection(pass.length());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.setSelection(pass.length());
                }
            }
        });

        showConfPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passConf.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passConf.setSelection(passConf.length());
                } else {
                    passConf.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passConf.setSelection(passConf.length());
                }
            }
        });
    }

    public boolean checkAccount(String username, String password, String confPass) {

        boolean valid = true;
        if (userExists(username)) {
            errors.setText("Username '" + username + "' already exists!");
            return false;
        }

        if (username.length() < 1 || password.length() < 1 || confPass.length() < 1) {
            errors.setText("Please fill out all the fields.");
            return false;
        }

        if (! confPass.equals(password)) {
            errors.setText("Passwords do not match.");
            return false;
        }

        String passError = "Your password does not meet the following password requirements.\nIt must contain at least:\n";
        boolean passEr = false;
        if (! password.matches("^.{5,10}$")) {
            passEr = true;
            passError += " - 5 to 10 characters\n";
        } if (! password.matches(".*[A-Z].*")) {
            passEr = true;
            passError += " - 1 uppercase letter\n";
        } if (! password.matches(".*[0-9].*")) {
            passEr = true;
            passError += " - 1 number (0-9)";
        } if (! password.matches(".*[a-z].*")) {
            passEr = true;
            passError += " - 1 lowercase letter\n";
        } if (! password.matches(".*[!@#$%^&*()].*")) {
            passEr = true;
            passError += " - 1 special character !@#$%^&*()";
        } if (passEr) {
            errors.setText(passError);
            return false;
        }

        return valid;
    }

    public boolean userExists(String username) {
        return getSharedPreferences("accounts", MODE_PRIVATE).contains(username);
    }

    public User getUser(String username) {
        Gson gson = new Gson();
        return gson.fromJson(getSharedPreferences("accounts", MODE_PRIVATE).getString(username, "notauser"), User.class);
    }

    public void everything(String username, String password, String confPass) {
        boolean valid = checkAccount(username, password, confPass);
        if (valid) {
            User u = new User(username, password);
            Gson gson = new Gson();
            String json = gson.toJson(u);

            SharedPreferences sharedPreferences = getSharedPreferences("accounts", MODE_PRIVATE);
            sharedPreferences.edit().putString(username, json).commit();
            Toast.makeText(getApplicationContext(), "Account created!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
}
