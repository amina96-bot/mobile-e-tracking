package com.starbrands.stb_carburant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText username;
    Button reset;
    DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        username=(EditText) findViewById(R.id.username_reset);
        reset=(Button) findViewById((R.id.btn_reset));
        db = new DbHandler(ChangePasswordActivity.this);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString();
                Boolean checkUser=db.checkUsername(user);
                if(checkUser){
                    Intent intent = new Intent(ChangePasswordActivity.this, ResetActivity.class);
                    intent.putExtra("username",user);
                    startActivity(intent);
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "Nom d'utilisateur incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}