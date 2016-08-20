package com.example.saar.locationalert;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    LocationManager locationManager;
    LocationListener locationListener;
    GPSHandler gpsHandler;
    Address add = null;
    DBOperations dbOperations;
    String phoneNumberStr = "";
    String messageStr = "";
    String addressStr = "";
    List<MetaData> metaDatas;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_location_main);

        phoneNumber = (EditText) findViewById(R.id.cellphone_number);
        messageBox = (EditText) findViewById(R.id.message_box);
        locationField = (EditText) findViewById(R.id.location_field);
        saveButton = (Button) findViewById(R.id.save_changes);

        locationsList = (ListView) findViewById(R.id.locations_list);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text_item, items);
        lHebrew = new Locale("he");
        geocoder = new Geocoder(this, lHebrew);

        b = getIntent().getExtras();
        if(b != null)
        {
            phoneNumber.setText(b.getString("cell"));
            messageBox.setText(b.getString("message"));
            locationField.setText(b.getString("address"));
        }

        saveButton.setOnClickListener(this);
        locationField.addTextChangedListener(this);
        dbOperations = new DBOperations(this);
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
                    int metadaId = b.getInt("id");
                    double latitiude;
                    double longitude;

                    if(add == null)
                    {
                        latitiude = b.getDouble("latitiude");
                        longitude = b.getDouble("longitude");
                    }
                    else
                    {
                        latitiude = add.getLatitude();
                        longitude = add.getLongitude();
                    }

                    dbOperations.removeMetaDataFromDb(metadaId);
                    dbOperations.insertMetaDataToDb(phoneNumberStr, messageStr, addressStr, latitiude, longitude);
                }

                Toast.makeText(this, "The message was saved", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }, 1000);

                break;
        }
    }

    protected void onStart() {
        super.onStart();
    }

    private void setUserData(String s){
        gpsHandler = new GPSHandler(NewMetaDataActivity.this);
        if(gpsHandler.isCanGetLocation())
        {
            List<Address> addresses = new ArrayList<Address>();
            items.clear();
            int tryToFindLocation = 0;
            while (addresses.size()==0 && tryToFindLocation < 3) {
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
    public void afterTextChanged(Editable s) {
        setUserData(s.toString());
    }
}
