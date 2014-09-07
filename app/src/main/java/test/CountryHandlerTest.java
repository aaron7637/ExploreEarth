package test;

import android.app.LauncherActivity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.example.exploreearth.app.CountryDatabaseHandler;
import com.example.exploreearth.app.CountryParser;
import com.example.exploreearth.app.Entry;
import com.example.exploreearth.app.MainActivity;
import com.example.exploreearth.app.R;
import com.example.exploreearth.app.RSSParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CountryHandlerTest extends ActivityInstrumentationTestCase2<testActivity> {

    private testActivity mFirstTestActivity;
    private final static String TAG = "Text";

    public CountryHandlerTest() {
        super(testActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstTestActivity = getActivity();
    }

    public void testLaunchNextActivityButton_labelText() {

        try {
            InputStream is = getActivity().getAssets().open("countryInfo.xml");
            CountryParser parser = new CountryParser(getActivity());
            parser.parse(is);
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        } catch (XmlPullParserException e) {
            Log.d(TAG, "XmlPullParserException", e);
        }

        CountryDatabaseHandler countryHandler = new CountryDatabaseHandler(getActivity());
        String[] countries = countryHandler.getCountries("");
        for (String s : countries) {
            String urlStr = countryHandler.getField(s, CountryDatabaseHandler.COUNTRYFIELDS.URL);
            assertNotNull(urlStr);
            //assertNotNull(food);

/*            try {
                URL url = new URL(urlStr);
                URLConnection connection;
                connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;

                int responseCode = httpConnection.getResponseCode();
                assertEquals(responseCode, HttpURLConnection.HTTP_OK);
                InputStream in = httpConnection.getInputStream();

                ArrayList<Entry> entries = RSSParser.parse(in);
                assertNotNull(entries);
                assertTrue(entries.size() != 0);
            }
            catch (MalformedURLException e) {
                Log.d(TAG, "MalformedURLException", e);
            }
            catch (IOException e) {
                Log.d(TAG, "IOException", e);
            }
            catch (XmlPullParserException e) {
                Log.d(TAG, "XmlPullParserException", e);
            }*/
        }
    }
}