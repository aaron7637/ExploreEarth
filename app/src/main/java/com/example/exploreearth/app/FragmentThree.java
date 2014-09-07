package com.example.exploreearth.app;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentThree extends Fragment {
    private WebView foodWebView;
    private static final String baseUrl = "http://globaltableadventure.com/category/";

    public FragmentThree() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_three, container,
                false);

        // Initialize Components
        foodWebView = (WebView) view.findViewById(R.id.displayFoodWebView);
        foodWebView.getSettings().setJavaScriptEnabled(true);
        foodWebView.getSettings().setBuiltInZoomControls(true);
        foodWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        new MyAsyncTask().execute();
        return view;
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        // String... arg0 is the same as String[] args
        protected String doInBackground(String... args) {
            String url = baseUrl;
            String country = ChosenCountry.getCountry();
            CountryDatabaseHandler countryHandler = new CountryDatabaseHandler(getActivity());
            String food = countryHandler.getField(country, CountryDatabaseHandler.COUNTRYFIELDS.FOOD);

            if (country.compareToIgnoreCase("Paraguay") < 0) url += "countries/";
            else {
                String continent = countryHandler.getField(country, CountryDatabaseHandler.COUNTRYFIELDS.CONTINENTNAME);
                url += "continents/" + continent + "/";
            }

            if (food.equals("")) url += country + "/";
            else url += food + "/";
            return url;
        }

        // Changes the values for a bunch of TextViews on the GUI
        protected void onPostExecute(String result) {
            foodWebView.loadUrl(result);
        }
    }
}
