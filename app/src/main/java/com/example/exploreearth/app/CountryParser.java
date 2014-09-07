package com.example.exploreearth.app;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public class CountryParser {
    CountryDatabaseHandler countryHandler;

    public CountryParser(Context context) {
        countryHandler = new CountryDatabaseHandler(context);
    }

    public void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser);
            return;
        } finally {
            in.close();
        }
    }

    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            // Starts by looking for the entry tag
            if (name.equals("country")) {
                readEntry(parser);
            } else if (name.equals("geonames")) {
                parser.next();
            } else {
                skip(parser);
            }
        }

        return;
    }

    private void readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        String countryName = "";
        String continentName = "";
        String capital = "";
        String population = "";
        String currencyCode = "";
        String url = "";
        String food = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("countryName")) {
                countryName = readText(parser);
            } else if (name.equals("continentName")) {
                continentName = readText(parser);
            } else if (name.equals("capital")) {
                capital = readText(parser);
            } else if (name.equals("population")) {
                population = readText(parser);
            } else if (name.equals("currencyCode")) {
                currencyCode = readText(parser);
            } else if (name.equals("url")) {
                url = readText(parser);
            } else if (name.equals("food")) {
                food = readText(parser);
            } else {
                skip(parser);
            }
        }

        if (!url.equals("")) {
            countryHandler.addCountryRecord(countryName, continentName, capital, population, currencyCode, url, food);
        }

        return;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
