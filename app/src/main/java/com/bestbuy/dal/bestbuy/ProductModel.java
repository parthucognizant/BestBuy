package com.bestbuy.dal.bestbuy;


public class ProductModel {

    String productName;
    String productPrice;
    String productImage;
    String productSKU;

    public ProductModel(String productImage,String productName,String productPrice, String productSKU){
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

    public String getProductImage(){
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }
}
