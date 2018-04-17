package org.compsat.carlo.foodmap;

public class Geocode {
    private String mEntityType, mCityName, mCountryName;
    private int mEntityID, mCityID, mCountryID;

    public String getEntityType() {
        return mEntityType;
    }

    public void setEntityType(String entityType) {
        mEntityType = entityType;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String countryName) {
        mCountryName = countryName;
    }

    public int getEntityID() {
        return mEntityID;
    }

    public void setEntityID(int entityID) {
        mEntityID = entityID;
    }

    public int getCityID() {
        return mCityID;
    }

    public void setCityID(int cityID) {
        mCityID = cityID;
    }

    public int getCountryID() {
        return mCountryID;
    }

    public void setCountryID(int countryID) {
        mCountryID = countryID;
    }
}
