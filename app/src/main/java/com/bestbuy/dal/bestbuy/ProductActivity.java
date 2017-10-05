package com.bestbuy.dal.bestbuy;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductActivity extends AppCompatActivity {


    ArrayList<HashMap<String, String>> mCategoryListValues = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView ProductList = (ListView) findViewById(R.id.productList);

        Intent intent = getIntent();
        mCategoryListValues = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("categoryCollection");

        final ArrayList<String> mCategoryDisplayList = new ArrayList<String>();
        for(HashMap<String, String> map : mCategoryListValues)
        {
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
                Toast.makeText(getApplicationContext(), text + " - " + catid, Toast.LENGTH_SHORT).show();
                Intent productIntent = new Intent(ProductActivity.this, ProductlistActivity.class);
                startActivity(productIntent);

            }

        });
    }
}


