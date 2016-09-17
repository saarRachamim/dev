package com.example.saar.locationalert.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.saar.locationalert.services.HasArrivedService;
import com.example.saar.locationalert.R;

/**
 * Created by Saar on 30/07/2016.
 */
public class MainActivity  extends AppCompatActivity implements View.OnClickListener {
    Button createNewButton;
    Button manageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        createNewButton = (Button) findViewById(R.id.newButton);
        manageButton = (Button) findViewById(R.id.manageButton);

        createNewButton.setOnClickListener(this);
        manageButton.setOnClickListener(this);

        startService(new Intent(this, HasArrivedService.class));
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
        switch(v.getId()){
            case R.id.newButton:
                startActivity(new Intent(this, NewMetaDataActivity.class));
                break;
            case R.id.manageButton:
                startActivity(new Intent(this, ManageMetaDataActivity.class));
                break;
        }
    }
}
