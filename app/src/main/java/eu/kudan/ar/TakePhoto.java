package eu.kudan.ar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap = null;
    ImageView mImageView = null;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (savedInstanceState != null)
            fileName = savedInstanceState.getString("fileName");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        mImageView = (ImageView) findViewById(R.id.picPreview);
        if(fileName != null){
            setPic();
        }

        FloatingActionButton picButton = (FloatingActionButton) findViewById(R.id.takePic);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        FloatingActionButton savePicBtn = (FloatingActionButton) findViewById(R.id.savePic);
        savePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(fileName);
                if(file.exists()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("picture", file);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else
                {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putString("fileName", fileName);
    }



    private void dispatchTakePictureIntent() {
        try{
            File outputDir = getExternalCacheDir();
            File tempFile = File.createTempFile("ar_pic", ".jpg", outputDir);
            fileName = tempFile.getAbsolutePath();
            Uri uri = Uri.fromFile(tempFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (IOException ex){

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            if (data != null)
            {
                Uri b = data.getData();
                Bitmap bitmap = BitmapFactory.decodeFile(b.toString());
                mImageView.setImageBitmap(bitmap);
            }
            setPic();
            findViewById(R.id.savePic).setVisibility(View.VISIBLE);
        }
    }

    private void setPic() {

        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        mImageView.setImageBitmap(bitmap);
    }
}
