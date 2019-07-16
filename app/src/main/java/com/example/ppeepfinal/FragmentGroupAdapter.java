package com.example.ppeepfinal;

import android.content.Context;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentGroupAdapter extends  RecyclerView.Adapter<FragmentGroupAdapter.FragmentGroupViewHolder> {

    private List<String> mFriendlist;

    private List<Double> mFriendPoint;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public FragmentGroupAdapter (List<String> FriendList, List<Double> FriendPoint,ListItemClickListener listener){
        mFriendlist = FriendList;
        mFriendPoint = FriendPoint;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public FragmentGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();

        int layoutItemOfRestaurant = R.layout.group_list_design;

        LayoutInflater layoutInflater = LayoutInflater.from(context) ;

        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutItemOfRestaurant,viewGroup,shouldAttachToParentImmediately);

        FragmentGroupViewHolder viewHolder = new FragmentGroupViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentGroupViewHolder fragmentGroupViewHolder, int position) {

        String friend = mFriendlist.get(position);

        double totalPoint = mFriendPoint.get(position);

        fragmentGroupViewHolder.bind(friend,totalPoint);
    }

    @Override
    public int getItemCount() {
        return mFriendlist.size();
    }


    class FragmentGroupViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView friendNameTextView,friendPointTextView;

        public FragmentGroupViewHolder(View itemView){
            super(itemView);
            friendNameTextView = (TextView)itemView.findViewById(R.id.tv_friend_name);
            friendPointTextView = (TextView)itemView.findViewById(R.id.tv_total_earn_point);
            itemView.setOnClickListener(this);

        }
        void bind(String friendName,double totalPoint){
            friendNameTextView.setText(friendName);
            friendPointTextView.setTextColor(Color.GREEN);
            friendPointTextView.setText(String.valueOf(totalPoint));
        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
