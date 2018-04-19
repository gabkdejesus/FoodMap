package org.compsat.carlo.foodmap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestaurantActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SearchResult mSearchResult;
    private Restaurant mRestaurant;

    @BindView(R.id.restaurantNameLabel) TextView restaurantName;
    @BindView(R.id.locationLabel) TextView location;
    @BindView(R.id.ratingLabel) TextView rating;
    @BindView(R.id.cuisines) TextView cuisines;
    @BindView(R.id.cost) TextView cost;
    @BindView(R.id.featureImage) ImageView featureImage;
    @BindView(R.id.menuButton) Button menuButton;
    @BindView(R.id.photosButton) Button photosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        ButterKnife.bind(this);

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
        final String apiKey = "becb791fe312a980dd1010bff53244c2";
        String lat = "14.649674";
        String lon = "121.074911";
        String query = "silantro";

        String searchURL = "https://developers.zomato.com/api/v2.1/search?" +
                "&q=" + query +
                "&count=1" +
                "&lat=" + lat +
                "&lon=" + lon;

        final OkHttpClient client = new OkHttpClient();
        Request searchRequest = new Request.Builder()
                .url(searchURL)
                .addHeader("user-key", apiKey)
                .build();

        Call searchCall = client.newCall(searchRequest);
        searchCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                updateDisplay();
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
                catch (Exception e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }

    private void updateDisplay() {
        restaurantName.setText(mRestaurant.getRestaurantName());
        location.setText(mRestaurant.getLocality());
        rating.setText(mRestaurant.getRating());
        cuisines.setText(mRestaurant.getCuisines());
        cost.setText(mRestaurant.getAveCost() + ".00");
        Picasso.get().load(mRestaurant.getFeaturedImage()).into(featureImage);
    }

    @OnClick(R.id.photosButton)
    public void goToPhotos() {
        Uri uri = Uri.parse(mRestaurant.getPhotosURL());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchBrowser);
    }

    @OnClick(R.id.menuButton)
    public void goToMenu() {
        Uri uri = Uri.parse(mRestaurant.getMenuURL());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchBrowser);
    }
}
