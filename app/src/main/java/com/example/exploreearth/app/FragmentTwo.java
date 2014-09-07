package com.example.exploreearth.app;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.AsyncTask;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class FragmentTwo extends Fragment {
    private static final String TAG = "News Feed Activity";
    ArrayList<Entry> entries = null;
    private ListView list;
    private NewsFeedAdaptor adapter;

    public FragmentTwo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_two, container, false);
        list = (ListView) view.findViewById(R.id.list);
        new MyAsyncTask().execute();
        return view;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        // String... arg0 is the same as String[] args
        protected String doInBackground(String... args) {

            InputStream in = null;
            String country = ChosenCountry.getCountry();
            CountryDatabaseHandler countryHandler = new CountryDatabaseHandler(
                    FragmentTwo.this.getActivity());
            final String urlStr = countryHandler.getField(country, CountryDatabaseHandler.COUNTRYFIELDS.URL);

            try {
                URL url = new URL(urlStr);
                URLConnection connection;
                connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                int responseCode = httpConnection.getResponseCode();

                // Tests if responseCode == 200 Good Connection
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = httpConnection.getInputStream();
                    entries = RSSParser.parse(in);
                    return "ok";
                }
            } catch (MalformedURLException e) {
                Log.d(TAG, "MalformedURLException", e);
            } catch (IOException e) {
                Log.d(TAG, "IOException", e);
            } catch (XmlPullParserException e) {
                Log.d(TAG, "XmlPullParserException", e);
                e.printStackTrace();
            } finally {
            }
            return null;
        }

        // Changes the values for a bunch of TextViews on the GUI
        protected void onPostExecute(String result) {
            // Getting adapter by passing xml data ArrayList
            if (result.equals("ok")) {
                adapter = new NewsFeedAdaptor(getActivity(), entries);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view,
                                            int pos, long id) {
                        final Entry e = entries.get(pos);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(e.link));
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
