package com.bestbuy.dal.bestbuy;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

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

import okhttp3.OkHttpClient;

import static com.bestbuy.dal.bestbuy.R.id.coordinatorLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button viewProducts;
    Button searchProducts;
    EditText searchText;
    ArrayList<HashMap<String, String>> categories = new ArrayList<HashMap<String, String>>();
    private ProgressDialog pDialog;
    FloatingActionButton menu1,menu2,menu3;
    String categoryURL = "http://api.remix.bestbuy.com/v1/categories?format=json&show=id,name&apiKey=3amgbj6kp9wfage4ka2k2f44";
    String searchURL = "https://api.bestbuy.com/v1/products(search={item})?format=json&show=productId,name,regularPrice,image&apiKey=3amgbj6kp9wfage4ka2k2f44";
    String searchString = "";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewProducts = (Button) findViewById(R.id.btnProducts);
        viewProducts.setOnClickListener(this);

        searchText = (EditText) findViewById(R.id.search);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.search_coordinator);

        searchProducts =(Button) findViewById(R.id.searchButton);
        searchProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = searchText.getText().toString();
                if(!searchString.isEmpty())
                getProductList(searchURL);
                else
                    searchText.setError("Please enter search keyword");
            }
        });
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
                if(!categories.isEmpty()){
                    categories.clear();
                }
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

    public void getProductList(String URL) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.get(URL)
                .addPathParameter("item", searchString)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        DataManager.getInstance().products = parseProductJson(response);
                        if(DataManager.getInstance().products.size() == 0){
                            //Toast.makeText(getApplicationContext(), "No Products", Toast.LENGTH_SHORT).show();
                            Snackbar mSnackbar = Snackbar
                                    .make(coordinatorLayout, "Products not found", Snackbar.LENGTH_LONG);
                            mSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            TextView snackTextView = (TextView) (mSnackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                            snackTextView.setTextColor(Color.WHITE);
                            snackTextView.setTextSize( 20 );
                            mSnackbar.show();
                        }else{
                            Intent productIntent = new Intent(MainActivity.this, ProductlistActivity.class);
                            startActivity(productIntent);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Data", anError.toString(), anError);
                    }
                });
    }

    public ArrayList parseProductJson(String data) {
        ArrayList<ProductModel> products = new ArrayList<>();
        if (data != null) {
            try {
                JSONObject jsonObj = new JSONObject(data);

                // Getting JSON Array node
                JSONArray mCatecogiesList = jsonObj.getJSONArray("products");

                // looping through All Categories
                for (int i = 0; i < mCatecogiesList.length(); i++) {
                    JSONObject c = mCatecogiesList.getJSONObject(i);

                    String price = "$"+c.getString("regularPrice");
                    String name = c.getString("name");
                    String image = c.getString("image");

                    ProductModel product = new ProductModel(image,name, price);


                    // adding Categories to Categories list
                    products.add(product);
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
        return products;
    }

}
