package com.fourlines.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.SickType;

import java.util.List;

public class SickTypeListAdapter extends ArrayAdapter<SickType> {

    private ViewHodler hodler;
    private LayoutInflater inflater;

    public SickTypeListAdapter(Context context, int textViewResourceId,
                               List<SickType> objects) {
        super(context, textViewResourceId, objects);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        hodler = new ViewHodler();
        SickType item = getItem(position);
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_sick_type, null);
            hodler.img = (ImageView) convertView.findViewById(R.id.imgSickType);
            hodler.name = (TextView) convertView.findViewById(R.id.nameSickType);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        hodler.img.setImageResource(item.getImgResID());
        hodler.name.setText(item.getNameSickType());
        return convertView;
    }

    private class ViewHodler {
        private ImageView img;
        private TextView name;

    }

}