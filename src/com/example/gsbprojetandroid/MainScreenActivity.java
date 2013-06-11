package com.example.gsbprojetandroid;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class MainScreenActivity extends Activity {
    Button btnLogin;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;
    
    private static int id_visiteur = 0;
    
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Création du JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> crList;
 
    // url de connexion
    private static String url_login	 = "http://192.168.1.10/android/login.php";
 
    // JSON TAG
    private static final String TAG_SUCCESS 	= "success";
    private static final String TAG_ERROR 		= "error";
    private static final String TAG_ERROR_MSG 	= "error_msg";
    private static final String TAG_ID 			= "id_visiteur";
    private static final String TAG_USERNAME 	= "username";
    
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
 
        // Création du formulaire
        inputUsername = (EditText) findViewById(R.id.loginUsername);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
 
        // Evenement sur le bouton de connexion
        btnLogin.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                new LoadLogin().execute();
            }
        });
    }
    

	/**
	 * Tache en arrière-plan pour charger les données via une requete HTTP asynchrone
	 * */
	class LoadLogin extends AsyncTask<String, String, String> {
	
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(MainScreenActivity.this);
	        pDialog.setMessage("Connexion en cours...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        pDialog.show();
	    }
	
	    /**
	     * connexion de l'utilisateur
	     * */
	    protected String doInBackground(String... args) {
            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();
            
	        // Paramètres a passer a la requete
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("username", username));
	        params.add(new BasicNameValuePair("password", password));
	        
	        // on récupère l'URL
	        JSONObject json = jParser.getJSONFromUrl(url_login, params);
	
	        // Log
	        Log.d("Connexion en cours : ", json.toString());

	        try {
	            int success = json.getInt(TAG_SUCCESS);
	
	            // si identifiants validés par la requete HTTP
	            if (success != 1) {
	            	id_visiteur = 0;
	    	        cancel(true);

        	        pDialog.dismiss();
                    loginErrorMsg.setText("Identifiants incorrects.");
                }
	            else {
	            	id_visiteur = json.getInt(TAG_ID);
	            }
	            
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	
	        return null;
	    }
	
	    protected void onPostExecute(String file_url) {
	        pDialog.dismiss();

	        // Si la connexion s'est bien déroulée
	        if (id_visiteur > 0) {
	            // On charge la prochaine vue (le dashboard)
	            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
		        
	            // On donne l'id du visiteur a la prochaine vue
		        Bundle b = new Bundle();
		        b.putInt("id_visiteur", id_visiteur);
		        dashboard.putExtras(b);
		         
		        // On ferme toutes les vues précédentes
		        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(dashboard);
		        
		        finish();
	        }
	    }

	
	}
}