package com.example.saar.locationalert;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class MetaDatasAdapter extends RecyclerView.Adapter<MetaDatasAdapter.MyViewHolder> {
    public List<MetaData> metaDataList;
    ClickListener clickListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.metadatas_list_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MetaData metaData = metaDataList.get(position);
        holder.cell.setText(metaData.getCell());
        holder.message.setText(metaData.getMessage());
        holder.address.setText(metaData.getAddress());
        holder.metadataId = metaData.getId();
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return metaDataList.size();
    }


    public void delete(int index){ metaDataList.remove(index); }


    public MetaDatasAdapter(List<MetaData> metaDataList) {
        this.metaDataList = metaDataList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView cell, message, address;
        public int metadataId;
        public ImageView deleteIcon;

        public MyViewHolder(View view) {
            super(view);
            cell = (TextView) view.findViewById(R.id.cell);
            message = (TextView) view.findViewById(R.id.message);
            address = (TextView) view.findViewById(R.id.address);
            deleteIcon = (ImageView) view.findViewById(R.id.remove_icon);
            deleteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.remove_icon:
                    if(clickListener != null)
                    {
                        clickListener.itemClicked(v, metadataId);
                    }

                    delete(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());
                    notifyDataSetChanged();
            }
        }
    }
    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

    //To add save toast, and open new activity of main activity.
    // db.open and db. close inside function of db operations. no outside.

    // check fragments - do fragments on the new meta data.
    // check for animations.

    //check google analytics
    //check crashylitics\fabrics
    //to add edits
    //clean code
}
