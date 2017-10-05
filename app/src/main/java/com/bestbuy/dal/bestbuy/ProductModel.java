package com.bestbuy.dal.bestbuy;


public class ProductModel {

    String productName;
    String productPrice;
    int productImage;

    public ProductModel(int productImage,String productName,String productPrice){
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName =productName;
    }

    public  String getProductPrice(){
        return  productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductImage(){
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }
}
