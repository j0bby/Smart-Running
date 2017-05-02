package eu.kudan.ar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TextView t = (TextView)findViewById(R.id.link_login);


        t.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the login activity
                Intent intent = new Intent(getApplicationContext(), SigninSignup.class);
                startActivity(intent);
                finish();
                // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
}
