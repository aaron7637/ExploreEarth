package test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.exploreearth.app.CountryParser;
import com.example.exploreearth.app.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class testActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //new MyAsyncTask();
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            try {
                InputStream is = getAssets().open("countryInfo.xml");
                CountryParser parser = new CountryParser(testActivity.this);
                parser.parse(is);
            } catch (IOException e) {
                Log.d("test", " Cant open file");
            } catch (XmlPullParserException e) {
                Log.d("test", " Pull Parser Exception");
            }
            return "";
        }

        // Changes the values for a bunch of TextViews on the GUI
        protected void onPostExecute(String result) {
        }
    }
}
