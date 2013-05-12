package com.example.gsbprojetandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends Activity {
    Button btnLogout;
    Button btnViewCR;
    protected static int id_visiteur = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        Bundle b = getIntent().getExtras();
        id_visiteur = b.getInt("id_visiteur");

        setContentView(R.layout.dashboard);
        
        btnViewCR = (Button) findViewById(R.id.btnViewCR);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        
        btnViewCR.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
	            Intent i = new Intent(getApplicationContext(), AllCRActivity.class);
		        
	            // On donne l'id du visiteur a la prochaine vue
		        Bundle b = new Bundle();
		        b.putInt("id_visiteur", id_visiteur);
		        i.putExtras(b);
		        
                startActivity(i);
 
            }
        });
         
        btnLogout.setOnClickListener(new View.OnClickListener() {
             
            public void onClick(View arg0) {
                Intent login = new Intent(getApplicationContext(), MainScreenActivity.class);
                
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);

		        finish();
            }
        });
       
    }
}