package com.example.saar.locationalert.actvities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.saar.locationalert.R;
import com.example.saar.locationalert.adapters.AddressAdapter;
import com.example.saar.locationalert.db.DBOperations;
import com.example.saar.locationalert.services.GPSHandler;
import com.example.saar.locationalert.objects.MetaData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewMetaDataActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    EditText phoneNumber;
    EditText messageBox;
    EditText locationField;
    Button saveButton;

    ListView locationsList;
    ArrayAdapter<String> adapter;
    AddressAdapter addressAdapter;
    ArrayList<String> items;
    Geocoder geocoder;
    Locale lHebrew;
    GPSHandler gpsHandler;
    Address add = null;
    DBOperations dbOperations;
    String phoneNumberStr = "";
    String messageStr = "";
    String addressStr = "";
    String prefMessage;
    MetaData metaData;
    String lastUpdatedAdd = "";
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_location_main);

        phoneNumber = (EditText) findViewById(R.id.cellphone_number);
        messageBox = (EditText) findViewById(R.id.message_box);
        locationField = (EditText) findViewById(R.id.location_field);
        saveButton = (Button) findViewById(R.id.save_changes);

        addressAdapter = new AddressAdapter(getBaseContext(), new ArrayList<Address>());
        locationsList = (ListView) findViewById(R.id.locations_list);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text_item, items);
        lHebrew = new Locale("he");
        geocoder = new Geocoder(this, lHebrew);
        dbOperations = new DBOperations(this);

        b = getIntent().getExtras();
        if(b != null)
        {
            int metadataId = b.getInt("id");
            metaData = dbOperations.getMetaDataById(metadataId);
            phoneNumber.setText(metaData.getCell());
            messageBox.setText(metaData.getMessage());
            locationField.setText(metaData.getAddress());
        }

        prefMessage = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_message", null);
        if(prefMessage != null && prefMessage!="")
        {
            messageBox.setText(prefMessage);
        }

        saveButton.setOnClickListener(this);
        locationField.addTextChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_changes:
                phoneNumberStr = phoneNumber.getText().toString();
                messageStr = messageBox.getText().toString();
                addressStr = locationField.getText().toString();
                if(b == null)
                {
                    dbOperations.insertMetaDataToDb(phoneNumberStr, messageStr, addressStr, add.getLatitude(), add.getLongitude());
                }
                else
                {
                    double latitude;
                    double longitude;

                    if(add == null)
                    {
                        latitude = metaData.getLatitude();
                        longitude = metaData.getLongitude();
                    }
                    else
                    {
                        latitude = add.getLatitude();
                        longitude = add.getLongitude();
                    }

                    dbOperations.removeMetaDataFromDb(metaData.getId());
                    dbOperations.insertMetaDataToDb(phoneNumberStr, messageStr, addressStr, latitude, longitude);
                }

                Toast.makeText(this, "The message was saved", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ManageMetaDataActivity.class));
                    }
                }, 1000);

                break;
        }
    }

    protected void onStart() {
        super.onStart();
    }



    private void setUserData(String s){
        gpsHandler = new GPSHandler(this);
        if(gpsHandler.isCanGetLocation())
        {
            List<Address> addresses = new ArrayList<Address>();
            items.clear();
            int tryToFindLocation = 0;
            while (addresses.size()==0 && tryToFindLocation < 1) {
                try {
                    addresses = geocoder.getFromLocationName(s, 5);
                    ++tryToFindLocation;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            addressAdapter = new AddressAdapter(this.getBaseContext(), addresses);
            locationsList.setAdapter(addressAdapter);
            addressAdapter.notifyDataSetChanged();
            locationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    add = addressAdapter.getItem(position);
                    locationField.setText(addressAdapter.getItemString(position));
                    addressAdapter = new AddressAdapter(getBaseContext(), new ArrayList<Address>());
                    locationsList.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
          new AsyncTextListener(this, s.toString()).execute();
    }

    public class AsyncTextListener extends AsyncTask<String, String, Long> {
        Context context;
        String s;

        public AsyncTextListener(Context context, String s){
            this.context = context;
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            lHebrew = new Locale("he");
            geocoder = new Geocoder(context, lHebrew);
        }

        @Override
        protected Long doInBackground(String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUserData(s);
                }
            });
            return null;
        }

        private void setUserData(String s){
            gpsHandler = new GPSHandler(context);
            boolean isEquals = lastUpdatedAdd.equals(locationField.getText().toString());
            if(gpsHandler.isCanGetLocation() && !lastUpdatedAdd.equals(locationField.getText().toString()))
            {
                List<Address> addresses = new ArrayList<Address>();
                items.clear();
                int tryToFindLocation = 0;
                while (addresses.size()==0 && tryToFindLocation < 1) {
                    try {
                        addresses = geocoder.getFromLocationName(s, 5);
                        ++tryToFindLocation;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                addressAdapter = new AddressAdapter(getBaseContext(), addresses);
                locationsList.setAdapter(addressAdapter);
                addressAdapter.notifyDataSetChanged();
                locationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        lastUpdatedAdd = addressAdapter.getItemString(position);
                        add = addressAdapter.getItem(position);
                        locationField.setText(addressAdapter.getItemString(position));
                        addressAdapter = new AddressAdapter(getBaseContext(), new ArrayList<Address>());
                        locationsList.setAdapter(addressAdapter);
                        addressAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

    }
}
