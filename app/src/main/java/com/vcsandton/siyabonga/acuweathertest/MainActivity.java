package com.vcsandton.siyabonga.acuweathertest;

import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.LOGGING_TAG;
import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.METRIC_VALUE;
import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.PARAM_API_KEY;
import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.PARAM_METRIC;
import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.WEATHERBASE_URL;

import com.vcsandton.siyabonga.acuweathertest.NetworkUtil;


import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;
import com.vcsandton.siyabonga.acuweathertest.databinding.ActivityMainBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String weather = fetchDataFromWeatherAPI();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvWeather.setText(weather);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private String fetchDataFromWeatherAPI() throws IOException {
        URL url = buildURLForWeather();
        if (url == null) {
            return "";
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    private URL buildURLForWeather() {
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
}