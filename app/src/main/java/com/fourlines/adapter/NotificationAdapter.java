package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {

    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<NotificationItem> notifList = new ArrayList<NotificationItem>();

    public NotificationAdapter(Context context, int resource,
                               ArrayList<NotificationItem> notifs) {
        super(context, resource, notifs);
        this.context = context;
        this.resource = resource;
        this.notifList = notifs;
    }


    @Override
    public int getCount() {
        return notifList.size();
    }

    @Override
    public NotificationItem getItem(int position) {
        return notifList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        NotificationItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_notif, parent, false);
            viewHolder.title = (TextView) row
                    .findViewById(R.id.txtTitle);
            viewHolder.body = (TextView) row.findViewById(R.id.txtBody);
            viewHolder.img = (ImageView) row.findViewById(R.id.imgNotifIcon);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.title.setText(item.getTitle());
        viewHolder.body.setText(item.getBody());
        viewHolder.img.setImageResource(R.drawable.ava_bacsi);
        return row;
    }

    private class ViewHolder {
        TextView title, time, body;
        ImageView img;
    }
}
