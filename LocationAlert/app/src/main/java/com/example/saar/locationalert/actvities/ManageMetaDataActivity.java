package com.example.saar.locationalert.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.example.saar.locationalert.R;
import com.example.saar.locationalert.db.DBOperations;
import com.example.saar.locationalert.objects.MetaData;
import com.example.saar.locationalert.adapters.MetaDatasAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class ManageMetaDataActivity extends AppCompatActivity implements MetaDatasAdapter.ClickListener, View.OnClickListener {
    List<MetaData> metaDatasList = new ArrayList<>();
    RecyclerView recyclerView;
    MetaDatasAdapter mAdapter;
    DBOperations dbOperations;
    Animation mSlideOutToRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_metadata_activity);

        dbOperations = new DBOperations(this);
        metaDatasList = dbOperations.getAllMetaData();

        recyclerView = (RecyclerView) findViewById(R.id.metadata_recycler_view);

        mAdapter = new MetaDatasAdapter(metaDatasList);
        mAdapter.setClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
    public void itemClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.remove_icon:
                dbOperations.removeMetaDataFromDb(position);
                break;
            case R.id.edit_icon:
                MetaData metadata = mAdapter.getMetadataById(position);
                Intent intent = new Intent(this, NewMetaDataActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Bundle b = new Bundle();
                b.putInt("id", position);
                b.putString("cell", metadata.getCell());
                b.putString("message", metadata.getMessage());
                b.putString("address", metadata.getAddress());
                b.putDouble("latitude", metadata.getLatitude());
                b.putDouble("longitude", metadata.getLongitude());

                intent.putExtras(b);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_new_metadata:
                Intent intent = new Intent(this, NewMetaDataActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
