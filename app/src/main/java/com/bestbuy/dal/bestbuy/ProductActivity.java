package com.bestbuy.dal.bestbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ProductActivity extends AppCompatActivity {


    ArrayList<HashMap<String, String>> mCategoryListValues = new ArrayList<HashMap<String, String>>();
    String productURL = "";
    private CoordinatorLayout coordinatorLayout;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView ProductList = (ListView) findViewById(R.id.productList);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Intent intent = getIntent();
        mCategoryListValues = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("categoryCollection");

        final ArrayList<String> mCategoryDisplayList = new ArrayList<String>();
        for (HashMap<String, String> map : mCategoryListValues) {
            String tagName = map.get("name");
            mCategoryDisplayList.add(tagName);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mCategoryDisplayList);
        ProductList.setAdapter(adapter);

        ProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String text = (String) ProductList.getItemAtPosition(position);
                HashMap<String, String> categoryID = mCategoryListValues.get(position);
                String catid = categoryID.get("id");
                productURL = "http://api.remix.bestbuy.com/v1/products(categoryPath.id="+catid+")?format=json&show=sku,name,regularPrice,image&apiKey=3amgbj6kp9wfage4ka2k2f44";
                getProductList(productURL);

            }

        });
    }

    public void getProductList(String URL) {
        pDialog = new ProgressDialog(ProductActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.get(URL)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        DataManager.getInstance().products = parseJson(response);
                        if(DataManager.getInstance().products.size() == 0){
                            //Toast.makeText(getApplicationContext(), "No Products Found", Toast.LENGTH_SHORT).show();
                            Snackbar mSnackbar = Snackbar
                                    .make(coordinatorLayout, R.string.product_error_info, Snackbar.LENGTH_LONG);
                            mSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            TextView snackTextView = (TextView) (mSnackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                            snackTextView.setTextColor(Color.WHITE);
                            snackTextView.setTextSize( 20 );
                            mSnackbar.show();
                        }else{
                            Intent productIntent = new Intent(ProductActivity.this, ProductlistActivity.class);
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

    public ArrayList parseJson(String data) {
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
                    String sku = c.getString("sku");

                    ProductModel product = new ProductModel(image,name, price,sku);


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


