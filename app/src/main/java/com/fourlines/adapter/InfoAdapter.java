package com.fourlines.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourlines.data.Var;
import com.fourlines.doctor.R;
import com.fourlines.model.InfoItem;

import java.util.ArrayList;
import java.util.List;


public class InfoAdapter extends ArrayAdapter<InfoItem> {

    Typeface font_awesome;
    private Context context;
    private int resource;
    private ViewHolder viewHolder;
    private LayoutInflater inflater;
    private List<InfoItem> infoList = new ArrayList<InfoItem>();


    public InfoAdapter(Context context, int resource,
                       ArrayList<InfoItem> sickItems) {
        super(context, resource, sickItems);
        this.context = context;
        this.resource = resource;
        this.infoList = sickItems;
        font_awesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
    }

    public List<InfoItem> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<InfoItem> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public InfoItem getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder = new ViewHolder();
        InfoItem item = getItem(position);
        View row = convertView;
        if (row == null) {
            inflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_info, parent, false);
            viewHolder.imgAvt = (TextView) row
                    .findViewById(R.id.iconInfo);
            viewHolder.content = (TextView) row.findViewById(R.id.contentInfo);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        viewHolder.imgAvt.setTypeface(font_awesome);
        viewHolder.imgAvt.setBackgroundResource(Var.drawList[position%6]);
        viewHolder.imgAvt.setText(item.getImage());
        viewHolder.content.setText(item.getContent());
        return row;
    }

    private class ViewHolder {
        TextView content;
        TextView imgAvt;
    }
}
