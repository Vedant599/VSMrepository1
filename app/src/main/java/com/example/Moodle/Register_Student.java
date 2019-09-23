package com.example.Moodle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

public class Register_Student extends AppCompatActivity {

    private TextView heading;
    private EditText FN;
    private EditText LN;
    private EditText Email;
    private EditText Password;
    private RadioGroup gender;
    private RadioButton selectGender;
    private TextView std;
    private Spinner spcntcode;
    private List<String> priorityList;
    private TextView dob;
    private EditText phno;
    private Button btn_Register;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ImageView imageViewProPic;
    private static int PICK_IMAGE=123;
    Uri imagePath;                              //Uri - Unique Resource Identifier...
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null){
            imagePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                imageViewProPic.setImageBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__student);
        setUiId();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSpinner();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
        /*priorityList = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,priorityList);
        spstd.setAdapter(adapter);
        spstd.setOnItemSelectedListener(this);*/
        imageViewProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");          //One can use application/* for all kinds of files like doc pdf etc
                //One can user audio/* to support all kinds os audio...
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()){
                    String user_Email = Email.getText().toString().trim();
                    String user_Password = Password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_Email,user_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                                /*Toast.makeText(Register_Student.this,"Registration successful",Toast.LENGTH_SHORT).show();*/
                            }else{
                                Toast.makeText(Register_Student.this,"Registration Unsuccessful",Toast.LENGTH_SHORT).show();
                                /*Intent intent = new Intent(Register_Student.this,MainActivity.class);
                                startActivity(intent);
                                Register_Student.this.finish();*/
                            }

                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register_Student.this,MainActivity.class);
                startActivity(intent);
                //Register_Student.this.finish();
            }
        });
    }
    private void setUiId(){
        FN = (EditText)findViewById(R.id.et_FN);
        LN = (EditText)findViewById(R.id.et_LN);
        Email = (EditText)findViewById(R.id.et_Email);
        Password = (EditText)findViewById(R.id.et_password);
        gender = (RadioGroup)findViewById(R.id.radioGroup);
        std = (TextView) findViewById(R.id.et_std);
        dob = (TextView)findViewById(R.id.et_dob);
        imageViewProPic = (ImageView)findViewById(R.id.imProfile);
        btn_Register = (Button)findViewById(R.id.btn_Register);
        login = (Button)findViewById(R.id.btn_login);
        imageViewProPic = (ImageView)findViewById(R.id.imProfile);

    }
    private boolean check(){
        boolean result=false;
        String userName_first=FN.getText().toString();
        String userName_last=LN.getText().toString();
        String userEmail=Email.getText().toString();
        String DOB=dob.getText().toString();
        if(userName_first.isEmpty()){
            Toast.makeText(this,"Please enter the first name",Toast.LENGTH_SHORT).show();
            return result;
        }
        if(userName_last.isEmpty()){
            Toast.makeText(this,"Please enter the last name",Toast.LENGTH_SHORT).show();
            return result;
        }
        if(userEmail.isEmpty()){
            Toast.makeText(this,"Please enter the email-address",Toast.LENGTH_SHORT).show();
            return result;
        }
        if(DOB.isEmpty()){
            Toast.makeText(this,"Please enter the date of birth",Toast.LENGTH_SHORT).show();
            return result;
        }
        if(imagePath==null){
            Toast.makeText(this,"Please select the image...",Toast.LENGTH_SHORT).show();
            return result;
        }
        result=true;
        return result;
    }
    /*private void getSpinner(){
        spcntcode.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryData.countryNames));

    }*/
    private String getRadioText(){
        int radioId = gender.getCheckedRadioButtonId();
        selectGender = findViewById(radioId);
        return selectGender.getText().toString();
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        uploadUserData();
                        Toast.makeText(Register_Student.this,"Successfully Registered.Verification email sent",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        //finish();
                        startActivity(new Intent(Register_Student.this,MainActivity.class));
                    }else{
                        Toast.makeText(Register_Student.this,"Network problem",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());                 //This getUid method is to get the User ID which is a hash value from the firebase...
        //In the above line we might have given the name as the reference but there could have been multiple names creating problems in data manupilation...
        StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pics");
        //Here the data is stored in this manner on firebase Uid/Images/profile_pics.jpg...
        //Here in the first child we can even specify audio... instead of Images...
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register_Student.this,"Unsuccessfull to uploaded image...",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Register_Student.this,"Successfully uploaded image...",Toast.LENGTH_SHORT).show();
            }
        });
        UserProfile userProfile = new UserProfile(FN.getText().toString(),LN.getText().toString(),Email.getText().toString(),std.getText().toString(),dob.getText().toString(),getRadioText());
        databaseReference.setValue(userProfile);
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
