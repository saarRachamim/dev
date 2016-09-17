package com.example.saar.locationalert.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saar.locationalert.R;

/**
 * Created by Saar on 10/09/2016.
 */
public class MetaDataListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.metadata_rceycler, container, false);
    }
}
