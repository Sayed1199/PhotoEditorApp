package com.example.photo_editor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;


public class PhotoPickedActivity extends AppCompatActivity {


    private Toolbar toolbar;

    private ImageView imageView;
    private ImageButton backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picked);

        toolbar = (Toolbar) findViewById(R.id.photo_picked_toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.picked_image);
        backButton = findViewById(R.id.back_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoPickedActivity.this,MainActivity.class));
                finish();
            }
        });

        Intent intent = getIntent();

        Uri imageUri =  Uri.parse(intent.getStringExtra("uri"));

        Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);

        dsPhotoEditorIntent.setData(imageUri);

        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Editor");

        //int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

        //dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

        startActivityForResult(dsPhotoEditorIntent, 200);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 200:

                    Uri outputUri = data.getData();

                    imageView.setImageURI(outputUri);

                    break;

            }

        }

        if(resultCode == RESULT_OK){
            Log.d("resulttt","ok");
        }else{
            Log.d("resulttt","not ok");
            startActivity(new Intent(PhotoPickedActivity.this,MainActivity.class));
        }


    }

}