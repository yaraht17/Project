package com.fourlines.doctor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class SickDetailActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<GroupItem> listDataHeader;
    HashMap<GroupItem, List<ItemDetail>> listDataChild;

    private TextView txtSickName;
    private TextView txtSickType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sick_detail);

        SickItem item = (SickItem) getIntent().getSerializableExtra(Var.SICK_KEY);

        expListView = (ExpandableListView) findViewById(R.id.sickDetail);
        txtSickName = (TextView) findViewById(R.id.sickName);
        txtSickType = (TextView) findViewById(R.id.sickType);


        txtSickName.setText(item.getName());
        txtSickType.setText(item.getType());

        createList();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });
    }

    private void createList() {
        listDataHeader = new ArrayList<GroupItem>();
        listDataChild = new HashMap<GroupItem, List<ItemDetail>>();
        GroupItem groupItem = new GroupItem("Nguyên Nhân", false);

        // Adding child data
        ItemDetail item = new ItemDetail("Header", "Chúng tôi là những người trẻ, năng động và đầy nhiệt huyết mong muốn tìm hiểu và phát triển đưa công nghệ mới áp dụng và giải quyết các vấn đề của cuộc sống, để chất lượng cuộc sống ngày càng được cải thiện.");
        listDataHeader.add(groupItem);
        listDataHeader.add(new GroupItem("Triệu Chứng", false));
        listDataHeader.add(new GroupItem("Cách Điều Trị", false));
        listDataHeader.add(new GroupItem("Dinh Dưỡng", false));
        // Adding child data
        List<ItemDetail> listItem = new ArrayList<ItemDetail>();
        listItem.add(item);


        listDataChild.put(listDataHeader.get(0), listItem); // Header, Child data
        listDataChild.put(listDataHeader.get(1), listItem);
        listDataChild.put(listDataHeader.get(2), listItem);
        listDataChild.put(listDataHeader.get(3), listItem);
    }


}
