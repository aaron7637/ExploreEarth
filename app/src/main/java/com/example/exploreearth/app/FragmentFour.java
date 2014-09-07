package com.example.exploreearth.app;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentFour extends Fragment {
    private WebView wikiWebView;
    private static final String baseUrl = "http://en.wikipedia.org/wiki/";

    public FragmentFour() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_four, container,
                false);

        // Initialize Components
        wikiWebView = (WebView) view.findViewById(R.id.displayWikiWebView);
        wikiWebView.getSettings().setJavaScriptEnabled(true);
        wikiWebView.getSettings().setBuiltInZoomControls(true);
        wikiWebView.setWebViewClient(new WebViewClient() {
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
            String country = ChosenCountry.getCountry();
            country.replace(' ', '_');
            String url = baseUrl + country;
            return url;
        }

        // Changes the values for a bunch of TextViews on the GUI
        protected void onPostExecute(String result) {
            wikiWebView.loadUrl(result);
        }
    }
}
