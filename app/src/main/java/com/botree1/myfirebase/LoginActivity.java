package com.botree1.myfirebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by botree1 on 26/1/17.
 */

public class LoginActivity extends AppCompatActivity {
    Context mContext;
    EditText editEmail, editPassword;
    Button btnSignIn, btnSignUp,btnForgetPassword;
    FirebaseAuth auth;
    ProgressDialog progressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }


        getElements();
        initElments();
    }

    private void initElments() {

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               progressDialog.show();

                String email = editEmail.getText().toString().trim();
                final String password = editPassword.getText().toString().trim();

                if (checkVaildation(email, password)) {

                    Log.v("###", "checkVaildation True");

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Login Error", Toast.LENGTH_SHORT).show();

                            } else {
                                 progressDialog.dismiss();
                                Intent i = new Intent(mContext, MainActivity.class);
                                startActivity(i);

                            }
                        }
                    });

                }


            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (checkVaildation(email, password)) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(mContext, "CreateUser Compete", Toast.LENGTH_SHORT).show();
                                Toast.makeText(mContext, "Please Login", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Sign up Error", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }


            }
        });


        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,ResetPasswordActivity.class);
                startActivity(i);
            }
        });


    }

    private boolean checkVaildation(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(mContext, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {

            Toast.makeText(mContext, "Please Enter password Max 6", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void getElements() {
        mContext = this;
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnForgetPassword=(Button)findViewById(R.id.btnForgetPassword);
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");



    }
}
