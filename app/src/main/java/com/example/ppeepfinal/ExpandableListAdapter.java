package com.example.ppeepfinal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
//import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppeepfinal.data.OrderMerchantModel;
import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;
import com.google.android.material.snackbar.Snackbar;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> _listChildPriceData;
    private HashMap<String, List<Integer>> _listChildIdData;
    private UserDatabase mdb;
    private int merchantId,vat,deliveryCharge;
    private String restaurantName;
    int i ;


    public ExpandableListAdapter(
            Context context,
            List<String> listDataHeader,
            HashMap<String, List<String>> listChildData,
            HashMap<String, List<String>> listChildPriceData,
            HashMap<String, List<Integer>> listChildIdData,
            int _merchantId,
            int vat,
            int deliveryCharge,
            String restaurantName
    ) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._listChildPriceData = listChildPriceData;
        this._listChildIdData = listChildIdData;
        mdb = UserDatabase.getInstance(this._context);// create db instance
        this.merchantId = _merchantId;
        this.vat = vat;
        this.deliveryCharge = deliveryCharge;
        this.restaurantName = restaurantName;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    public Object getChildDataId(int groupPosition, int childPosititon) {
        return this._listChildIdData.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    public Object getChildPrice(int groupPosition, int childPosititon) {
        return this._listChildPriceData.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public  int ItemAddCheck( int ItemId){


        List<OrderModel> order  = mdb.orderDAO().loadOrder();
        int flag =  0;
        if(order.size()!=0){
            for(int j =0;j<order.size();j++){
                if(order.get(j).getItemId() == ItemId){
                    //i++;
                    //textViewAddCart.setText( " "+String.valueOf(i)+ " ");
                    flag = 1;
                    return flag;


                }
            }
            return flag;
        }
        return flag;
    }

    @Override
    public View getChildView(
            final int groupPosition,
            final int childPosition,
            boolean isLastChild,
            View convertView,
            final ViewGroup parent
    ) {

        final String childText = (String) getChild(groupPosition, childPosition);
        final String childPriceText = (String) getChildPrice(groupPosition, childPosition);
        final Integer childDataIdText = (Integer) getChildDataId(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.food_menu_item_list, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        TextView txtListChildId = (TextView) convertView.findViewById(R.id.tv_item_id);
        TextView textViewPrice = (TextView) convertView.findViewById(R.id.price_food);

        final TextView  textViewAddCart = (TextView)convertView.findViewById(R.id.tv_add_to_cart) ;
        final ImageView increaseImage = (ImageView) convertView.findViewById(R.id.increase);
        final ImageView decreaseImage = (ImageView) convertView.findViewById(R.id.decrease);
        final Intent intent = new Intent(this._context,FoodCartPage.class);


         i =0;//intialize
        increaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                textViewAddCart.setText( " "+String.valueOf(i)+ " ");
                //Date date = new Date();
                int ItemId = _listChildIdData.get(_listDataHeader.get(groupPosition)).get(childPosition);
                int ItemPrice = Integer.valueOf(_listChildPriceData.get(_listDataHeader.get(groupPosition)).get(childPosition));
                String ItemName = _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);

                int Flag = ItemAddCheck(ItemId);
                OrderModel order =  mdb.orderDAO().loadOrderById(ItemId);
                if(Flag == 1){
                    //Toast.makeText(_context, ItemName +" Already added ", Toast.LENGTH_SHORT).show();
                    if(order != null){
                        order.setItemAmount(i);
                        mdb.orderDAO().updateOrder(order);
                    }
                }

            }
        });

        decreaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i >1) {
                    i--;
                    textViewAddCart.setText( " "+String.valueOf(i)+ " ");
                    Date date = new Date();
                    int ItemId = _listChildIdData.get(_listDataHeader.get(groupPosition)).get(childPosition);
                    int ItemPrice = Integer.valueOf(_listChildPriceData.get(_listDataHeader.get(groupPosition)).get(childPosition));
                    String ItemName = _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);


                    int Flag = ItemAddCheck(ItemId);
                    OrderModel order =  mdb.orderDAO().loadOrderById(ItemId);
                    if(Flag == 1){
                        //Toast.makeText(_context, ItemName +" Already added ", Toast.LENGTH_SHORT).show();
                        if(order != null){
                            order.setItemAmount(i);
                            mdb.orderDAO().updateOrder(order);
                        }
                    }
                }
            }
        });


        textViewAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<OrderMerchantModel> orderMerchantModelList =  mdb.orderMercahntDAO().loadOrderMerchant();
                i = 1;

                //add order item if order from same marchant or no add to cart select

                if(orderMerchantModelList.size() ==0  || Integer.valueOf(merchantId)== orderMerchantModelList.get(0).getMerchantId() ) {

                    // Toast.makeText(v.getContext(),"Clicked", Toast.LENGTH_LONG).show();
                    //i;
                    textViewAddCart.setText( " "+String.valueOf(i)+ " ");
                    increaseImage.setVisibility(View.VISIBLE);
                    decreaseImage.setVisibility(View.VISIBLE);
                    //Add to merchant list if no order merchant select
                    if (orderMerchantModelList.size() == 0) {
                        OrderMerchantModel orderMerchantModel = new OrderMerchantModel(Integer.valueOf(merchantId), restaurantName, vat, deliveryCharge);
                        mdb.orderMercahntDAO().insertOrderMerchant(orderMerchantModel);
                    }

                    Date date = new Date();
                    int ItemId = _listChildIdData.get(_listDataHeader.get(groupPosition)).get(childPosition);
                    int ItemPrice = Integer.valueOf(_listChildPriceData.get(_listDataHeader.get(groupPosition)).get(childPosition));
                    String ItemName = _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition);

                    int Flag = ItemAddCheck(ItemId);
                    if(Flag != 1){
                        OrderModel orderModel = new OrderModel(ItemId, ItemName, ItemPrice,i, date);
                        mdb.orderDAO().insertOrder(orderModel);
                    }




                    Snackbar.make(parent.findViewById(R.id.lvExp), " Item has been added to cart", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Go to cart", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    _context.startActivity(intent);
                                }
                            })
                            .show();



                }else{
                    Snackbar.make(parent.findViewById(R.id.lvExp), " Do not order from diffrent restaurant", Snackbar.LENGTH_LONG)
                            .show();
                }
                //i++;
               // textViewAddCart.setText("+ "+ String.valueOf(i)+ " -");
            }
        });

        txtListChild.setText(childText);
        txtListChildId.setText(String.valueOf(childDataIdText));
        textViewPrice.setText(childPriceText + " BDT ");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.food_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}