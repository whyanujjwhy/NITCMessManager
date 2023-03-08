package com.example.nitcmessmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.*;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    //private EditText editText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);


        //editText = findViewById(R.id.editText);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRegistration();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });
       /* button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAdminLogin();
            }
        });*/

    }
    public void openActivityRegistration() {
        Intent I=new Intent(this, RegistrationActivity.class);
        startActivity(I);
    }
    public void openActivityLogin() {
        Intent I=new Intent(this, LoginActivity.class);
        startActivity(I);
    }
    /*public void openActivityAdminLogin() {
        Intent I=new Intent(this, AdminLoginActivity.class);
        startActivity(I);
    }*/
}