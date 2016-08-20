package com.example.saar.locationalert;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class MetaDatasAdapter extends RecyclerView.Adapter<MetaDatasAdapter.MyViewHolder> {
    public List<MetaData> metaDataList;
    ClickListener clickListener;
    Animation fadeOutAnimation;

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

    public MetaData getMetadataById(int id) {
        for(int i = 0; i < metaDataList.size(); ++i)
            if(metaDataList.get(i).getId() == id)
                return metaDataList.get(i);

        return null;
    }

    public MetaDatasAdapter(List<MetaData> metaDataList) {
        this.metaDataList = metaDataList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView cell, message, address;
        public int metadataId;
        public ImageView deleteIcon, editIcon;
        public LinearLayout metadataLayout;

        public MyViewHolder(View view) {
            super(view);

            fadeOutAnimation = new AlphaAnimation(1, 0);
            fadeOutAnimation.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOutAnimation.setStartOffset(100);
            fadeOutAnimation.setDuration(1000);

            metadataLayout = (LinearLayout) view.findViewById(R.id.metadata_layout);
            cell = (TextView) view.findViewById(R.id.cell);
            message = (TextView) view.findViewById(R.id.message);
            address = (TextView) view.findViewById(R.id.address);
            deleteIcon = (ImageView) view.findViewById(R.id.remove_icon);
            deleteIcon.setOnClickListener(this);
            editIcon = (ImageView) view.findViewById(R.id.edit_icon);
            editIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.remove_icon:
                    if(clickListener != null)
                    {
                        clickListener.itemClicked(v, metadataId);
                    }

                    metadataLayout.startAnimation(fadeOutAnimation);
                    delete(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());
                    notifyDataSetChanged();
                    break;
                case R.id.edit_icon:
                    if(clickListener != null)
                    {
                        clickListener.itemClicked(v, metadataId);
                    }
                    notifyItemChanged(getAdapterPosition());
                    break;
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

    //To add save toast, and open new activity of main activity - done
    // db.open and db.close inside function of db operations. no outside. - done

    // check fragments - do fragments on the new meta data.
    // check for animations - done, implemented a fade out for removal of metadatas.

    //check google analytics - done, for app only, fire base is recommended. I checked it and I can see the amount of users for the app - 1.
    //check crashylitics\fabrics - done, added what needed to the gradle files, stopped after noticing it requires a credit card information for a token to be created.
    //to add edits - done.
    //clean code - done.
}
