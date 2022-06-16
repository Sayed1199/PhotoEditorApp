package com.example.photo_editor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.smb.glowbutton.GlowButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private GlowButton add_image_btn;
    private Toolbar toolbar;
    private Dialog dialog;
    private Uri tempImageUri;
    private String tempImageFilePath="";

    private static final int MY_CAMERA_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        add_image_btn = findViewById(R.id.add_image_btn);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.image_selection_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_rounded));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);


        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("pressed");

                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                }
                dialog.show();
                //startActivity(new Intent(MainActivity.this,TestActivity.class));

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.appbar_menu,menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.miCompose:
                System.out.print("Compose");
                return true;
            case R.id.miProfile:
                System.out.print("Profile");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clickCamera(View view) {
        //Toast.makeText(MainActivity.this,"Camera",Toast.LENGTH_LONG).show();
        dialog.dismiss();


        //Intent openCameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityIntent.launch(openCameraIntent);

        tempImageUri = FileProvider.getUriForFile(this,"com.example.photo_editor.provider",createImageFile());

        cameraLauncher.launch(tempImageUri);


    }

    private File createImageFile() {

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {

            File file = File.createTempFile("temp_photo",".jpg",storageDir);
            tempImageFilePath = file.getAbsolutePath();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }

    }


    public void clickGallery(View view) {
        Toast.makeText(MainActivity.this,"Gallery",Toast.LENGTH_LONG).show();
        dialog.dismiss();

        selectPictureLauncher.launch("image/*");


    }


    ActivityResultLauncher<Uri> cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    if(result){
                        Intent moveIntent = new Intent(MainActivity.this,PhotoPickedActivity.class);
                        moveIntent.putExtra("uri",tempImageUri.toString());
                        startActivity(moveIntent);
                    }

                }
            });

    ActivityResultLauncher<String> selectPictureLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Log.d("finalUri",uri.toString());
                    Intent moveIntent = new Intent(MainActivity.this,PhotoPickedActivity.class);
                    moveIntent.putExtra("uri",uri.toString());
                    startActivity(moveIntent);
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}