package com.example.Moodle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button LoginBtn;
    private TextView Info;
    private TextView Register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private int counter=5;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);
        LoginBtn = (Button) findViewById(R.id.btn_login);
        Info = (TextView) findViewById(R.id.tv_attempts);
        Register = (TextView) findViewById(R.id.tv_register);
        forgotPassword = (TextView)findViewById(R.id.tv_forgotPass);

        Info.setText("Number of attempts remaining are: 5");

        //Firebase functions...
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, HomePage.class));                        //Here start activity is very important since without start we cannot jump to the next activity...
        }

        //progress dialogue
        progressDialog = new ProgressDialog(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {                                //Method to do something when a button or a text is clicked...
            @Override
            public void onClick(View view) {
                if (check()) {            //Check the methods first get the text and then convert it to string...
                    validate(Email.getText().toString(), Password.getText().toString());

                }

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Select_User.class);
                startActivity(intent);
                //MainActivity.this.finish();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
                //MainActivity.this.finish();
            }
        });
    }

    private boolean check() {
        boolean result = false;
        String userName = Email.getText().toString();
        String userPassword = Password.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter the user name", Toast.LENGTH_SHORT).show();
            return result;
        }
        if (userPassword.isEmpty()) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return result;
        }
        result = true;
        return result;
    }

    public void validate(String userEmail, String userPassword) {
        progressDialog.setMessage("Please wait while the verification is done");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    //finish();
                    /*Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomePage.class));*/
                    checkEmailVerification();
                } else {
                    Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    counter--;
                    Info.setText("Number of attempts remaining are: "+counter);
                    if(counter==0){
                        LoginBtn.setEnabled(false);
                    }
                }
            }
        });
    }

    public void checkEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean check = firebaseUser.isEmailVerified();

        if(check){
            //finish();
            startActivity(new Intent(MainActivity.this,HomePage.class));
        }else{
            Toast.makeText(this,"Please verify the email",Toast.LENGTH_SHORT).show();;
            firebaseAuth.signOut();
        }
    }
}

