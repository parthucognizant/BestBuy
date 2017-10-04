package com.bestbuy.dal.bestbuy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button viewProducts;
    ArrayList<HashMap<String, String>> categories = new ArrayList<HashMap<String, String>>();
    private ProgressDialog pDialog;
    String categoryURL = "http://api.remix.bestbuy.com/v1/categories?format=json&show=id,name&apiKey=3amgbj6kp9wfage4ka2k2f44";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewProducts = (Button) findViewById(R.id.getStarted);
        viewProducts.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        viewProducts.setClickable(false);
        new LongRunningGetIO().execute();
    }

    private class LongRunningGetIO extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();


            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);


                if (n > 0) out.append(new String(b, 0, n));
            }


            return out.toString();
        }


        @Override


        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(categoryURL);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);


                HttpEntity entity = response.getEntity();


                text = getASCIIContentFromEntity(entity);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }


            return text;
        }


        protected void onPostExecute(String results) {
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if (results != null) {
                ArrayList<HashMap<String, String>> mCategories = parseJson(results);
                //Toast.makeText(getApplicationContext(), mCategories.toString(), Toast.LENGTH_SHORT).show();
                viewProducts.setClickable(true);
                Intent MainIntent = new Intent(MainActivity.this, ProductActivity.class);
                MainIntent.putExtra("categoryCollection", mCategories);
                startActivity(MainIntent);
            }

        }


    }


    public ArrayList parseJson(String data) {
        if (data != null) {
            try {
                JSONObject jsonObj = new JSONObject(data);

                // Getting JSON Array node
                JSONArray mCatecogiesList = jsonObj.getJSONArray("categories");

                // looping through All Categories
                for (int i = 0; i < mCatecogiesList.length(); i++) {
                    JSONObject c = mCatecogiesList.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");

                    // tmp hash map for single Categories
                    HashMap<String, String> mSingleCategory = new HashMap<>();

                    // adding each child node to HashMap key => value
                    mSingleCategory.put("id", id);
                    mSingleCategory.put("name", name);

                    // adding Categories to Categories list
                    categories.add(mSingleCategory);
                }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
        return categories;
    }
}
