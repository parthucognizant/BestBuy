package com.bestbuy.dal.bestbuy;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


import okhttp3.OkHttpClient;

public class ProductActivity extends AppCompatActivity {


    ArrayList<HashMap<String, String>> mCategoryListValues = new ArrayList<HashMap<String, String>>();
    String productURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView ProductList = (ListView) findViewById(R.id.productList);

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
                productURL = "http://api.remix.bestbuy.com/v1/products(categoryPath.id="+catid+")?format=json&show=productId,name,regularPrice,image&apiKey=3amgbj6kp9wfage4ka2k2f44";
                getProductList(productURL);
                //Toast.makeText(getApplicationContext(), text + " - " + catid, Toast.LENGTH_SHORT).show();
                //Intent productIntent = new Intent(ProductActivity.this, ProductlistActivity.class);
                //startActivity(productIntent);

            }

        });
    }

    public void getProductList(String URL) {
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.get(URL)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Success::"+response, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Data", anError.toString(), anError);
                    }
                });
    }

}


