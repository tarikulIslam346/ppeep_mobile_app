package com.example.ppeepfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ppeepfinal.data.OrderModel;
import com.example.ppeepfinal.data.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoodCartPage extends AppCompatActivity  implements   FoodCartPageAdapter.ListItemClickListener{

    private RecyclerView mListOfCartItem;
    private FoodCartPageAdapter foodCartPageAdapter;
    private UserDatabase mdb;
    List<String>OrderItem;
    List<Integer>OrderItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart_page);
        mListOfCartItem = (RecyclerView)findViewById(R.id.recycler_cart);
        mdb = UserDatabase.getInstance(getApplicationContext());//intantiate room database
        List<OrderModel> order =  mdb.orderDAO().loadOrder();//select all data form room database user table
        OrderItem = new ArrayList<String>();
        OrderItemPrice = new ArrayList<Integer>();
        if(order.size()!= 0){//if data exist
            for(int i=0;i<order.size();i++) {
                OrderItem.add(order.get(i).getItemName());// set order item name
                OrderItemPrice.add(order.get(i).getItemPrice());// set order item price

            }
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mListOfCartItem.setLayoutManager(layoutManager);
        mListOfCartItem.setHasFixedSize(true);
        foodCartPageAdapter = new FoodCartPageAdapter(OrderItem ,OrderItemPrice,this);
        mListOfCartItem.setAdapter(foodCartPageAdapter);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        /*int clickedRestaurnat = MerchantId.get(clickedItemIndex).intValue();
        String restaurantName = allNames.get(clickedItemIndex);
        String cusine = Cusines.get(clickedItemIndex);
        //Toast.makeText(getContext(),"restaurant id" +clickedRestaurnat ,Toast.LENGTH_SHORT).show();
        Intent foodmenuIntent = new Intent(getContext(),RestaurantMenuPage.class);
        foodmenuIntent.putExtra("mercahnt_Id",String.valueOf(clickedRestaurnat));
        foodmenuIntent.putExtra("restaurant_name",restaurantName);
        foodmenuIntent.putExtra("cuisine",cusine);
        startActivity(foodmenuIntent);*/



    }
}
