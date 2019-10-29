package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /** Sample JSON response for a USGS query */

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils(){}

    private static URL createUrl(String requestedUrl){
        URL url=null;
        try{
            url=new URL(requestedUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"error creating the url");
        }
            return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String output=bufferedReader.readLine();
            while(output!=null){
                stringBuilder.append(output);
                output=bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static String MakeHttpRequest(URL url) throws IOException{
            String jsonResponse="";
            if(url==null)
                return jsonResponse;
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"error reteriving data");
        }
        finally {
            if(httpURLConnection!=null)
                httpURLConnection.disconnect();
            if(inputStream!=null)
                inputStream.close();
        }
        return jsonResponse;
    }

    private static ArrayList<EarthquakeInfo> extractFeatureFromJson(String json){
        if(TextUtils.isEmpty(json)){
            return null;
        }
        ArrayList<EarthquakeInfo> earthquakes = new ArrayList<>();
        try {

            JSONObject jsonObject=new JSONObject(json);

            JSONArray jsonArray= jsonObject.getJSONArray("features");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject current= jsonArray.getJSONObject(i);
                JSONObject properties =  current.getJSONObject("properties");
                double magnitude= properties.getDouble("mag");
                String Place=properties.getString("place");
                Long time=properties.getLong("time");
                String url=properties.getString("url");

                earthquakes.add(new EarthquakeInfo(magnitude,Place,time,url));

            }
        }catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        /**
         * Return a list of {@link EarthquakeInfo} objects that has been built up from
         * parsing a JSON response.
         */
        return earthquakes;

    }


    public static ArrayList<EarthquakeInfo> fetchdata(String requestedurl){
        URL url=createUrl(requestedurl);
            String jsonResponse=null;
        try{
            jsonResponse=MakeHttpRequest(url);
}       catch (IOException e){
                Log.e(LOG_TAG,"error making the connection");
}
            ArrayList<EarthquakeInfo> earthquakeInfoArrayList=extractFeatureFromJson(jsonResponse);
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){e.printStackTrace();}


                return earthquakeInfoArrayList;
    }

}