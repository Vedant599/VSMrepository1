package com.example.Moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private EditText newPass,retpyePass;
    private Button changePass;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setUi();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Comparing the two input of the password to be same...
        //String newpass = newPass.getText().toString();

        /*if(!check(newpass,retypepass)){
            Toast.makeText(ChangePassword.this,"Please retype the password correctly...",Toast.LENGTH_LONG).show();
        }*/


            changePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String retypepass = retpyePass.getText().toString();
                    firebaseUser.updatePassword(retypepass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ChangePassword.this,"Password changed successfully",Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                            else{
                                Toast.makeText(ChangePassword.this,"Network error or recheck the passwords...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });


    }

    private void setUi(){
        newPass = (EditText)findViewById(R.id.etnewpass);
        retpyePass = (EditText)findViewById(R.id.etnewpass);
        changePass = (Button)findViewById(R.id.btnchangepass);
    }

    /*    if(newpass.equals(oldpass)){
            return true;
        }
        else{
            return false;
        }
    }*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
