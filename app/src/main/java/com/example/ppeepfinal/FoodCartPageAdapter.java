package com.example.ppeepfinal;

import android.content.Context;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserModel;

import java.util.List;

public class FoodCartPageAdapter extends RecyclerView.Adapter<FoodCartPageAdapter.FoodCartViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private List<OrderModel> mOrders;
    private Context mContext;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public FoodCartPageAdapter(
            Context context,
            FoodCartPageAdapter.ListItemClickListener listener
    ){
        // mNumberRestaurant= numberOfRestaurant;
        mContext = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public FoodCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_food_cart_single_item_page,viewGroup,false);
        return new FoodCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartViewHolder foodCartViewHolder, int position) {
        OrderModel orderModel = mOrders.get(position);
        String orederItemName = orderModel.getItemName();
        int orderItemPrice = orderModel.getItemPrice();
        int ItemAmount = orderModel.getItemAmount();
        foodCartViewHolder.bind(orederItemName,orderItemPrice,ItemAmount);
    }

    @Override
    public int getItemCount() {
        if(mOrders == null){
            return 0;
        }
        return mOrders.size();
    }

    public  List<OrderModel> getmOrders(){
        return mOrders;
    }

    public void setmOrders(List<OrderModel> orders){
        mOrders = orders;
        notifyDataSetChanged();
    }

    class FoodCartViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderItemTextview,orderItemPriceTextView;
        public FoodCartViewHolder(View itemView){
            super(itemView);
            orderItemTextview = (TextView) itemView.findViewById(R.id.tv_order_item);
            orderItemPriceTextView = (TextView)itemView.findViewById(R.id.tv_order_item_price);
            itemView.setOnClickListener(this);

        }
        void bind(String orderITemName,int orderItemPrice,int ItemAmount){
            orderItemTextview.setText(orderITemName);
            orderItemPriceTextView.setText(String.valueOf(orderItemPrice)+" x "+ String.valueOf(ItemAmount));
        }
        public void onClick(View v){
            int clickedPosition = mOrders.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
