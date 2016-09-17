package com.example.saar.locationalert.adapters;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saar.locationalert.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saar on 4/29/2016.
 */
public class AddressAdapter extends ArrayAdapter<Address> {
    private final Context context;
    private ArrayList<Address> addressList;
    private static LayoutInflater inflater = null;

    public AddressAdapter (Context context, List<Address> _addressList){
        super(context, -1, _addressList);
        addressList = (ArrayList<Address>) _addressList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = infalter.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text_item);
        String addressStr = getItemString(position);

        textView.setText(addressStr);
        return textView;
    }

    public String getItemString(int position)
    {
        String addressStr = "";
        if(addressList.size() > 0)
            for (int j = 0; j < addressList.get(position).getMaxAddressLineIndex(); ++j)
                addressStr += " " + addressList.get(position).getAddressLine(j);

        return addressStr;
    }
}
