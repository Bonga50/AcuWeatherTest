package com.vcsandton.siyabonga.acuweathertest;

import static com.vcsandton.siyabonga.acuweathertest.NetworkUtil.buildURLForWeather;

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

        tvweather = binding.tvWeather; // Make sure the ID matches your layout

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
                    Log.e("FetchWeatherData",  e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String weatherData) {
            if (weatherData != null) {
                tvweather.setText(weatherData);
            } else {
                Log.e("FetchWeatherData", "Weather data is null");
            }
        }
    }





    private String readTextFromURL(URL url) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }catch (IOException e){Log.w("myApp", e.toString());;}
        return content.toString();
    }


}