package com.fourlines.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fourlines.adapter.NotificationAdapter;
import com.fourlines.doctor.R;
import com.fourlines.model.NotificationItem;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {


    private ArrayList<NotificationItem> notifList;
    private ListView notifListView;
    private NotificationAdapter notifListAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_notification,
                container, false);
        notifListView = (ListView) rootView.findViewById(R.id.listNotif);
        notifList = createNotif();
        notifListAdapter = new NotificationAdapter(rootView.getContext(), R.layout.item_notif, notifList);
        notifListView.setAdapter(notifListAdapter);
        return rootView;
    }

    private ArrayList<NotificationItem> createNotif() {
        ArrayList<NotificationItem> list = new ArrayList<>();
        list.add(new NotificationItem("", R.drawable.ic_doctor, "Title Demo", "12:00 Hôm nay", "AAACASDJAJDJADSAJDJSADASDJAJSDJAJD"));
        list.add(new NotificationItem("", R.drawable.ic_doctor, "Title Demo", "12:00 Hôm nay", "Bệnh thận thường diễn biến âm thầm nên có khi phát hiện được bệnh thì đã ở giai đoạn suy thận và cần phải tiến hành sự điều trị phức tạp hơn…Bệnh thận đôi khi là do vi khuẩn streptocoques gây nên"));
        list.add(new NotificationItem("", R.drawable.ic_doctor, "Title Demo", "12:00 Hôm nay", "AAACASDJAJDJADSAJDJSADASDJAJSDJAJD"));
        list.add(new NotificationItem("", R.drawable.ic_doctor, "Title Demo", "12:00 Hôm nay", "Bệnh thận thường diễn biến âm thầm nên có khi phát hiện được bệnh thì đã ở giai đoạn suy thận và cần phải tiến hành sự điều trị phức tạp hơn…Bệnh thận đôi khi là do vi khuẩn streptocoques gây nên"));

        return list;
    }


}
