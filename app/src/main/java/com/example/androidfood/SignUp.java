package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import Model.User;

public class SignUp extends AppCompatActivity {

    RelativeLayout background;
    EditText edtPhone,edtPass,edtName;
    Button btn_signup;
    ImageView img_logo;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            background.setBackground(new BitmapDrawable(getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName = findViewById(R.id.id_name);
        edtPass = findViewById(R.id.id_pass);
        edtPhone = findViewById(R.id.id_phone);
        btn_signup = findViewById(R.id.btn_signup);
        background = findViewById(R.id.img_backgroundsignup);
        img_logo =  findViewById(R.id.img_addfood);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waitting");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(edtPhone.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone already register", Toast.LENGTH_SHORT).show();
                        }
                        else{
//                            mDialog.dismiss();
//                            int status = 0;
//                            User user = new User(edtName.getText().toString(),edtPass.getText().toString(),status);
//                            table_user.child(edtPhone.getText().toString()).setValue(user);
//                            Toast.makeText(SignUp.this, "Register Successfully", Toast.LENGTH_SHORT).show();
//                            finish();

                            Intent intent = new Intent(getApplicationContext(),VerifiOTPActivity.class);
                            intent.putExtra("phone", edtPhone.getText().toString());
                            intent.putExtra("pass", edtPass.getText().toString());
                            intent.putExtra("name", edtName.getText().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        Picasso picasso = Picasso.with(this);
        picasso.load(
                R.drawable.my_bg
        ).into(target);





    }
}