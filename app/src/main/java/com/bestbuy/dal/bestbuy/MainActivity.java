package com.bestbuy.dal.bestbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button viewProducts;
    Button searchProducts, viewStores;
    EditText searchText;
    ArrayList<HashMap<String, String>> categories = new ArrayList<HashMap<String, String>>();
    private ProgressDialog pDialog;


    String categoryURL = "http://api.remix.bestbuy.com/v1/categories?format=json&show=id,name&apiKey=3amgbj6kp9wfage4ka2k2f44";
    String searchURL = "https://api.bestbuy.com/v1/products(search={item})?format=json&show=sku,name,regularPrice,image&pageSize=100&apiKey=3amgbj6kp9wfage4ka2k2f44";
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


        searchProducts = (Button) findViewById(R.id.searchButton);
        searchProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchString = searchText.getText().toString();
                if (!searchString.isEmpty()) {
                    InputMethodManager keyboardManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboardManager.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                    getProductList(searchURL);
                } else {
                    searchText.setError("Please enter search keyword");
                }
            }
        });

        viewStores = (Button) findViewById(R.id.btnStores);
        viewStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapIntent = new Intent(MainActivity.this, StoreActivity.class);
                startActivity(MapIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        viewProducts.setClickable(false);
        getCategoriesList(categoryURL);
    }

    public ArrayList parseJson(String data) {
        if (data != null) {
            try {
                JSONObject jsonObj = new JSONObject(data);

                // Getting JSON Array node
                JSONArray mCatecogiesList = jsonObj.getJSONArray("categories");
                if(!categories.isEmpty()){
                    categories.clear();
                }
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

    public void getCategoriesList(String URL) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.get(URL)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        ArrayList<HashMap<String, String>> mCategories = parseJson(response);
                        //Toast.makeText(getApplicationContext(), mCategories.toString(), Toast.LENGTH_SHORT).show();
                        viewProducts.setClickable(true);
                        Intent MainIntent = new Intent(MainActivity.this, ProductActivity.class);
                        MainIntent.putExtra("categoryCollection", mCategories);
                        startActivity(MainIntent);
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

    public void getProductList(String URL) {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.get(URL)
                .addPathParameter("item", searchString)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        ProductActivity pa = new ProductActivity();
                        DataManager.getInstance().products = pa.parseJson(response);
                        searchText.setText("");
                        if (DataManager.getInstance().products.size() == 0) {
                            //Toast.makeText(getApplicationContext(), "No Products", Toast.LENGTH_SHORT).show();
                            Snackbar mSnackbar = Snackbar
                                    .make(coordinatorLayout, R.string.product_error_info, Snackbar.LENGTH_LONG);
                            mSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            TextView snackTextView = (TextView) (mSnackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                            snackTextView.setTextColor(Color.WHITE);
                            snackTextView.setTextSize(20);
                            mSnackbar.show();
                        } else {
                            Intent productIntent = new Intent(MainActivity.this, listActivity.class);
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
}
