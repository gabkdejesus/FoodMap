package org.compsat.carlo.foodmap;

public class SearchResult {
    private String mRestaurantName, mLocality;
    private int mRestaurantID;

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String locality) {
        mLocality = locality;
    }

    public int getRestaurantID() {
        return mRestaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        mRestaurantID = restaurantID;
    }
}
