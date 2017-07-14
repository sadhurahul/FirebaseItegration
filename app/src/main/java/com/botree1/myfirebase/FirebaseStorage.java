package com.botree1.myfirebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Created by botree1 on 30/1/17.
 */

public class FirebaseStorage extends Activity {

    Button btnUpload, btnDownload, btnChoose;
    ImageView imgFileStorage;
    public Uri filePath;
    StorageReference storageReference;
    com.google.firebase.storage.FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_storage_layout);
        storage= com.google.firebase.storage.FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://myfirebase-de13b.appspot.com");

        getElement();
        initElement();

    }

    private void initElement() {
         btnChoose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showFileChooser();
             }
         });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upLoadFile();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadFile();
            }


        });




    }

    private void downLoadFile() {

          StorageReference childRef=storageReference.child("images/pic.jpg");
        try {
            final File localFile=File.createTempFile("images","jpg");
            childRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgFileStorage.setImageBitmap(bitmap);


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    private void upLoadFile() {
        if(filePath!=null){

            final ProgressDialog pDialog=new ProgressDialog(this);
            pDialog.setMessage("Upload File");
            pDialog.show();
            StorageReference childRef=storageReference.child("images/pic.jpg");
            childRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pDialog.dismiss();
                            Toast.makeText(FirebaseStorage.this,"File Upload",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.dismiss();
                            Toast.makeText(FirebaseStorage.this,"Upload Error:"+e.toString(),Toast.LENGTH_SHORT).show();


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=((100.0* taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                            pDialog.setMessage("Upload "+(int)progress+"%...");
                        }
                    });

            {



            }


        }


    }

    private void showFileChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),100);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
             if(requestCode==100 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                 filePath=data.getData();
                 try {
                     Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                     imgFileStorage.setImageBitmap(bitmap);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

             }
    }

    private void getElement() {
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgFileStorage = (ImageView) findViewById(R.id.imgFileStorage);


    }
}
