package com.example.saar.locationalert.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

import com.example.saar.locationalert.listener.OnSwipeListener;
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
    int lastPosition = -1;


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

        setAnimation(holder.metadataLayout, position);
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

        public MyViewHolder(final View view) {
            super(view);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    view.getContext());

            createNew  = (Button)view.findViewById(R.id.create_new_metadata);
            mSlideOutToRight = AnimationUtils.loadAnimation(view.getContext(), R.anim.swipe_out_to_right);
            metadataLayout = (LinearLayout) view.findViewById(R.id.metadata_layout);

            alertDialogBuilder.setMessage("Are you sure").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    triggerRemoveIconOnClick(view);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            metadataLayout.setOnTouchListener(new OnSwipeListener(view.getContext()) {

                public void onSwipeRight(){
                    alertDialogBuilder.show();
                }

                public void onSwipeLeft(){
                    alertDialogBuilder.show();
                }
            });

            cell = (TextView) view.findViewById(R.id.cell);
            message = (TextView) view.findViewById(R.id.message);
            address = (TextView) view.findViewById(R.id.address);
            deleteIcon = (ImageView) view.findViewById(R.id.remove_icon);
            deleteIcon.setOnClickListener(this);
            editIcon = (ImageView) view.findViewById(R.id.edit_icon);
            editIcon.setOnClickListener(this);
        }

        public void triggerRemoveIconOnClick(View view){
            onClick((ImageView)view.findViewById(R.id.remove_icon));
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
                    mSlideOutToRight.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            delete(getAdapterPosition());
                            notifyItemChanged(getAdapterPosition());
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
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

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
//    Use action bar in the app

//    Not critical - Back should not go back to all previous activities

//    Performance in location finder
//    First, all UI changes should be in onPostExecute and not in doInBackground. Check out how you pass data to it. - done
//    On any input change, wait 1 sec. If that 1 sec passed and there was no other input change since - run the asynctask - done

//    Add a new activity that shows a map - // TODO: 23/10/2016  

//    Change UI of manage screen to appear like Gmail - done

//    Add gesture on each item in manage screen, that if you do swipe left to right or right to left... - done

//    ORM - feel free to migrate your data to use GreenDAO - // TODO: 23/10/2016

//    You can start thinking about on boarding and how you the app behaving - done.
//    I think that on setting to add support for manager number, by doing that updates by the manager will be shown to the user.
//    The manager will see all the users that are related to him.
}
