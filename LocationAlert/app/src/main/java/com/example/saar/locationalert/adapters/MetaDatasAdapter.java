package com.example.saar.locationalert.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saar.locationalert.objects.MetaData;
import com.example.saar.locationalert.R;

import java.util.List;

/**
 * Created by Saar on 30/07/2016.
 */
public class MetaDatasAdapter extends RecyclerView.Adapter<MetaDatasAdapter.MyViewHolder> {
    public List<MetaData> metaDataList;
    ClickListener clickListener;
    Animation mSlideOutToRight;


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
        Button createNew;

        public MyViewHolder(View view) {
            super(view);
            createNew  = (Button)view.findViewById(R.id.create_new_metadata);
            mSlideOutToRight = AnimationUtils.loadAnimation(view.getContext(), R.anim.swipe_out_to_right);
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
                    metadataLayout.startAnimation(mSlideOutToRight);

//                    delete(getAdapterPosition());
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

    // In android there is a possibility to add a settings component.
    // Add the following:
    //Settings-> Default message-> - done

    //  At the create new, after save create new activity of the manage. - done

    // Add a create button to the manage - done
    // Add animation that the new order will fade in to the screen, in its index location. - no time
    // And the minus will shift the item out of the screen instead of fade out. - stuck

    //To pass an id of the object at the database instead of the data of the order between activities- done
    // Learn on ORM, green dao- learned briefly
    // To improve the performance of the locations search, - done.
    //Add fragments. - done.
    //Next time to add tasks for networking and UI- reminder
}
