package com.bestbuy.dal.bestbuy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<ProductModel> dataSet;
    Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.proName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.proPrice);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.proImage);
        }
    }

    public CardAdapter(Context context,List<ProductModel> data) {
        this.context = context;
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_productlist_card, parent, false);

        //view.setOnClickListener(ProductlistCard_Activity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;



        textViewName.setText(dataSet.get(listPosition).getProductName());
        textViewVersion.setText(dataSet.get(listPosition).getProductPrice());
        Picasso.with(context).load(dataSet.get(listPosition).getProductImage()).placeholder(R.drawable.loading).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}