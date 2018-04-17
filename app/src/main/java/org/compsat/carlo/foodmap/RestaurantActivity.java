package org.compsat.carlo.foodmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestaurantActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Geocode mGeocode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        String apikey = "becb791fe312a980dd1010bff53244c2";
        String lat = "14.649674";
        String lon = "121.074911";
        int entityID = mGeocode.getEntityID();
        String entityType = mGeocode.getEntityType();
        String query = "silantro";

        String geocodeURL = "https://developers.zomato.com/api/v2.1/geocode?lat=" + lat + "&lon=" + lon;
        String searchURL = "https://developers.zomato.com/api/v2.1/search?" +
                "entity_id=" + entityID +
                "&entity_type=" + entityType +
                "&q=" + query +
                "&lat=" + lat +
                "&lon=" + lon +
                "&radius=10";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request geocodeRequest = new Request.Builder()
                    .url(geocodeURL)
                    .addHeader("user-key", apikey)
                    .build();

            Call geocodeCall = client.newCall(geocodeRequest);
            geocodeCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mGeocode = getGeocodeDetails(jsonData);
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private Geocode getGeocodeDetails(String jsonData) throws JSONException {
        JSONObject geocodeJSON = new JSONObject(jsonData);
        JSONObject location = geocodeJSON.getJSONObject("location");

        Geocode geocode = new Geocode();

        geocode.setEntityType(location.getString("entity_type"));
        geocode.setEntityID(location.getInt("entity_id"));
        geocode.setCityID(location.getInt("city_id"));
        geocode.setCityName(location.getString("city_name"));
        geocode.setCountryID(location.getInt("country_id"));
        geocode.setCountryName(location.getString("country_name"));

        return geocode;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }
}
