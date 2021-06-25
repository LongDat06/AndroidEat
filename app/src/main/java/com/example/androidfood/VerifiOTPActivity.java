package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.concurrent.TimeUnit;

import Model.User;

public class VerifiOTPActivity extends AppCompatActivity {
    EditText input1,input2,input3,input4,input5,input6;
    Button btnVerifi;
    String verificationId,phone,pass,name,verificationCodeBySystem;
    RelativeLayout background;

    ProgressBar progressBar;
    FirebaseDatabase database =  FirebaseDatabase.getInstance();
    DatabaseReference table_user = database.getReference("User");
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
        setContentView(R.layout.activity_verifi_o_t_p);



        background = findViewById(R.id.img_backround);
        input1 = findViewById(R.id.inputcode1);
        input2 = findViewById(R.id.inputcode2);
        input3 = findViewById(R.id.inputcode3);
        input4 = findViewById(R.id.inputcode4);
        input5 = findViewById(R.id.inputcode5);
        input6 = findViewById(R.id.inputcode6);

        btnVerifi = findViewById(R.id.btnVerify);

        
//        setupOTPinput();
        progressBar = findViewById(R.id.progressBar);
        verificationId = getIntent().getStringExtra("verificationId" );
        phone = "+84"+getIntent().getStringExtra("phone");
        pass = getIntent().getStringExtra("pass");
        name = getIntent().getStringExtra("name");

        sendVerificationCodetoUser(phone);
        btnVerifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input1.getText().toString().trim().isEmpty()
                    ||input2.getText().toString().trim().isEmpty()
                    ||input3.getText().toString().trim().isEmpty()
                    ||input4.getText().toString().trim().isEmpty()
                    ||input5.getText().toString().trim().isEmpty()
                    ||input6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifiOTPActivity.this,"Please enter valid code ",Toast.LENGTH_SHORT).show();
                    return;
                }
                String otp = input1.getText().toString()
                        + input2.getText().toString()
                        + input3.getText().toString()
                        + input4.getText().toString()
                        + input5.getText().toString()
                        + input6.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                verificode(otp);
            }
        });
        Picasso picasso = Picasso.with(this);
        picasso.load(
                R.drawable.my_bg
        ).into(target);
    }

    private void sendVerificationCodetoUser(String phone) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                        .setActivity(this)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                progressBar.setVisibility(View.VISIBLE);
                verificode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifiOTPActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }
    };

    private void verificode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        signInTheUserByCredential(phoneAuthCredential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(VerifiOTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            int status = 0;
                            User user = new User(name,pass,status);
                            table_user.child(getIntent().getStringExtra("phone")).setValue(user);
                            Toast.makeText(VerifiOTPActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(VerifiOTPActivity.this,"The verification code entered was invalid ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}