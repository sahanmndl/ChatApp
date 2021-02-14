package com.smproductions.flingg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText email, name, username, password;
    MaterialButton btnRegister;
    TextView txtBackToLogin;

    FirebaseAuth auth;
    DatabaseReference reference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.register_email);
        name = findViewById(R.id.register_name);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.register_btnRegister);
        txtBackToLogin = findViewById(R.id.register_txtBackToLogin);

        auth = FirebaseAuth.getInstance();

        txtBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String str_username = Objects.requireNonNull(username.getText()).toString();
            String str_name = Objects.requireNonNull(name.getText()).toString();
            String str_email = Objects.requireNonNull(email.getText()).toString();
            String str_password = Objects.requireNonNull(password.getText()).toString();

            if (TextUtils.isEmpty(str_email)) {
                email.setError("Empty");
                email.requestFocus();
            } else if (TextUtils.isEmpty(str_name)) {
                name.setError("Empty");
                name.requestFocus();
            } else if (str_name.contains("@") || str_name.contains("$") || str_name.contains("#") || str_name.contains("*") || str_name.contains("&")) {
                name.setError("Invalid Characters present");
                name.requestFocus();
            } else if (TextUtils.isEmpty(str_username)) {
                username.setError("Empty");
                username.requestFocus();
            } else if (str_username.contains("@") || str_username.contains("$") || str_username.contains("#") || str_username.contains("*") || str_username.contains("&") || str_username.contains(" ")) {
                username.setError("Invalid Characters present");
                username.requestFocus();
            } else if (TextUtils.isEmpty(str_password)) {
                password.setError("Empty");
                password.requestFocus();
            } else if (str_password.contains(" ")) {
                password.setError("Password cannot contain space");
                password.requestFocus();
            } else if (str_password.length() < 8) {
                password.setError("Minimum 8 characters required");
                password.requestFocus();
            } else {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("REGISTERING");
                progressDialog.setMessage("Your new account is being created, please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                userRegistration(str_email, str_name, str_username, str_password);
            }
        });
    }

    private void userRegistration(String email, String name, String username, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(auth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task12 -> {
                            if (task12.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                assert firebaseUser != null;
                                String userID = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userID);
                                hashMap.put("name", name);
                                hashMap.put("username", username.toLowerCase());
                                hashMap.put("search", name.toLowerCase());
                                hashMap.put("imageURL", "default");

                                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Verification link has been sent to your email!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        progressDialog.dismiss();
                                        FirebaseAuthException e = (FirebaseAuthException) task12.getException();
                                        assert e != null;
                                        Toast.makeText(RegisterActivity.this, "REGISTRATION FAILED: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                FirebaseAuthException f = (FirebaseAuthException) task12.getException();
                                assert f != null;
                                Toast.makeText(RegisterActivity.this, "REGISTRATION FAILED: "+f.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        FirebaseAuthException g = (FirebaseAuthException)task.getException();
                        assert g != null;
                        Toast.makeText(RegisterActivity.this, "REGISTRATION FAILED: "+g.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}