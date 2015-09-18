package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.GroupItem;
import com.fourlines.model.ItemDetail;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<GroupItem> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<GroupItem, List<ItemDetail>> listDataChild;

    public ExpandableListAdapter(Context context, List<GroupItem> listDataHeader,
                                 HashMap<GroupItem, List<ItemDetail>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ItemDetail item = (ItemDetail) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_list_detail, null);
        }

        TextView header = (TextView) convertView
                .findViewById(R.id.txtHeader);
        TextView body = (TextView) convertView
                .findViewById(R.id.txtBody);
        header.setText(item.getHeader());
        body.setText(item.getBody());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupItem item = (GroupItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_group, null);
        }

        TextView groupTitle = (TextView) convertView
                .findViewById(R.id.groupTitle);
        ImageView imageStatus = (ImageView) convertView.findViewById(R.id.imageStatus);
        groupTitle.setText(item.getContent());

        if (isExpanded) {
            imageStatus.setImageResource(R.drawable.open);
        } else {
            imageStatus.setImageResource(R.drawable.close);
        }

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
