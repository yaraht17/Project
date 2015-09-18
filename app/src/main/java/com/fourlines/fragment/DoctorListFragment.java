package com.fourlines.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fourlines.adapter.DoctorListAdapter;
import com.fourlines.data.Var;
import com.fourlines.doctor.ChatActivity;
import com.fourlines.doctor.R;
import com.fourlines.model.DoctorItem;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<DoctorItem> doctorList;
    private ListView doctorListView;
    private DoctorListAdapter doctorListAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_chat,
                container, false);
        doctorListView = (ListView) rootView.findViewById(R.id.doctorList);
        doctorList = createDoctor();
        doctorListAdapter = new DoctorListAdapter(rootView.getContext(), R.layout.item_doctor, doctorList);
        doctorListView.setAdapter(doctorListAdapter);
        doctorListView.setOnItemClickListener(this);
        return rootView;
    }

    private ArrayList<DoctorItem> createDoctor() {
        ArrayList<DoctorItem> list = new ArrayList<>();
        DoctorItem doctorItem = new DoctorItem("Bác Sĩ Hoa Súng", R.drawable.ava_bacsi);
        list.add(doctorItem);
        DoctorItem doctorItem1 = new DoctorItem("Bác Sĩ Hoa Sen", R.drawable.ic_doctor);
        list.add(doctorItem1);
        list.add(new DoctorItem("Bác Sĩ Đa Khoa", R.drawable.ava_bacsi));
        list.add(new DoctorItem("Bác Sĩ Nhi Khoa", R.drawable.ic_doctor));
        list.add(new DoctorItem("Bác Sĩ Tai Mũi Họng", R.drawable.ava_bacsi));
        list.add(new DoctorItem("Bác Sĩ Dởm", R.drawable.ic_doctor));
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DoctorItem doctorItem = doctorList.get(position);
        Intent intent = new Intent(rootView.getContext(), ChatActivity.class);
        intent.putExtra(Var.DOCTOR_KEY, doctorItem);
        startActivity(intent);
    }
}