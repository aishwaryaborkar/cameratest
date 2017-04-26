package com.example.aishwarya.cameratest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";

    private Button uploadButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = (Button)findViewById(R.id.upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                onLaunchCamera(view);
            }
        });
    }

    public void onLaunchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(imageBitmap);
                saveImageToGallery();
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

            return FileProvider.getUriForFile(MainActivity.this, "com.example.aishwarya.cameratest.fileprovider", file);
    }

    private void saveImageToGallery(){
        imageView.setDrawingCacheEnabled(true);
        Bitmap b = imageView.getDrawingCache();
        MediaStore.Images.Media.insertImage(this.getContentResolver(), b,"test", "test");
    }

}
