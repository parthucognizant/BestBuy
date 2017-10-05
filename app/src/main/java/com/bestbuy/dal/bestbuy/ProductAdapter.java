package com.bestbuy.dal.bestbuy;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    //the list values in the List of type Product
    List<ProductModel> productList;

    //activity context
    Context context;



    //constructor initializing the values
    public ProductAdapter(Context context, List<ProductModel> productList) {
        super();
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView textViewProductName;
        TextView textViewProductPrice;
    }

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //getting the view
        if(convertView == null){

            convertView = mInflater.inflate(R.layout.productlist_row, null);
            holder = new ViewHolder();

            //getting the view elements of the list from the view
            holder.imageView = (ImageView) convertView.findViewById(R.id.product_image);
            holder.textViewProductName = (TextView) convertView.findViewById(R.id.product_name);
            holder.textViewProductPrice = (TextView) convertView.findViewById(R.id.product_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ProductModel productModelValues = (ProductModel) getItem(position);

        //adding values to the list item
        Picasso.with(context).load(productModelValues.getProductImage()).into(holder.imageView);
        //holder.imageView.setImageResource(productModelValues.getProductImage());
        holder.textViewProductName.setText(productModelValues.getProductName());
        holder.textViewProductPrice.setText(productModelValues.getProductPrice());


        //finally returning the view
        return convertView;
    }
}
