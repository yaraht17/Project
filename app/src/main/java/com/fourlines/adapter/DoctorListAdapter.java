package com.fourlines.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.DoctorItem;

import java.util.ArrayList;

public class DoctorListAdapter extends ArrayAdapter<DoctorItem> {
    private ArrayList<DoctorItem> doctorList = new ArrayList<DoctorItem>();
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public DoctorListAdapter(Context context, int textViewResourceId,
                             ArrayList<DoctorItem> objects) {
        super(context, textViewResourceId, objects);
        doctorList = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        DoctorItem doctorItem = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.item_doctor, parent, false);

            viewHolder.name = (TextView) row.findViewById(R.id.txtDoctorName);
            viewHolder.image = (ImageView) row.findViewById(R.id.imgDoctorAvt);

            viewHolder.name.setText(doctorItem.getDoctorName());
            viewHolder.image.setImageResource(doctorItem.getDoctorAvt());
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }
        return row;
    }

    private class ViewHolder {
        TextView name;
        ImageView image;
    }
}
