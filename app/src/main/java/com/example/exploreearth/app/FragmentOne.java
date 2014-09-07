package com.example.exploreearth.app;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentOne extends Fragment {
    private EditText editTextSearchCountry;
    private ListView listViewShowCountries;
    private TextView textViewShowChosenCountry;
    private String[] countries = null;

    public FragmentOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_one, container, false);

        editTextSearchCountry = (EditText) view.findViewById(R.id.editText1);
        listViewShowCountries = (ListView) view.findViewById(R.id.listView1);
        textViewShowChosenCountry = (TextView) view.findViewById(R.id.textViewShowChosenCountry);

        editTextSearchCountry.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                new MyAsyncTask().execute();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        new MyAsyncTask().execute();
        return view;
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            CountryDatabaseHandler countryHandler = new CountryDatabaseHandler(getActivity());
            countries = countryHandler.getCountries(editTextSearchCountry.getText().toString());
            return "";
        }

        protected void onPostExecute(String result) {
            listViewShowCountries.setAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, countries));

            listViewShowCountries.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view,
                                        int pos, long id) {
                    String country = adapter.getItemAtPosition(pos).toString();
                    ChosenCountry.setCountry(country);
                    textViewShowChosenCountry.setText("Country Selected: " + country);
                }
            });
        }
    }
}
