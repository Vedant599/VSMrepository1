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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateUserInfo extends AppCompatActivity {

    private ImageView uppropicImg;
    private EditText upFn,upLn,upGen,upEmail,upStd,upDob;
    private Button upSave;
    private FirebaseDatabase profileFirebaseDatabase;
    private FirebaseAuth profileFirebaseAuth;
    private FirebaseStorage profileFirebaseStorage;
    private static int PICK_IMAGE=123;
    Uri imagePath;                              //Uri - Unique Resource Identifier...


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null){
            imagePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                uppropicImg.setImageBitmap(bitmap);
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
        setContentView(R.layout.activity_update_user_info);
        setUi();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileFirebaseAuth = FirebaseAuth.getInstance();
        profileFirebaseDatabase = FirebaseDatabase.getInstance();
        profileFirebaseStorage = FirebaseStorage.getInstance();

        final DatabaseReference profileDatabaseReference = profileFirebaseDatabase.getReference(profileFirebaseAuth.getUid());

        final StorageReference profileStorageReference = profileFirebaseStorage.getReference();         //This need to be declared final since this is been accessed by the inner class...
        profileStorageReference.child(profileFirebaseAuth.getUid()).child("Images").child("Profile Pics").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //profilePic.setImageUri(uri);              //We cannot implement this line since the data is in the form of a link so it cannot be assigned directly to the image view...
                Picasso.get().load(uri).into(uppropicImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUserInfo.this,"Failed to download image...",Toast.LENGTH_LONG).show();
            }
        });
        profileDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                upFn.setText(userProfile.getFName());
                upLn.setText(userProfile.getLName());
                upGen.setText(userProfile.getGender());
                upEmail.setText(userProfile.getEmail());
                upStd.setText(userProfile.getStd());
                upDob.setText(userProfile.getDOB());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateUserInfo.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        upSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String upFN = upFn.getText().toString();
                String upLN = upLn.getText().toString();
                String upGEN = upGen.getText().toString();
                String upEMAIl = upEmail.getText().toString();
                String upSTD = upStd.getText().toString();
                String upDOB = upDob.getText().toString();

                UserProfile userProfile = new UserProfile(upFN,upLN,upEMAIl,upSTD,upDOB,upGEN);

                profileDatabaseReference.setValue(userProfile);

                StorageReference imageReference = profileStorageReference.child(profileFirebaseAuth.getUid()).child("Images").child("Profile Pics");
                //Here the data is stored in this manner on firebase Uid/Images/profile_pics.jpg...
                //Here in the first child we can even specify audio... instead of Images...
                UploadTask uploadTask = imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateUserInfo.this,"Unsuccessfull to uploaded image...",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UpdateUserInfo.this,"Successfully uploaded image...",Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });

        uppropicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");          //One can use application/* for all kinds of files like doc pdf etc
                //One can user audio/* to support all kinds os audio...
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });
    }

    private void setUi(){
        uppropicImg = (ImageView)findViewById(R.id.imviewupdatedata);
        upFn = (EditText)findViewById(R.id.upetfn);
        upLn = (EditText)findViewById(R.id.upetln);
        upGen = (EditText)findViewById(R.id.upetgen);
        upEmail = (EditText)findViewById(R.id.upetemail);
        upStd = (EditText)findViewById(R.id.upetstd);
        upDob = (EditText)findViewById(R.id.upetdob);
        upSave = (Button)findViewById(R.id.upbtnsave);
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
