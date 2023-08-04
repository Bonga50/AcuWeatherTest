package com.vcsandton.siyabonga.acuweathertest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.vcsandton.siyabonga.acuweathertest.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TextView tvweather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvweather = findViewById(R.id.tv_weather);

        // Call the AsyncTask to fetch weather data
        new FetchWeatherData().execute();
    }

    private class FetchWeatherData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            URL url = buildURLForWeather();
            if (url != null) {
                try {
                    return NetworkUtil.getResponseFromHttpUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String weatherData) {
            if (weatherData != null) {
                tvweather.setText(weatherData);
            } else {
                Log.e("MainActivity", "Weather data is null");
            }
        }
    }

    private URL buildURLForWeather() {
        final String WEATHERBASE_URL = "https://api.accuweather.com/data/1.0/currentconditions/v1/123456";
        final String PARAM_API_KEY = "api_key";
        final String PARAM_METRIC = "metric";

        Uri buildUri = Uri.parse(WEATHERBASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.ACCUWEATHER_API_KEY) // Replace with your API key
                .appendQueryParameter(PARAM_METRIC, "true") // Set this to "true" if you want metric units
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i("MainActivity", "buildURLForWeather: " + url);
        return url;
    }
}