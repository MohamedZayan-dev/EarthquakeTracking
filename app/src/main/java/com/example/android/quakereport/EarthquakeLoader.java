package com.example.android.quakereport;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeInfo>> {

    private String mUrl;
    public EarthquakeLoader(Context context,String url) {
        super(context);
        mUrl=url;
    }

    @Override
    public List<EarthquakeInfo> loadInBackground() {
        if(mUrl==null){
            return null;

        }
        List<EarthquakeInfo> earthquakes=QueryUtils.fetchdata(mUrl);


        return earthquakes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();


    }
}
