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

public class TabFragmentNearbyAdapter extends RecyclerView.Adapter<TabFragmentNearbyAdapter.RestaurantViewHolder> {

    private int mNumberRestaurant;

    public TabFragmentNearbyAdapter(int numberOfRestaurant){
        mNumberRestaurant= numberOfRestaurant;
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
        restaurantViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberRestaurant;
    }


    class RestaurantViewHolder extends  RecyclerView.ViewHolder{
        TextView restaurantNameTextView;
        public RestaurantViewHolder(View itemView){
            super(itemView);
            restaurantNameTextView = (TextView)itemView.findViewById(R.id.tv_item_number);

        }
        void bind(int listIndex){
            restaurantNameTextView.setText(String.valueOf(listIndex));
        }

    }
}
