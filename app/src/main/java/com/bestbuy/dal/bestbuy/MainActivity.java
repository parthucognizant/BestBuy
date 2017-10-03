package com.bestbuy.dal.bestbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button viewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewProducts = (Button) findViewById(R.id.getStarted);
        viewProducts.setOnClickListener(this);
    }
    String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu"};

    @Override
    public void onClick(View view) {
        Intent MainIntent = new Intent(MainActivity.this, ProductActivity.class);
        MainIntent.putExtra("stringCollection", values);
        startActivity(MainIntent);
    }
}
