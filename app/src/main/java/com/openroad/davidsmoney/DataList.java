package com.openroad.davidsmoney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataList extends AppCompatActivity {
    private RecyclerView dataListRecycler;
    private RecyclerView.Adapter dataListAdapter;
    private RecyclerView.LayoutManager dataListLayoutMgr;
    private String[] dataSet = new String[] {"one", "two", "three"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataListRecycler = (RecyclerView) findViewById(R.id.recycler_data_list);
        dataListRecycler.setHasFixedSize(true);

        dataListLayoutMgr = new LinearLayoutManager(this);
        dataListRecycler.setLayoutManager(dataListLayoutMgr);

        dataListAdapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                TextView budgetItemView  = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_text_view, parent, false);
                RecyclerView.ViewHolder budgetItemViewHolder = new RecyclerView.ViewHolder(budgetItemView) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                return budgetItemViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                
            }

            @Override
            public int getItemCount() {
                return 0;
            }


        };

        dataListRecycler.setAdapter(dataListAdapter);
    }

}
