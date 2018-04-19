package org.compsat.carlo.foodmap;

public class Restaurant {
    private String restaurantName, locality, cuisines, rating, photosURL, menuURL, featuredImage;
    private int aveCost;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhotosURL() {
        return photosURL;
    }

    public void setPhotosURL(String photosURL) {
        this.photosURL = photosURL;
    }

    public String getMenuURL() {
        return menuURL;
    }

    public void setMenuURL(String menuURL) {
        this.menuURL = menuURL;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public int getAveCost() {
        return aveCost;
    }

    public void setAveCost(int aveCost) {
        this.aveCost = aveCost;
    }

}
