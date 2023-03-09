package com.example.nitcmessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    String[] users={"Select User Type", "Student", "Mess Contractor", "Admin"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar=findViewById(R.id.progressBar);
        etEmail=findViewById(R.id.inputEmail);
        etPassword=findViewById(R.id.inputPwd);
        Spinner type=findViewById(R.id.userType);

        TextView btnNewUser=findViewById(R.id.newUser);
        Button btnLogin=findViewById(R.id.btnLogin);
        authProfile=FirebaseAuth.getInstance();
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

            }
        });
        ArrayAdapter<String> adaptor=new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, users);
        adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adaptor);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                //String value=parent.getItemAtPosition(i).toString();
                //Toast.makeText(LoginActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=etEmail.getText().toString();
                String pwd=etPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email required");
                    etEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Incorrect email!");
                    etEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password required");
                    etPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email, pwd);
                }
            }
        });
    }
    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(LoginActivity.this, "User login failed", Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}