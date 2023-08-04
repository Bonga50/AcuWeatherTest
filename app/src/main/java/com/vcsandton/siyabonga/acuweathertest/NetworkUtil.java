package com.vcsandton.siyabonga.acuweathertest;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {
    public static String WEATHERBASE_URL =
            "https://dataservice.accuweather.com/forecasts/v1/daily/5day/305605";
    public static String PARAM_METRIC = "metric";
    public static String METRIC_VALUE = "true";
    public static String PARAM_API_KEY = "apikey";
    public static String LOGGING_TAG = "URLWECREATED";

    public NetworkUtil() {
    }



    public static URL buildURLForWeather() {
        Uri buildUri = Uri.parse(WEATHERBASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.ACCUWEATHER_API_KEY) // passing in api key
                .appendQueryParameter(PARAM_METRIC, METRIC_VALUE) // passing in metric as measurement unit
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(LOGGING_TAG, "buildURLForWeather: " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
        try {

            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("//A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
            }
            return null;
        } finally {
            urlConnection.disconnect();
        }
    }

}
