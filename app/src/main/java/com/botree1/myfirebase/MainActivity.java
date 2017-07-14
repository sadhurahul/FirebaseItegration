package com.botree1.myfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public DatabaseReference mFirebaseDatabase;
    public FirebaseDatabase mFirebaseInstance;
    public String userID;
    Context mContext;
    EditText editName, editEmail, editAddress, editMobile;
    Button btnSave, btnGetAllData,btnUpdate,btnDelete,btnSignOut;
    List<UserVO> list;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getElements();
        auth=FirebaseAuth.getInstance();
        getData();
        initElements();
    }

    private void initElements() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String address = editAddress.getText().toString();
                String mobile = editMobile.getText().toString();

                UserVO vo = new UserVO();
                vo.setName(name);
                vo.setEmail(email);
                vo.setAddress(address);
                vo.setMobile(mobile);


                createUser(vo);
            }
        });


        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                String address = editAddress.getText().toString();
                String mobile = editMobile.getText().toString();
                UserVO vo=new UserVO();
                vo.setName(name);
                vo.setEmail(email);
                vo.setAddress(address);
                vo.setMobile(mobile);

                updateData(vo);




            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();


                for(int i=0;i<list.size();i++){
                    UserVO vo=list.get(i);

                    Toast.makeText(mContext,vo.getName(),Toast.LENGTH_SHORT).show();
                    if(name.equals(vo.getName().toString())){
                        mFirebaseInstance=FirebaseDatabase.getInstance();
                        mFirebaseDatabase=mFirebaseInstance.getReference("users");
                        mFirebaseDatabase.child(vo.getId()).removeValue();
                        Toast.makeText(mContext,"Delete Firebase Database for Name :"+vo.getName(),Toast.LENGTH_SHORT).show();

                    }
                }

                getData();

            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("###","btnSignout Onclick");

                auth.signOut();
                FirebaseAuth.AuthStateListener authStateListener= new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        if(user==null){
                            Log.v("###","Sign out");
                            Intent i=new Intent(mContext,LoginActivity.class);
                            startActivity(i);

                        }

                    }
                };
                auth.addAuthStateListener(authStateListener);

            }
        });



    }

    private void updateData(UserVO userVO) {

        String name = editName.getText().toString();
        for(int i=0;i<list.size();i++){
            UserVO vo=list.get(i);
            Toast.makeText(mContext,vo.getName(),Toast.LENGTH_SHORT).show();
            if(name.equals(vo.getName().toString())){

                 userVO.setId(vo.getId());
                mFirebaseInstance=FirebaseDatabase.getInstance();
                mFirebaseDatabase=mFirebaseInstance.getReference("users");
                mFirebaseDatabase.child(vo.getId()).setValue(userVO);
                Toast.makeText(mContext,"Upadate FireBase Database Name :"+vo.getName(),Toast.LENGTH_SHORT).show();

            }
        }


       /* Random random=new Random();
        int randomNumber=random.nextInt(list.size()-1);
        Log.v("###"," randomNumber "+randomNumber);*/




    }

    private void createUser(UserVO vo) {

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        userID = mFirebaseDatabase.push().getKey();
        vo.setId(userID);
        mFirebaseDatabase.child(userID).setValue(vo);
        addUserChangeListener();
    }

    private void addUserChangeListener() {
        mFirebaseDatabase.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserVO userVO = dataSnapshot.getValue(UserVO.class);
                if (userVO == null) {
                    Log.v("####", "user data is null");
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("###", "database Error :" + databaseError);

            }
        });
    }



    public void getData() {
        list=new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    UserVO user = noteDataSnapshot.getValue(UserVO.class);
                    Log.v("###", user.getName());
                    list.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.v("###", "Database Error" + databaseError);

            }
        });


    }


    private void getElements() {
        mContext = this;
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editMobile = (EditText) findViewById(R.id.editMobile);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnGetAllData = (Button) findViewById(R.id.btnGetAllData);
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnSignOut=(Button)findViewById(R.id.btnSignOut);



    }
}
