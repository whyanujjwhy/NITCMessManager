package com.example.nitcmessmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";
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
                    //get instance of current user
                    FirebaseUser fbUser=authProfile.getCurrentUser();

                    //check if email is verified before user can access their profile
                    //if(fbUser.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_LONG).show();
                        //open user profile activity
                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                        //prevents user to go back to registration page after registering once.
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                   //  }
                   /* else {
                        fbUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }*/
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e) {
                        etEmail.setError("User doesn't exists no longer, please register again");
                        etEmail.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        etEmail.setError("Invalid credentials, please re-register");
                        etEmail.requestFocus();
                    }
                    catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email id. You can't login without email verification.");

        //open email apps if user clicks continue btn
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //..new window
                startActivity(intent);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    //check if user is already logged in or not
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null) {
            Toast.makeText(LoginActivity.this, "Already logged in!", Toast.LENGTH_SHORT).show();
            //start userProfileActivity
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish();
        }
        else {
            Toast.makeText(LoginActivity.this, "You can login now!", Toast.LENGTH_SHORT).show();

        }
    }
}