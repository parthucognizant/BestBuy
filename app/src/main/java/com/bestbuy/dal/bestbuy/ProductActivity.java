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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final ListView ProductList = (ListView) findViewById(R.id.productList);

        Intent intent = getIntent();
        ArrayList<HashMap<String, String>> mCategoryListValues = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("categoryCollection");

        final ArrayList<String> mCategoryDisplyListlist = new ArrayList<String>();
        for(HashMap<String, String> map : mCategoryListValues)
        {
            String tagName = map.get("name");
            mCategoryDisplyListlist.add(tagName);
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, mCategoryDisplyListlist);
        ProductList.setAdapter(adapter);

        ProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                String text = (String) ProductList.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), text + " - " + position, Toast.LENGTH_SHORT).show();

            }

        });
    }
}


