package com.example.gsbprojetandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
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
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> crList;
 
    // url to get all products list
    private static String url_all_cr = "http://192.168.1.10/android/compterendus.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CR = "compteRendus";
    private static final String TAG_ID = "id_compterendu";
    private static final String TAG_MOTIF = "motif";
 
    // products JSONArray
    JSONArray products = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_cr);
 
        // Hashmap for ListView
        crList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllCR().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
        /*
         * lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.id)).getText()
                        .toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditProductActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);
 
                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });
        */
 
    }
 
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllCR extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllCRActivity.this);
            pDialog.setMessage("R�cup�ration des comptes-rendus...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url_all_cr);
 
            // Check your log cat for JSON reponse
            Log.d("Tous les comptes-rendus : ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_CR);
 
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String motif = c.getString(TAG_MOTIF);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_MOTIF, motif);
 
                        // adding HashList to ArrayList
                        crList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllCRActivity.this, crList,
                            R.layout.list_item, new String[] { TAG_ID,
                            		TAG_MOTIF},
                            new int[] { R.id.id, R.id.motif });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}