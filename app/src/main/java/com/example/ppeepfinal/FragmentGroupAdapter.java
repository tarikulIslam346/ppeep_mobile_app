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

import java.util.List;

public class FragmentGroupAdapter extends  RecyclerView.Adapter<FragmentGroupAdapter.FragmentGroupViewHolder> {

    private List<String> mFriendlist;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }


    public FragmentGroupAdapter (List<String> FriendList, ListItemClickListener listener){
        mFriendlist = FriendList;
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

        fragmentGroupViewHolder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return mFriendlist.size();
    }


    class FragmentGroupViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView friendNameTextView;

        public FragmentGroupViewHolder(View itemView){
            super(itemView);
            friendNameTextView = (TextView)itemView.findViewById(R.id.tv_friend_name);
            itemView.setOnClickListener(this);

        }
        void bind(String friendName){
            friendNameTextView.setText(friendName);
        }
        public void onClick(View v){
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

    }
}
