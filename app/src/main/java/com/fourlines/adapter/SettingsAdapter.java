package com.fourlines.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.SettingItem;

import java.util.ArrayList;

public class SettingsAdapter extends ArrayAdapter<SettingItem> {
    private ArrayList<SettingItem> settingsList = new ArrayList<SettingItem>();
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public SettingsAdapter(Context context, int textViewResourceId,
                           ArrayList<SettingItem> objects) {
        super(context, textViewResourceId, objects);
        settingsList = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        SettingItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.item_settings, parent, false);

            viewHolder.name = (TextView) row.findViewById(R.id.nameSettings);
            viewHolder.des = (TextView) row.findViewById(R.id.desSettings);

            viewHolder.name.setText(item.getNameSettings());
            viewHolder.des.setText(item.getDesSettings());
            if (item.isStatus()) {
                viewHolder.aSwitch.setChecked(true);
            } else {
                viewHolder.aSwitch.setChecked(false);
            }
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        return row;
    }

    private class ViewHolder {
        TextView name;
        TextView des;
        Switch aSwitch;
    }
}
