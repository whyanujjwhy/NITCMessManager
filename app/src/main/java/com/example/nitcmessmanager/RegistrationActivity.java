package com.example.nitcmessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.locks.ReadWriteLock;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etName, etPassword;
    private ProgressBar progressBar;
    private static final String TAG = "RegistrationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //getSupportActionBar().setTitle("Register");
        progressBar=findViewById(R.id.progressBar);
        etUsername=findViewById(R.id.inputUsername);
        etEmail=findViewById(R.id.inputEmailid);
        etName=findViewById(R.id.inputName);
        etPassword=findViewById(R.id.inputPassword);

        TextView btnAlreadyAcc=findViewById(R.id.AlreadyHaveAcc);
        btnAlreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
        Button btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=etUsername.getText().toString();
                String email=etEmail.getText().toString();
                String name=etName.getText().toString();
                String pwd=etPassword.getText().toString();

                if(TextUtils.isEmpty(username)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter your username", Toast.LENGTH_LONG).show();
                    etUsername.setError("Username required");
                    etUsername.requestFocus();
                }
                else if(TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email required");
                    etEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegistrationActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    etEmail.setError("Incorrect email");
                    etEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(name)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter your name", Toast.LENGTH_LONG).show();
                    etName.setError("Name required");
                    etName.requestFocus();
                }
                else if(TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegistrationActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password required");
                    etPassword.requestFocus();
                }
                else if(pwd.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password should be of length at least 6", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password too weak");
                    etPassword.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(username, email, name, pwd);
                }
            }
        });

    }
    private void registerUser(String username, String email, String name, String pwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //creating user profile
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Toast.makeText(RegistrationActivity.this, "Student registered successfully", Toast.LENGTH_LONG).show();

                            FirebaseUser fbUser=auth.getCurrentUser();
                            //display name of user
                            UserProfileChangeRequest profileChangeReq = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            fbUser.updateProfile(profileChangeReq);
                            //enter user data into FB Realtime DB
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(username, email, pwd);
                            //extracting user ref from DB for Reg users
                            DatabaseReference refProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
                            refProfile.child(fbUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegistrationActivity.this, "Student registered successfully", Toast.LENGTH_LONG).show();
                                                //send verification email
                                                fbUser.sendEmailVerification();
                                                //open user profile
                                               Intent intent = new Intent(RegistrationActivity.this, UserProfileActivity.class);
                                                //prevents user to go back to registration page after registering once.
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(RegistrationActivity.this, "Student registration failed", Toast.LENGTH_LONG).show();

                                            }progressBar.setVisibility(View.GONE);
                                        }
                                    });



                        }
                        else {
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthWeakPasswordException e) {
                                etPassword.setError("Password too weak, use a alphanumeric pwd");
                                etPassword.requestFocus();
                            }
                            catch (FirebaseAuthInvalidCredentialsException e) {
                                etPassword.setError("Email already in use or invalid, re-enter");
                                etPassword.requestFocus();
                            }
                            catch (FirebaseAuthUserCollisionException e) {
                                etPassword.setError("User already registered with this email, use another email");
                                etPassword.requestFocus();
                            }
                            catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}