package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            viewHolder.topic = (TextView) row.findViewById(R.id.txtTopic);
            viewHolder.bgAvt = (RelativeLayout) row.findViewById(R.id.bg_avt);
            viewHolder.txtNameAvt = (TextView) row.findViewById(R.id.name_avatar);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        viewHolder.title.setText(item.getTitle());
        viewHolder.body.setText(item.getBody());

        viewHolder.topic.setText(item.getTopic());

        if (item.getImgID() == 0) {

            String name = item.getTitle();
            String name_split[] = name.split(" ");
            int l = name_split.length;
            if (l >= 2) {
                String s = name_split[0].charAt(0) + ""
                        + name_split[1].charAt(0);
                s = s.toUpperCase(Locale.getDefault());
                viewHolder.txtNameAvt.setText(s);
            } else {
                viewHolder.txtNameAvt.setText(name.charAt(0) + "");

            }
            viewHolder.bgAvt.setBackgroundResource(Var.drawList[(position + 3) % 6]);
        } else {
            viewHolder.img.setImageResource(item.getImgID());
        }

        return row;
    }

    private class ViewHolder {
        TextView title, topic, body;
        ImageView img;
        //avatar
        RelativeLayout bgAvt;
        TextView txtNameAvt;
    }
}
