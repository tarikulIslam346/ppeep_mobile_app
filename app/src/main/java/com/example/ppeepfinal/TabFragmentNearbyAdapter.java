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

    public TabFragmentNearbyAdapter(List<String> RestaurantList){
       // mNumberRestaurant= numberOfRestaurant;
        mData = RestaurantList;
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
        restaurantViewHolder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class RestaurantViewHolder extends  RecyclerView.ViewHolder{
        TextView restaurantNameTextView;
        public RestaurantViewHolder(View itemView){
            super(itemView);
            restaurantNameTextView = (TextView)itemView.findViewById(R.id.tv_item_number);

        }
        void bind(String restaurnatName){
            restaurantNameTextView.setText(restaurnatName);
        }

    }
}
