package com.example.ppeepfinal.adapter;

import android.app.Activity;
import android.content.Context;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.R;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;

import java.util.List;


import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;




public class FoodCartPageAdapter extends RecyclerView.Adapter<FoodCartPageAdapter.FoodCartViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private List<OrderModel> mOrders;
    private Context mContext;
    private UserDatabase mdb;

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
        mdb = UserDatabase.getInstance(context);
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

        TextView orderItemTextview,orderItemPriceTextView,orderItemTotalPriceTextview;

        public FoodCartViewHolder(View itemView){

            super(itemView);

            orderItemTextview = (TextView) itemView.findViewById(R.id.tv_order_item);

            orderItemPriceTextView = (TextView)itemView.findViewById(R.id.tv_order_item_price);

            orderItemTotalPriceTextview =(TextView) itemView.findViewById(R.id.order_total_price);

            ImageView deleteIcon = (ImageView) itemView.findViewById(R.id.iv_order_item_delete);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    List<OrderModel> orders = getmOrders();
                    mdb.orderDAO().deleteOrder(orders.get(position));
                    mOrders.remove(orders.get(position));
                    notifyItemRemoved(getAdapterPosition());
                    //((Activity)mContext).finish();
                    //overridePendingTransition( 0, 0);

                    Intent intent = ((Activity) mContext).getIntent();
                    ((Activity)mContext).overridePendingTransition(0, 0);
                    intent.setFlags(FLAG_ACTIVITY_NO_ANIMATION);

                    ((Activity)mContext).finish();
                    ((Activity)mContext).overridePendingTransition(0, 0);

                    mContext.startActivity(intent );


                   // overridePendingTransition( 0, 0);

                    //startActivity(mContext,)
                    //notifyItemRangeChanged(getAdapterPosition(),mOrders.size());
                    //Toast.makeText(v.getContext(),"Click",Toast.LENGTH_LONG).show();
                    // OrderModel orderModel = mOrders.get(position);
                    // mdb.orderDAO().deleteOrder(orders.get());
                }
            });

            itemView.setOnClickListener(this);

        }
        void bind(String orderITemName,int orderItemPrice,int ItemAmount){
            orderItemTextview.setText(orderITemName);
            orderItemPriceTextView.setText(String.valueOf(orderItemPrice)+" x "+ String.valueOf(ItemAmount));
            int total = orderItemPrice * ItemAmount;
            orderItemTotalPriceTextview.setText(String.valueOf(total));
        }
        public void onClick(View v){

            int clickedPosition = mOrders.get(getAdapterPosition()).getId();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
