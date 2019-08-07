package com.example.ppeepfinal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.google.type.Color;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

public class HistoryPageAdapter extends RecyclerView.Adapter<HistoryPageAdapter.HistoryPageViewHolder> {

    private int mNumberRestaurant;
    private List<Integer> mOrderId;
    private List<String> mRestaurantName;
    private List<String> mOrderDate;
    private List<Integer> mOrderStatus;
    private List<String> mLogo;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public HistoryPageAdapter(
            List<Integer> OrderId,
            List<String> RestauranName,
            List<String> OrderDate,
            List<Integer> OrderStatus,
            List<String> Logo,
            ListItemClickListener listener
    ){
        // mNumberRestaurant= numberOfRestaurant;
        mOrderId = OrderId;
        mRestaurantName = RestauranName;
        mOrderDate = OrderDate;
        mOrderStatus = OrderStatus;
        mLogo = Logo;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public HistoryPageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutItemOfRestaurant = R.layout.recycleview_history_page_design;
        LayoutInflater layoutInflater = LayoutInflater.from(context) ;
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutItemOfRestaurant,viewGroup,shouldAttachToParentImmediately);
        HistoryPageViewHolder viewHolder = new HistoryPageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryPageViewHolder historyPageViewHolder, int position) {
        int orderId = mOrderId.get(position);
        String restaurant = mRestaurantName.get(position);
        String arr[] = mOrderDate.get(position).split(" ");
        Log.d("Order  : Date ", arr[0]);
        Log.d("Order : Time ", arr[1]);
        String orderDate = arr[0];
        String orderTime = arr[1];
        int orderStatus = mOrderStatus.get(position);
        String logo = mLogo.get(position);

        historyPageViewHolder.bind(orderId,restaurant,orderDate,orderTime,orderStatus,logo);
    }

    @Override
    public int getItemCount() {
        return mRestaurantName.size();
    }


    class HistoryPageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView orderIdTextView;
        TextView restaurantNameTextView;
        TextView orderDateTextView;
        TextView orderTimeTextview;
        TextView orderStatusTextView;
        ImageView orderRestaurantLogo;
        public HistoryPageViewHolder(View itemView){
            super(itemView);
            orderIdTextView = (TextView)itemView.findViewById(R.id.tv_order_Id) ;
            restaurantNameTextView = (TextView)itemView.findViewById(R.id.tv_restaurant_name);
            orderDateTextView = (TextView) itemView.findViewById(R.id.tv_order_date);
            orderTimeTextview = (TextView) itemView.findViewById(R.id.tv_order_time);
            orderStatusTextView = (TextView) itemView.findViewById(R.id.tv_order_status);
            orderRestaurantLogo = (ImageView) itemView.findViewById(R.id.logo_image) ;
            itemView.setOnClickListener(this);

        }
        void bind(int orderId,String restaurnatName,String orderDate,String orderTime,int orderStatus, String logo){

            URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+logo);

            Picasso.get().load(getimageUrl.toString()).resize(80,90).into(orderRestaurantLogo);
            orderIdTextView.setText("Order ID #"+String.valueOf(orderId));
            restaurantNameTextView.setText(restaurnatName);
            orderDateTextView.setText(orderDate);
            orderTimeTextview.setText(orderTime);
            if(orderStatus == 0) {
                orderStatusTextView.setBackgroundColor(YELLOW);
                orderStatusTextView.setTextColor(BLACK);
                orderStatusTextView.setText("Order Pending");
            }
            if(orderStatus == 1) {
                orderStatusTextView.setBackgroundColor(BLUE);
                orderStatusTextView.setTextColor(WHITE);
                orderStatusTextView.setText("Order Accepted");
            }
            if(orderStatus == 2) {
                orderStatusTextView.setBackgroundColor(GREEN);
                orderStatusTextView.setTextColor(WHITE);
                orderStatusTextView.setText("Order Deliverd");
            }
            if(orderStatus == 3) {
                orderStatusTextView.setBackgroundColor(RED);
                orderStatusTextView.setTextColor(WHITE);
                orderStatusTextView.setText("Order Cancelled");
            }
        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
