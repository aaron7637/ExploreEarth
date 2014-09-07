package com.example.exploreearth.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsFeedAdaptor extends BaseAdapter {
    private Activity activity;
    private ArrayList<Entry> entries;
    private static LayoutInflater inflater = null;

    public NewsFeedAdaptor(Activity a, ArrayList<Entry> entries) {
        activity = a;
        this.entries = entries;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return entries.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.news_feed_list_item, null);
        }

        TextView newsTitleTextView = (TextView) vi.findViewById(R.id.title);
        TextView newsDescriptionTextView = (TextView) vi.findViewById(R.id.summary);
        newsTitleTextView.setText(entries.get(position).title);
        newsDescriptionTextView.setText(Html.fromHtml(entries.get(position).summary));

        return vi;
    }
}