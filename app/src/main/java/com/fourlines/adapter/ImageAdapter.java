package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.SickType;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SickType> items;
    int size;

    public ImageAdapter(Context c, int size, ArrayList<SickType> items) {
        mContext = c;
        this.size = size;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SickType item = items.get(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_sick_type, parent, false);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.nameSickType);
        ImageView imageView = (ImageView) view.findViewById(R.id.imgSickType);
        imageView.setImageResource(item.getImgResID());
        textView.setText(item.getNameSickType());

        return view;
    }


}

