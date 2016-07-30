package com.example.saar.locationalert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView helloMessage;

    EditText phoneNumber;
    EditText messageBox;
    EditText locationField;
    Button saveButton;
    Button logoutButton;

    ListView locationsList;
    ArrayAdapter<String> adapter;
    AddressAdapter addressAdapter;
    ArrayList<String> items;
    UserStore userStore;
    User user;
    Geocoder geocoder;
    Locale lHebrew;
    LocationManager locationManager;
    LocationListener locationListener;
    GPSHandler gpsHandler;
    Address add = null;

    String phoneNumberStr = "";
    String messageStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloMessage = (TextView) findViewById(R.id.helloMessage);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        phoneNumber = (EditText) findViewById(R.id.cellphone_number);
        messageBox = (EditText) findViewById(R.id.message_box);
        locationField = (EditText) findViewById(R.id.location_field);
        saveButton = (Button) findViewById(R.id.save_changes);


        locationsList = (ListView) findViewById(R.id.locations_list);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text_item, items);
        lHebrew = new Locale("he");
        geocoder = new Geocoder(this, lHebrew);

        logoutButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        locationField.addTextChangedListener(this);
        userStore = new UserStore(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                userStore.clearData();
                userStore.setIsUserLoggedIn(false);
                startActivity(new Intent(this, login.class));
                break;
            case R.id.save_changes:
                phoneNumberStr = phoneNumber.getText().toString();
                messageStr = messageBox.getText().toString();
                SmsManager smsManager = SmsManager.getDefault();

                try {
                    do{
                        gpsHandler.getLocation();
                        Thread.sleep(5000);
                    }while(distance(gpsHandler.getLatitude(), gpsHandler.getLongtitude(), add.getLatitude(), add.getLongitude()) > 0.2);
                }
                catch (Exception e)
                {
                    //Do nothing
                }
                if(distance(gpsHandler.getLatitude(), gpsHandler.getLongtitude(), add.getLatitude(), add.getLongitude()) <= 0.2)
                {
                    smsManager.sendTextMessage(phoneNumberStr, null, messageStr, null, null);
                }

        }
    }

    protected void onStart() {
        super.onStart();
        if (isUserLoggenIn())
        {
//            user = userStore.getLoggenInUser();

//            double longtitude = gpsHandler.getLongtitude();
//            double latitude = gpsHandler.getLatitude();
//            helloMessage.setText("hello, " + user.userName + "\n " + longtitude + " "  + latitude);
        }
        else {
            startActivity(new Intent(this, login.class));
        }
    }

    private void setUserData(String s){
        gpsHandler = new GPSHandler(MainActivity.this);
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

    private boolean isUserLoggenIn() {
        return userStore.isUserLoggedIn();
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

    /** calculates the distance between two locations in MILES */
    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75;; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }
}
