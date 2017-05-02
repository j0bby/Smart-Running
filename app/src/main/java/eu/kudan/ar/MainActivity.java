package eu.kudan.ar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    final static int TAKE_PIC_REQUEST = 1;
    final static int FIND_BALISE_REQUEST = 2;

    File pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton picbtn = (FloatingActionButton) findViewById(R.id.picactivity);
        picbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TakePhoto.class);
                startActivityForResult(intent, TAKE_PIC_REQUEST);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        FloatingActionButton rabtn = (FloatingActionButton) findViewById(R.id.RAbtn);
        rabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindBalise.class);
                if(pic != null)
                    intent.putExtra("picture", pic);
                startActivityForResult(intent, FIND_BALISE_REQUEST);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK ) {
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                pic = (File)bundle.get("picture");
            }
        }
        else if (requestCode == FIND_BALISE_REQUEST && resultCode == RESULT_OK ) {
            // do something
        }
    }

}
