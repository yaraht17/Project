package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.DrawerItem;

import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    private LayoutInflater inflater;
    private DrawerItemHolder drawerHolder;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        View view = convertView;
        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);
        if (view == null) {
            if (dItem.getItemName() != null) {
                drawerHolder = new DrawerItemHolder();
                view = inflater.inflate(R.layout.custom_drawer_item_setting, parent, false);
                //item
                drawerHolder.itemLayout = (LinearLayout) view
                        .findViewById(R.id.itemLayout);
                drawerHolder.itemName = (TextView) view
                        .findViewById(R.id.drawer_itemName);
                drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
                //set view
                drawerHolder.itemName.setText(dItem.getItemName());
                drawerHolder.icon.setImageResource(dItem.getImgResID());
                view.setTag(drawerHolder);
            } else {
                //header
                drawerHolder = new DrawerItemHolder();
                view = inflater.inflate(R.layout.custom_drawer_item, parent, false);

                drawerHolder.headerLayout = (LinearLayout) view
                        .findViewById(R.id.headerLayout);
                drawerHolder.avatarUser = (ImageView) view.findViewById(R.id.avatarUser);
                drawerHolder.nameUser = (TextView) view.findViewById(R.id.nameUser);
                drawerHolder.statusUser = (TextView) view.findViewById(R.id.statusUser);
                //set view
                drawerHolder.avatarUser.setImageResource(
                        dItem.getImgResID());
                drawerHolder.nameUser.setText(dItem.getNameUser());
                drawerHolder.statusUser.setText(dItem.getStatusUser());
                view.setTag(drawerHolder);
            }
        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();
        }
        return view;
    }

    private static class DrawerItemHolder {
        TextView nameUser, statusUser;
        TextView itemName;
        ImageView icon, avatarUser;
        LinearLayout headerLayout, itemLayout;
    }
}