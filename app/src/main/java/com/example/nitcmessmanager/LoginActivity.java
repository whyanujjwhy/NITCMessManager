package com.example.nitcmessmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    String[] users={"Select User Type", "Student", "Mess Contractor", "Admin"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=findViewById(R.id.newUser);
        Spinner type=findViewById(R.id.userType);
        btn.setOnClickListener(new View.OnClickListener() {
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
    }
}