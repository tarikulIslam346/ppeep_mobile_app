package com.example.ppeepfinal;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private HashMap<String, List<String>> _listChildPriceData;
    private HashMap<String, List<Integer>> _listChildIdData;

    public ExpandableListAdapter(
            Context context,
            List<String> listDataHeader,
            HashMap<String, List<String>> listChildData,
            HashMap<String, List<String>> listChildPriceData,
            HashMap<String, List<Integer>> listChildIdData
    ) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._listChildPriceData = listChildPriceData;
        this._listChildIdData = listChildIdData;
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

    @Override
    public View getChildView(
            int groupPosition,
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
        TextView textViewAddCart = (TextView)convertView.findViewById(R.id.tv_add_to_cart) ;

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