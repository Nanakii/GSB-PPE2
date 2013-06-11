package com.example.gsbprojetandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class AllCRActivity extends ListActivity {
	
	protected static int id_visiteur = 0;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Création du JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> crList;
 
    // url pour récupérer les comptes-rendus
    private static String url_all_cr = "http://192.168.1.10/android/compterendus.php";
 
    // JSON TAG
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CR = "compteRendus";
    private static final String TAG_ID = "id_compterendu";
    private static final String TAG_MOTIF = "motif";
    private static final String TAG_BILAN = "bilan";
    private static final String TAG_PRATICIEN = "praticien";
 
    // Comptes-rendus
    JSONArray compteRendus = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_cr);
        
        Bundle b = getIntent().getExtras();
        id_visiteur = b.getInt("id_visiteur");
 
        // Hashmap  ListView
        crList = new ArrayList<HashMap<String, String>>();
 
        // Chargement des données en arrière-plan
        new LoadAllCR().execute();
 
        ListView lv = getListView(); 
    }
 

 
    /**
     * Tache en arrière-plan pour charger les données via une requete HTTP asynchrone
     * */
    class LoadAllCR extends AsyncTask<String, String, String> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllCRActivity.this);
            pDialog.setMessage("Récupération des comptes-rendus...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * Récupération des comptes-rendus
         * */
        protected String doInBackground(String... args) {
            // Liste des paramètres à passer
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id_visiteur", String.valueOf(id_visiteur)));
            
            // Récupération de l'URL
            JSONObject json = jParser.getJSONFromUrl(url_all_cr, params);
 
            // Log
            Log.d("Tous les comptes-rendus : ", json.toString());
 
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // Récupération des comptes-rendus
                	compteRendus = json.getJSONArray(TAG_CR);
 
                    // boucle sur les comptes-rendus
                    for (int i = 0; i < compteRendus.length(); i++) {
                        JSONObject c = compteRendus.getJSONObject(i);
 
                        // On récupère chaque donnée associé au compte-rendu
                        String id = c.getString(TAG_ID);
                        String motif = c.getString(TAG_MOTIF);
                        String bilan = c.getString(TAG_BILAN);
                        String praticien = c.getString(TAG_PRATICIEN);
 
                        // Création HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // On ajoute chaque donnée au HashMap
                        map.put(TAG_ID, id);
                        map.put(TAG_MOTIF, motif);
                        map.put(TAG_BILAN, bilan);
                        map.put(TAG_PRATICIEN, praticien);
 
                        // On ajoute le HashMap à la ListView
                        crList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }

        protected void onPostExecute(String file_url) {
            // on supprime la popup une fois le chargement terminé
            pDialog.dismiss();
            
            // On met à jour l'interface utilisateur
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllCRActivity.this, crList,
                            R.layout.list_item, new String[] { TAG_ID,
                            		TAG_MOTIF, TAG_PRATICIEN, TAG_BILAN},
                            new int[] { R.id.id, R.id.motif, R.id.praticien, R.id.bilan });
                    
                    // Mise à jour de la ListView
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}