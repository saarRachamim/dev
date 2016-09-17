package com.example.saar.locationalert.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.saar.locationalert.R;
import com.example.saar.locationalert.actvities.NewMetaDataActivity;

/**
 * Created by Saar on 10/09/2016.
 */
public class NewMetaDataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more_for_meta_recycler, container, false);
        Button newMetadataButton = (Button)view.findViewById(R.id.create_new_metadata);
        newMetadataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewMetaDataActivity.class));
            }
        });
        return view;
    }
}
