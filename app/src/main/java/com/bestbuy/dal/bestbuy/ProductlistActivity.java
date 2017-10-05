package com.bestbuy.dal.bestbuy;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ProductlistActivity extends AppCompatActivity {

    ListView productList;

    public static final String[] names = new String[] { "Elite Screens - Spectrum Electric Projection Screen - White",
            "APC - AV J-Type 1kVA Power Conditioner / UPS Battery Backup Device - Black", "Niles - Remote Control Anywhere! Kit",
            "Bell'O - Triple Play TV Stand for Flat-Panel TVs Up to 46\\\" - Cherry" };

    public static final String[] price = new String[] {
            "$340", "$520", "$900", "$100" };

    public static final Integer[] images = { R.drawable.tv,
            R.drawable.tv, R.drawable.tv, R.drawable.tv };

    List<ProductModel> productListValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);

        productListValues = new ArrayList<ProductModel>();
        for (int i = 0; i < names.length; i++) {
            ProductModel item = new ProductModel(images[i], names[i], price[i]);
            productListValues.add(item);
        }

        productList = (ListView)findViewById(R.id.productList);
        ProductAdapter mProductAdapter = new ProductAdapter(this,productListValues);
        productList.setAdapter(mProductAdapter);

    }
}
