package com.bestbuy.dal.bestbuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class StoreActivity extends AppCompatActivity {

    // Google Map
    private GoogleMap googleMap;
    Button storeSearch;
    private ProgressDialog pDialog;
    private CoordinatorLayout coordinatorLayout;
    EditText editPostal;
    String postalCode = "";
    private String storeURL = "https://api.bestbuy.com/v1/stores(postalCode={postalCode})?format=json&&show=name,lat,lng,phone&apiKey=3amgbj6kp9wfage4ka2k2f44";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mapCoordinator);

        editPostal = (EditText) findViewById(R.id.editTextStoresearch);

        storeSearch = (Button) findViewById(R.id.btnStoreSearch);
        storeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postalCode = editPostal.getText().toString();
                if (postalCode.isEmpty()) {
                    editPostal.setError("Enter postal code");
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editPostal.getWindowToken(), 0);
                    getStoreList(storeURL);
                }

            }
        });

        try {
            // Loading map
            initMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapStore)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }

    public void getStoreList(String URL) {
        pDialog = new ProgressDialog(StoreActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        AndroidNetworking.get(URL)
                .addPathParameter("postalCode", postalCode)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        parseJson(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), anError.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Data", anError.toString(), anError);
                    }
                });
    }

    public void parseJson(String data) {
        ArrayList<StoreModel> stores = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (data != null) {
            try {
                JSONObject jsonObj = new JSONObject(data);

                // Getting JSON Array node
                JSONArray mStoresList = jsonObj.getJSONArray("stores");
                if (mStoresList.length() > 0) {
                    // looping through All Categories
                    for (int i = 0; i < mStoresList.length(); i++) {
                        JSONObject c = mStoresList.getJSONObject(i);

                        String name = c.getString("name");
                        double latitude = c.getDouble("lat");
                        double longitude = c.getDouble("lng");
                        String phone = c.getString("phone");

                        StoreModel store = new StoreModel(name, latitude, longitude);

                        if (googleMap != null) {
                            Marker mark = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(name)
                                    .snippet("Phone:" + phone)
                                    .icon(BitmapDescriptorFactory
                                            .fromResource(R.drawable.ic_bb_map)));

                            builder.include(mark.getPosition());
                        }

                        // adding Categories to Categories list
                        stores.add(store);
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 30; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    googleMap.animateCamera(cu);
                } else {
                    Snackbar mSnackbar = Snackbar
                            .make(coordinatorLayout, "No Stores", Snackbar.LENGTH_LONG);
                    mSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    TextView snackTextView = (TextView) (mSnackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
                    snackTextView.setTextColor(Color.WHITE);
                    snackTextView.setTextSize(20);
                    mSnackbar.show();
                }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
        //return stores;
    }
}
