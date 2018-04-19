package org.compsat.carlo.foodmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
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

    private SearchResult mSearchResult;
    private Restaurant mRestaurant;

    private final String apiKey = "becb791fe312a980dd1010bff53244c2";
    private String lat, lon, query, searchURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        lat = "14.649674";
        lon = "121.074911";
        query = "silantro";

        searchURL = "https://developers.zomato.com/api/v2.1/search?" +
                "&q=" + query +
                "&count=1" +
                "&lat=" + lat +
                "&lon=" + lon;

        if(isNetworkAvailable()) {
            getData();
        }
        else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    private Restaurant getRestaurantDetails(String jsonData) throws JSONException {
        JSONObject restaurantJSON = new JSONObject(jsonData);
        JSONObject location = restaurantJSON.getJSONObject("location");
        JSONObject rating = restaurantJSON.getJSONObject("user_rating");

        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantName(restaurantJSON.getString("name"));
        restaurant.setLocality(location.getString("locality_verbose"));
        restaurant.setCuisines(restaurantJSON.getString("cuisines"));
        restaurant.setAveCost(restaurantJSON.getInt("average_cost_for_two"));
        restaurant.setRating(rating.getString("aggregate_rating"));
        restaurant.setPhotosURL(restaurantJSON.getString("photos_url"));
        restaurant.setMenuURL(restaurantJSON.getString("menu_url"));
        restaurant.setFeaturedImage(restaurantJSON.getString("featured_image"));

        return restaurant;

    }

    private SearchResult getSearchDetails(String jsonData) throws JSONException {
        JSONObject searchJSON = new JSONObject(jsonData);
        JSONArray restaurantsArray = searchJSON.getJSONArray("restaurants");
        JSONObject restaurants = restaurantsArray.getJSONObject(0);
        JSONObject restaurant = restaurants.getJSONObject("restaurant");
        JSONObject rID = restaurant.getJSONObject("R");

        SearchResult searchResult = new SearchResult();

        searchResult.setRestaurantID(rID.getInt("res_id"));

        return searchResult;
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

    private void getData() {
        final OkHttpClient client = new OkHttpClient();
        Request searchRequest = new Request.Builder()
                .url(searchURL)
                .addHeader("user-key", apiKey)
                .build();

        Call searchCall = client.newCall(searchRequest);
        searchCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TETETE", "TETETETE");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        mSearchResult = getSearchDetails(jsonData);

                        int restaurantID = mSearchResult.getRestaurantID();
                        String restaurantURL = "https://developers.zomato.com/api/v2.1/restaurant?" +
                                "res_id=" + restaurantID;

                        Request restaurantRequest = new Request.Builder()
                                .url(restaurantURL)
                                .addHeader("user-key", apiKey)
                                .build();

                        Call restaurantCall = client.newCall(restaurantRequest);
                        restaurantCall.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String jsonData = response.body().string();
                                    Log.v(TAG, jsonData);
                                    if (response.isSuccessful()) {
                                        mRestaurant = getRestaurantDetails(jsonData);
                                    }
                                }
                                catch (Exception e) {
                                    Log.e(TAG, "Exception caught: ", e);
                                }
                            }
                        });
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }
}
