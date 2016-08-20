package com.example.saar.locationalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class ManageMetaDataActivity extends AppCompatActivity implements MetaDatasAdapter.ClickListener {
    List<MetaData> metaDatasList = new ArrayList<>();
    RecyclerView recyclerView;
    MetaDatasAdapter mAdapter;
    DBOperations dbOperations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadata_rceycler);

        dbOperations = new DBOperations(this);
        metaDatasList = dbOperations.getAllMetaData();

        recyclerView = (RecyclerView) findViewById(R.id.metadata_recycler_view);

        mAdapter = new MetaDatasAdapter(metaDatasList);
        mAdapter.setClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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
}
