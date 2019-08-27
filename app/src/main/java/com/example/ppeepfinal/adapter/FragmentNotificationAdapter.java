package com.example.ppeepfinal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.R;
import com.example.ppeepfinal.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class FragmentNotificationAdapter extends RecyclerView.Adapter<FragmentNotificationAdapter.NotificationPageViewHolder> {

    private int mNumberRestaurant;

    private List<String> mNotificationText;
    private List<String> mNotificationDate;
    private List<String> mNotificationImg;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public FragmentNotificationAdapter(
            List<String> NotificationText,
            List<String> NotificationImg,
            List<String> NotificationDate,
            ListItemClickListener listener
    ){

        mNotificationText = NotificationText;
        mNotificationImg = NotificationImg;
        mNotificationDate = NotificationDate;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public NotificationPageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutItemOfRestaurant = R.layout.notification_design;
        LayoutInflater layoutInflater = LayoutInflater.from(context) ;
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutItemOfRestaurant,viewGroup,shouldAttachToParentImmediately);
        NotificationPageViewHolder viewHolder = new FragmentNotificationAdapter.NotificationPageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationPageViewHolder notificationPageViewHolder, int position) {
        String notificationText = mNotificationText.get(position);
        String arr[] = mNotificationDate.get(position).split(" ");
        Log.d("Order  : Date ", arr[0]);
        Log.d("Order : Time ", arr[1]);
        String notificationDate = arr[0];
        String notificationTime = arr[1];
        String logo = mNotificationImg.get(position);

        notificationPageViewHolder.bind(notificationText,notificationDate,notificationTime,logo);
    }

    @Override
    public int getItemCount() {
        return mNotificationText.size();
    }


    class NotificationPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notificationTextView;
        TextView notificationDateTextView;
        TextView notificationTimeTextview;
        ImageView notificationImage;
        public NotificationPageViewHolder(View itemView){
            super(itemView);

            notificationTextView = (TextView)itemView.findViewById(R.id.tv_notification_text);
            notificationDateTextView = (TextView) itemView.findViewById(R.id.tv_notification_date);
            notificationTimeTextview = (TextView) itemView.findViewById(R.id.tv_notification_time);
            notificationImage = (ImageView) itemView.findViewById(R.id.notification_image) ;
            itemView.setOnClickListener(this);

        }
        void bind(String notificationText,String notificationDate,String notificationTime, String logo){

            URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+logo);
            Picasso.get().load(getimageUrl.toString()).resize(80,90).into(notificationImage);
            notificationTextView.setText(notificationText);
            notificationDateTextView.setText(notificationDate);
            notificationTimeTextview.setText(notificationTime);

        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
