package com.example.ppeepfinal;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppeepfinal.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.List;

public class TabFragmentNearbyAdapter extends RecyclerView.Adapter<TabFragmentNearbyAdapter.RestaurantViewHolder> {

    private int mNumberRestaurant;

    private List<String> mData;
    private List<String> mOpeningTime;
    private List<String> mClosingTime;
    private List<String> mCusine;
    private List<String> mLogo;
    private List<String> mBanner;
    final private  ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public TabFragmentNearbyAdapter(
            List<String> RestaurantList,
            List<String> OpeningTime,
            List<String> ClosingingTime,
            List<String> Cusine,
            List<String> Logo,
            List<String> Banner,
            ListItemClickListener listener
    ){
       // mNumberRestaurant= numberOfRestaurant;
        mData = RestaurantList;
        mOpeningTime = OpeningTime;
        mClosingTime = ClosingingTime;
        mCusine = Cusine;
        mLogo = Logo;
        mBanner = Banner;
        mOnClickListener = listener;
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
        String logo = mLogo.get(position);
        String banner= mBanner.get(position);

        restaurantViewHolder.bind(restaurant,opening,closing,cusine,logo,banner);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class RestaurantViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView restaurantNameTextView;
        TextView openingClosingTextView;
        TextView cusineTextview;
        LinearLayout layoutBanner;
        ImageView restaurantLogo,restaurantBanner;
        public RestaurantViewHolder(View itemView){
            super(itemView);
            restaurantNameTextView = (TextView)itemView.findViewById(R.id.tv_item_number);
            openingClosingTextView = (TextView) itemView.findViewById(R.id.tv_opening_closing);
            cusineTextview = (TextView) itemView.findViewById(R.id.tv_cusine);
            restaurantLogo = (ImageView) itemView.findViewById(R.id.image_logo_of_restaurant);
            restaurantBanner = (ImageView) itemView.findViewById(R.id.image_banner_of_restaurant);
            layoutBanner=(LinearLayout) itemView.findViewById(R.id.restaurant_banner) ;
            itemView.setOnClickListener(this);

        }
        void bind(String restaurnatName,String openTime,String closing, String cusine,String logo,String banner){
            if(logo !=null){
                URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+logo);
                Picasso.get().load(getimageUrl.toString()).resize(80,90).into(restaurantLogo);

            }
            if(banner !=null){
                URL getimageUrl = NetworkUtils.buildLoadIamgeUrl(NetworkUtils.LOGO_URL+banner);

                Picasso.get().load(getimageUrl.toString()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        layoutBanner.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }


            restaurantNameTextView.setText(restaurnatName);
            openingClosingTextView.setText(openTime + " - "+closing);
            cusineTextview.setText(cusine);
        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
