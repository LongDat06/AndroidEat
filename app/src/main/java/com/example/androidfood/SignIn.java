package com.example.androidfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import Model.Commen;
import Model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPass;
    Button btn_Signin;
    RelativeLayout background;

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
        setContentView(R.layout.activity_sign_in);

        edtPhone = findViewById(R.id.id_phone);
        edtPass =  findViewById(R.id.id_pass);
        btn_Signin = findViewById(R.id.btn_signin);
        background = findViewById(R.id.img_backgroundsignin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user =database.getReference("User");
        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.getName();
                                user.setPhone(edtPhone.getText().toString());
                                if (user.getPassword().equals(edtPass.getText().toString()) && user.getStatus() == 0) {
                                    Intent homeIntent =  new Intent(SignIn.this,Home.class);
                                    Commen.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                }else if(user.getPassword().equals(edtPass.getText().toString()) && user.getStatus() == 1){
                                    Intent admin =  new Intent(SignIn.this,admin.class);
                                    Commen.currentUser = user;
                                    startActivity(admin);
                                    finish();

                                }
                                else {
                                    Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User not exists", Toast.LENGTH_SHORT).show();

                            }
                        }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
            }
        });


        Picasso picasso = Picasso.with(this);
        picasso.load(
                R.drawable.my_bg
        ).into(target);

    }
}