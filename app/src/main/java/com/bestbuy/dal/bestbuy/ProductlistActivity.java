package com.bestbuy.dal.bestbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ProductlistActivity extends AppCompatActivity {

    ListView productList;

    List<ProductModel> productListValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        productListValues = DataManager.getInstance().products;

        productList = (ListView)findViewById(R.id.productList);
        ProductAdapter mProductAdapter = new ProductAdapter(this,productListValues);
        productList.setAdapter(mProductAdapter);

    }
}
