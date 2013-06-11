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
         
        // On récupère l'id de l'utilisateur
        Bundle b = getIntent().getExtras();
        id_visiteur = b.getInt("id_visiteur");

        setContentView(R.layout.dashboard);
        
        // Ajout des boutons
        btnViewCR = (Button) findViewById(R.id.btnViewCR);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        
        // Bouton pour voir les comptes-rendus
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
         
        // Bouton pour se déconnecter
        btnLogout.setOnClickListener(new View.OnClickListener() {
             
            public void onClick(View arg0) {
                Intent login = new Intent(getApplicationContext(), MainScreenActivity.class);
                
                // Permet de supprimer tous les layers
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);

		        finish();
            }
        });
       
    }
}