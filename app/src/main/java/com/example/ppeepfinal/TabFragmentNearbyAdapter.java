package com.example.ppeepfinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TabFragmentNearbyAdapter extends RecyclerView.Adapter<TabFragmentNearbyAdapter.RestaurantViewHolder> {

    private int mNumberRestaurant;
    private List<String> mData;
    private List<String> mOpeningTime;
    private List<String> mClosingTime;
    private List<String> mCusine;


    public TabFragmentNearbyAdapter(
            List<String> RestaurantList,
            List<String> OpeningTime,
            List<String> ClosingingTime,
            List<String> Cusine
    ){
       // mNumberRestaurant= numberOfRestaurant;
        mData = RestaurantList;
        mOpeningTime = OpeningTime;
        mClosingTime = ClosingingTime;
        mCusine = Cusine;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutItemOfRestaurant = R.layout.tab_fragment_nearby_restaurant_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context) ;
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutItemOfRestaurant,viewGroup,shouldAttachToParentImmediately);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder, int position) {
        String restaurant = mData.get(position);
        String opening = mOpeningTime.get(position);
        String closing = mClosingTime.get(position);
        String cusine = mCusine.get(position);

        restaurantViewHolder.bind(restaurant,opening,closing,cusine);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class RestaurantViewHolder extends  RecyclerView.ViewHolder{
        TextView restaurantNameTextView;
        TextView openingClosingTextView;
        TextView cusineTextview;
        public RestaurantViewHolder(View itemView){
            super(itemView);
            restaurantNameTextView = (TextView)itemView.findViewById(R.id.tv_item_number);
            openingClosingTextView = (TextView) itemView.findViewById(R.id.tv_opening_closing);
            cusineTextview = (TextView) itemView.findViewById(R.id.tv_cusine);

        }
        void bind(String restaurnatName,String openTime,String closing, String cusine){
            restaurantNameTextView.setText(restaurnatName);
            openingClosingTextView.setText(openTime + " - "+closing);
            cusineTextview.setText(cusine);
        }

    }
}
