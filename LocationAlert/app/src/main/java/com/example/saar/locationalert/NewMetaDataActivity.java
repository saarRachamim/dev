package com.example.saar.locationalert;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewMetaDataActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
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
    DBOperations dbOperations;
    String phoneNumberStr = "";
    String messageStr = "";
    String addressStr = "";
    List<MetaData> metaDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_location_main);
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
        dbOperations = new DBOperations(this);
        dbOperations.open();
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
                addressStr = locationField.getText().toString();

                dbOperations.insertMetaDataToDb(phoneNumberStr, messageStr, addressStr, add.getLatitude(), add.getLongitude());
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
}
