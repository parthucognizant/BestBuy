package com.bestbuy.dal.bestbuy;

import java.util.ArrayList;


public class DataManager {
    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    public ArrayList<ProductModel> products;
    public ArrayList<StoreModel> stores;
    private DataManager() {
    }
}
