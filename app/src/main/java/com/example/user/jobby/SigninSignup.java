package com.example.user.jobby;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.example.user.jobby.model.Route;

import java.io.InputStream;
import java.util.ArrayList;

public class SigninSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signin_signup);
        try {
            setContentView(R.layout.activity_signin_signup);
            ImageView myLogoImgView = (ImageView) findViewById(R.id.imglogo);
            myLogoImgView.setImageBitmap(decodeSampleBitmapFromResource(getResources(), R.drawable.oldlogo, 100, 100));
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

//        TextView t = (TextView) findViewById(R.id.link_signup);
        TextView signuparea = (TextView) findViewById(R.id.link_signup);
        final EditText textemail = (EditText) findViewById(R.id.input_email);
        final EditText textpassword = (EditText) findViewById(R.id.input_password);
        Button buttonlogin = (Button) findViewById(R.id.button);

//        t.setOnClickListener(new View.OnClickListener() {
        signuparea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                RestAPI singleton = RestAPI.getINSTANCE();

                String token = singleton.connect(textemail.getText().toString(),textpassword.getText().toString(),"");
                if(token.equals("error")){
                    Toast.makeText(SigninSignup.this, "impossible to connect", Toast.LENGTH_LONG)
                            .show();
                }else if(token.equals("bad auth")) {
                    Toast.makeText(SigninSignup.this, "bad auth", Toast.LENGTH_LONG)
                            .show();
                }else{
                    Toast.makeText(SigninSignup.this, token, Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //original height and width of resource image
        final int imgHeight = options.outHeight;
        final int imgWidth = options.outWidth;
        int inSampleSize = 1;

        if (imgHeight > reqHeight || imgWidth > reqWidth) {
            final int halfHeight = imgHeight / 2;
            final int halfWidth = imgWidth / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        //configure inJustDecodeBoudns=true to acquire the size of resource image
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opt);

        //calculate inSampleSize
        opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);

        // generate Bitmap (decoding image with inSampleSize obtained)
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, opt);
    }
}