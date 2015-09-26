package com.fourlines.doctor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fourlines.adapter.ExpandableListAdapter;
import com.fourlines.data.Var;
import com.fourlines.model.GroupItem;
import com.fourlines.model.ItemDetail;
import com.fourlines.model.SickItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SickDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<GroupItem> listDataHeader;
    HashMap<GroupItem, List<ItemDetail>> listDataChild;

    private TextView txtSickName;
    private TextView txtSickType;
    private SickItem sickItem;
    private Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_detail);

        sickItem = (SickItem) getIntent().getSerializableExtra(Var.SICK_KEY);

        expListView = (ExpandableListView) findViewById(R.id.sickDetail);
        txtSickName = (TextView) findViewById(R.id.sickName);
        txtSickType = (TextView) findViewById(R.id.sickType);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        txtSickName.setText(sickItem.getName());
        txtSickType.setText(sickItem.getType());

        createList();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


    }

    private void createList() {
        listDataHeader = new ArrayList<GroupItem>();
        listDataChild = new HashMap<GroupItem, List<ItemDetail>>();

        // Adding child data
        ItemDetail item = new ItemDetail("Header", "Chúng tôi là những người trẻ, năng động và đầy nhiệt huyết mong muốn tìm hiểu và phát triển đưa công nghệ mới áp dụng và giải quyết các vấn đề của cuộc sống, để chất lượng cuộc sống ngày càng được cải thiện.");
        listDataHeader.add(new GroupItem("Nguyên Nhân", false));
        listDataHeader.add(new GroupItem("Triệu Chứng", false));
        listDataHeader.add(new GroupItem("Cách Điều Trị", false));
        listDataHeader.add(new GroupItem("Dinh Dưỡng", false));
        listDataHeader.add(new GroupItem("Phòng Tránh", false));
        // Adding child data
        List<ItemDetail> reason = new ArrayList<ItemDetail>();
        reason.add(new ItemDetail("", sickItem.getReason()));

        List<ItemDetail> prevention = new ArrayList<ItemDetail>();
        prevention.add(new ItemDetail("", sickItem.getPrevention()));

        List<ItemDetail> treatment = new ArrayList<ItemDetail>();
        treatment.add(new ItemDetail("", sickItem.getTreatment()));

        List<ItemDetail> nutri = new ArrayList<ItemDetail>();
        nutri.add(new ItemDetail("Ăn gì?", sickItem.getFoods()));
        nutri.add(new ItemDetail("Kiêng gì?", sickItem.getBanFoods()));

        List<ItemDetail> symptons = new ArrayList<ItemDetail>();
        for (String s : sickItem.getSymptoms()) {
            s = Character.toUpperCase(s.charAt(0)) + s.substring(1);
            symptons.add(new ItemDetail("", s));
        }

        listDataChild.put(listDataHeader.get(0), reason); // Header, Child data
        listDataChild.put(listDataHeader.get(1), symptons);
        listDataChild.put(listDataHeader.get(2), treatment);
        listDataChild.put(listDataHeader.get(3), nutri);
        listDataChild.put(listDataHeader.get(4), prevention);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBack) {
            finish();
        }
    }
}
