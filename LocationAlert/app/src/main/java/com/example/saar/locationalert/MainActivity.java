package com.example.saar.locationalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
