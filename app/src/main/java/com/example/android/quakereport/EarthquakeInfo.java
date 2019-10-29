package com.example.android.quakereport;


public class EarthquakeInfo {

    private double mMagnitude;
    private String mPlace;
    private Long mDate;
    private String mUrl;
    public EarthquakeInfo(double Magnitude,String Place, Long Date, String url){
        mMagnitude=Magnitude;
        mPlace=Place;
        mDate=Date;
        mUrl=url;
    }

    public double getmMagnitude(){
        return mMagnitude;
    }

    public String getmPlace(){
        return mPlace;
    }

    public Long getmDate(){
        return mDate;
    }

    public String getmUrl(){
        return mUrl;
    }
}
