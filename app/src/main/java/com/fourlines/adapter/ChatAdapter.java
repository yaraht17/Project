package com.fourlines.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourlines.doctor.R;
import com.fourlines.model.ChatMessage;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private ArrayList<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    public ChatAdapter(Context context, int textViewResourceId,
                       ArrayList<ChatMessage> objects) {
        super(context, textViewResourceId, objects);
        chatMessageList = objects;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        inflater = (LayoutInflater) this.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left == true) {
            row = inflater.inflate(R.layout.item_chat_left, parent, false);
        } else {
            row = inflater.inflate(R.layout.item_chat_right, parent, false);
        }
        viewHolder.message = (TextView) row.findViewById(R.id.msgr);
        viewHolder.avatar = (ImageView) row.findViewById(R.id.avatar);
        row.setTag(viewHolder);
        viewHolder.message.setText(chatMessageObj.message);
        if (chatMessageObj.left == true) {
            viewHolder.avatar.setImageResource(R.drawable.bacsi);
        } else {
            viewHolder.avatar.setImageResource(R.drawable.benhnhan);
            String path = Environment.getExternalStorageDirectory() + "/SAM/pictures/avatar.png";
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                Log.d("TienDH", "bitmap not null");
                bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
                viewHolder.avatar.setImageBitmap(bitmap);
            } else {
                viewHolder.avatar.setImageResource(R.drawable.benhnhan);
            }

        }
        return row;
    }

    private class ViewHolder {
        TextView message;
        ImageView avatar;
    }


}