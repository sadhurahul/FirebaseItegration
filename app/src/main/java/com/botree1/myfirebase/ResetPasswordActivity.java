package com.botree1.myfirebase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by botree1 on 26/1/17.
 */

public class ResetPasswordActivity extends Activity {
    EditText editEmail;
    Button btnResetPassword;
    Context mContext;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);
        auth=FirebaseAuth.getInstance();
        getElements();
        initElements();
    }

    private void initElements() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(mContext,"Please Enter Email",Toast.LENGTH_SHORT).show();
                }

                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(mContext,"Please Check your Email",Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(mContext,"Reset Password Error",Toast.LENGTH_SHORT).show();
                       }
                    }
                });


            }
        });

    }

    private void getElements() {
        mContext=this;
        editEmail = (EditText) findViewById(R.id.editEmail);
        btnResetPassword = (Button) findViewById(R.id.btnResetPassword);


    }
}
