package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.SickItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SickListAdapter extends ArrayAdapter<SickItem> {

    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<SickItem> sickList = new ArrayList<SickItem>();

    public SickListAdapter(Context context, int resource,
                           ArrayList<SickItem> sickItems) {
        super(context, resource, sickItems);
        this.context = context;
        this.resource = resource;
        this.sickList = sickItems;
    }

    public List<SickItem> getSickList() {
        return sickList;
    }

    public void setSickList(List<SickItem> sickList) {
        this.sickList = sickList;
    }

    @Override
    public int getCount() {
        return sickList.size();
    }

    @Override
    public SickItem getItem(int position) {
        return sickList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        SickItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_sick_list, parent, false);
            viewHolder.sickName = (TextView) row
                    .findViewById(R.id.txt_sick_name);
            viewHolder.img = (ImageView) row.findViewById(R.id.img_sick_avatar);
            viewHolder.des = (TextView) row.findViewById(R.id.txtDescription);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.sickName.setText(item.getName());
        viewHolder.img.setImageResource(R.drawable.avatar_sick);
        viewHolder.des.setText(item.getDescription());
        return row;
    }

    private class ViewHolder {
        TextView sickName;
        ImageView img;
        TextView des;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sickList.clear();
        if (charText.length() == 0) {
            sickList.addAll(sickList);
        } else {
            for (SickItem wp : sickList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(
                        charText)) {
                    sickList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
