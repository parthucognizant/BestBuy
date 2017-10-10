package com.bestbuy.dal.bestbuy;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    //the list values in the List of type Product
    List<ProductModel> productList;

    //activity context
    Context context;

    Animation scaleUp;



    //constructor initializing the values
    public ProductAdapter(Context context, List<ProductModel> productList) {
        super();
        this.context = context;
        this.productList = productList;
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up_fast);
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
            convertView.startAnimation(scaleUp);
        }

        ProductModel productModelValues = (ProductModel) getItem(position);

        //adding values to the list item
        Picasso.with(context).load(productModelValues.getProductImage()).placeholder(R.drawable.loading).into(holder.imageView);
        //holder.imageView.setImageResource(productModelValues.getProductImage());
        holder.textViewProductName.setText(productModelValues.getProductName());
        holder.textViewProductPrice.setText(productModelValues.getProductPrice());


        //finally returning the view
        return convertView;
    }
}
