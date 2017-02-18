package com.example.saar.locationalert.actvities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.example.saar.locationalert.objects.AppConstants;
import com.example.saar.locationalert.objects.PermissionHandler;
import com.example.saar.locationalert.services.GPSHandler;
import com.example.saar.locationalert.objects.MetaData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NewMetaDataActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final int REQUEST_CODE = 1;

    private EditText phoneNumber;
    private EditText messageBox;
    private EditText locationField;
    private Button saveButton;

    private ListView locationsList;
    private ArrayAdapter<String> adapter;
    private AddressAdapter addressAdapter;
    private ArrayList<String> items;
    private Geocoder geocoder;
    private Locale lHebrew;
    private GPSHandler gpsHandler;
    private Address add = null;
    private DBOperations dbOperations;
    private String phoneNumberStr = "";
    private String messageStr = "";
    private String addressStr = "";
    private String prefMessage;
    private MetaData metaData;
    private String lastUpdatedAdd = "";
    private Bundle b;

    private Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_metadata_activity);

        phoneNumber = (EditText) findViewById(R.id.cellphone_number);
        messageBox = (EditText) findViewById(R.id.message_box);
        locationField = (EditText) findViewById(R.id.location_field);
        saveButton = (Button) findViewById(R.id.save_changes);

        addressAdapter = new AddressAdapter(getBaseContext(), new ArrayList<Address>());
        locationsList = (ListView) findViewById(R.id.locations_list);
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text_item, items);
        lHebrew = new Locale(getString(R.string.hebrewLocale));
        geocoder = new Geocoder(this, lHebrew);
        dbOperations = new DBOperations(this);

        b = getIntent().getExtras();
        if(b != null)
        {
            int metadataId = b.getInt(AppConstants.idStr);
            metaData = dbOperations.getMetaDataById(metadataId);
            phoneNumber.setText(metaData.getCell());
            messageBox.setText(metaData.getMessage());
            locationField.setText(metaData.getAddress());
        }

        phoneNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (phoneNumber.getRight() - phoneNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Uri uri = Uri.parse("content://contacts");
                        Intent intent = new Intent(Intent.ACTION_PICK, uri);
                        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(intent, REQUEST_CODE);

                        return true;
                    }
                }
                return false;
            }
        });

        prefMessage = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_message", null);
        if(prefMessage != null && prefMessage!= "")
        {
            messageBox.setText(prefMessage);
        }

        saveButton.setOnClickListener(this);
        locationField.addTextChangedListener(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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

                /**Missing fields content in the app**/
                if (phoneNumberStr == null || messageStr == null || addressStr == null || addressStr.equals("")) {
                    Toast.makeText(this, R.string.missingInput, Toast.LENGTH_SHORT).show();
                    return;
                }

                /** Checking if there is all permision are missing, if so asks for all at once**/
                PermissionHandler permissionHandler = PermissionHandler.getInstance();
                if(!permissionHandler.isPermissionGrantedForAccessCoarseLocation(this) // coarse access permission
                        || !permissionHandler.isPermissionGrantedForAccessFineLocation(this) // fine location permission
                        || !permissionHandler.isPermissionGrantedForSendSms(this)) // send sms permission
                {
                    Toast.makeText(this, R.string.missingPermissionsMessage, Toast.LENGTH_SHORT).show();
                    return;
                }

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

                Toast.makeText(this, R.string.messageSavedStr, Toast.LENGTH_SHORT).show();

                
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent manageMetaDataIntent = new Intent(getApplicationContext(), ManageMetaDataActivity.class);
                        manageMetaDataIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(manageMetaDataIntent);
                    }
                }, 1000);
                finish();
                break;
        }
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(timer != null)
        {
            timer.cancel();
        }
    }

    @Override
    public void afterTextChanged(final Editable s) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new AsyncTextListener(getApplication().getApplicationContext(), s.toString()).execute();
            }
        }, 400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);
                phoneNumber.setText(number);
            }
        }
    };

    public class AsyncTextListener extends AsyncTask<String, String, Long> {
        Context context;
        String s;

        public AsyncTextListener(Context context, String s){
            this.context = context;
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            lHebrew = new Locale(getString(R.string.hebrewLocale));
            geocoder = new Geocoder(context, lHebrew);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

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
            }
        }
    }
}
