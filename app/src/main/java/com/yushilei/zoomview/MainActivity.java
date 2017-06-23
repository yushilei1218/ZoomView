package com.yushilei.zoomview;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yushilei.zoomview.adapter.RecyAdapter;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        backImg = ((ImageView) findViewById(R.id.background_img));

        RecyAdapter adapter = new RecyAdapter(this);
        List<String> data = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            data.add("item+" + i);
        }
        adapter.addAll(data);
        recycler.setAdapter(adapter);
    }
}
