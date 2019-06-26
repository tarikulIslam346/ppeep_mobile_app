package com.example.ppeepfinal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FoodCartPageAdapter extends RecyclerView.Adapter<FoodCartPageAdapter.FoodCartViewHolder> {

    private List<String> mItemName;
    private List<Integer> mItemPrice;
    final private FoodCartPageAdapter.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public FoodCartPageAdapter(
            List<String> orderItemName,
            List<Integer> orderItemPrice,
            FoodCartPageAdapter.ListItemClickListener listener
    ){
        // mNumberRestaurant= numberOfRestaurant;
        mItemName = orderItemName;
        mItemPrice = orderItemPrice;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public FoodCartPageAdapter.FoodCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutItemOfRestaurant = R.layout.activity_food_cart_single_item_page;
        LayoutInflater layoutInflater = LayoutInflater.from(context) ;
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(layoutItemOfRestaurant,viewGroup,shouldAttachToParentImmediately);
        FoodCartPageAdapter.FoodCartViewHolder viewHolder = new FoodCartPageAdapter.FoodCartViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartPageAdapter.FoodCartViewHolder foodCartViewHolder, int position) {
        String orederItemName = mItemName.get(position);
        int orderItemPrice = mItemPrice.get(position);
        foodCartViewHolder.bind(orederItemName,orderItemPrice);
    }

    @Override
    public int getItemCount() {
        return mItemName.size();
    }

    class FoodCartViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderItemTextview,orderItemPriceTextView;
        public FoodCartViewHolder(View itemView){
            super(itemView);
            orderItemTextview = (TextView) itemView.findViewById(R.id.tv_order_item);
            orderItemPriceTextView = (TextView)itemView.findViewById(R.id.tv_order_item_price);
            itemView.setOnClickListener(this);

        }
        void bind(String orderITemName,int orderItemPrice){
            orderItemTextview.setText(orderITemName);
            orderItemPriceTextView.setText(String.valueOf(orderItemPrice));
        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
