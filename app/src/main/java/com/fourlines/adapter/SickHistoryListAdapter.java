package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.SickHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SickHistoryListAdapter extends ArrayAdapter<SickHistoryItem> {

    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<SickHistoryItem> sickList = new ArrayList<SickHistoryItem>();

    public SickHistoryListAdapter(Context context, int resource,
                                  ArrayList<SickHistoryItem> sickItems) {
        super(context, resource, sickItems);
        this.context = context;
        this.resource = resource;
        this.sickList = sickItems;
    }


    public List<SickHistoryItem> getSickList() {
        return sickList;
    }

    public void setSickList(List<SickHistoryItem> sickList) {
        this.sickList = sickList;
    }

    @Override
    public int getCount() {
        return sickList.size();
    }

    @Override
    public SickHistoryItem getItem(int position) {
        return sickList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        SickHistoryItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_sick_history, parent, false);
            viewHolder.sickName = (TextView) row
                    .findViewById(R.id.txtSickName);
//            viewHolder.img = (ImageView) row.findViewById(R.id.img_sick_avatar);
            viewHolder.date = (TextView) row.findViewById(R.id.txtDate);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.sickName.setText(item.getSickItem().getName());
//        viewHolder.img.setImageResource(R.drawable.avatar_sick);
        viewHolder.date.setText(item.getDatetime());
        return row;
    }

    private class ViewHolder {
        TextView sickName;
        ImageView img;
        TextView date;
    }
}