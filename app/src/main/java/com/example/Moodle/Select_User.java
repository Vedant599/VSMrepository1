package com.example.Moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Select_User extends AppCompatActivity {

    private Button std,org,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__user);
        setUi();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Select_User.this,Register_Student.class);
                startActivity(intent);
                //Select_User.this.finish();
            }
        });
        org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Select_User.this,Register_Organiser.class);
                startActivity(intent);
                //Select_User.this.finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Select_User.this,MainActivity.class);
                startActivity(intent);
                //Select_User.this.finish();
            }
        });
    }
    public void setUi(){
        std = (Button)findViewById(R.id.btn_student);
        org = (Button)findViewById(R.id.btn_organiser);
        signup = (Button)findViewById(R.id.btn_signup);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
