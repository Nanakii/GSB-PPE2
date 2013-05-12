package com.example.gsbprojetandroid;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 
public class MainScreenActivity extends Activity{
 
    Button btnViewCR;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
 
        // Buttons
        btnViewCR = (Button) findViewById(R.id.btnViewCR);
 
        // view products click event
        btnViewCR.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllCRActivity.class);
                startActivity(i);
 
            }
        });

    }
}