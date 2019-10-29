/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

    import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
    import android.content.SharedPreferences;
    import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
    import android.preference.PreferenceFragment;
    import android.preference.PreferenceManager;
    import android.support.v7.app.AppCompatActivity;
import android.util.Log;
    import android.view.Menu;
    import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
    import android.view.Menu;
    import android.view.MenuItem;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeInfo>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String url="https://earthquake.usgs.gov/fdsnws/event/1/query";
    private TextView emptyView;
private EarthquakeAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);



            // Find a reference to the {@link ListView} in the layout
             ListView earthquakeListView = (ListView) findViewById(R.id.list);

            // Create a new {@link ArrayAdapter} of earthquakes
            mAdapter = new EarthquakeAdapter(getApplication(), new ArrayList<EarthquakeInfo>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(mAdapter);
      emptyView  =(TextView)findViewById(R.id.emptyElement);
            earthquakeListView.setEmptyView(emptyView);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

            {
                @Override
                public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
                    EarthquakeInfo currentEarthquakeInfo = mAdapter.getItem(position);
                    Uri urlUri = Uri.parse(currentEarthquakeInfo.getmUrl());
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, urlUri);
                    startActivity(urlIntent);
                }
            });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected()) {
            LoaderManager loaderManager=getLoaderManager();

            loaderManager.initLoader(0,null,this);
        }
            else
        {
            ProgressBar progressBar =(ProgressBar) findViewById(R.id.progress_circular);
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.noInternetConnection);
        }



    }



    @Override
    public Loader<List<EarthquakeInfo>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        String minMagn=sharedPreferences.getString(getString(R.string.MinimumKey),getString(R.string.MinimumDefaultValue));
        String orderBy=sharedPreferences.getString(getString(R.string.ListPreferenceOrderByKey),getString(R.string.ListPreferenceOrderByDefault));
        Uri baseUrl=Uri.parse(url);
        Uri.Builder builder=baseUrl.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("minmag",minMagn);
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this,builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeInfo>> loader, List<EarthquakeInfo> data) {
        emptyView.setText(R.string.emptyView);

        ProgressBar progressBar =(ProgressBar) findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.GONE);

        mAdapter.clear();

        if(data!=null&&!data.isEmpty()) {
            mAdapter.addAll(data);
        }

   }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeInfo>> loader) {

        mAdapter.clear();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.settings){
            Intent intent =new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item); //false
    }
}


