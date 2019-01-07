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
    private String[] budgetItemDataset = new String[]{"one", "two", "three"};

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
        dataListAdapter = new BudgetItemAdapter(budgetItemDataset);

        dataListRecycler.setLayoutManager(dataListLayoutMgr);
        dataListRecycler.setAdapter(dataListAdapter);
    }

    public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
        private String[] budgetItemDataset;

        public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
            public TextView budgetItemTextView;

            public BudgetItemViewHolder(View itemView) {
                super(itemView);
                budgetItemTextView = itemView.findViewById(R.id.item_text_view);
            }
        }

        public BudgetItemAdapter(String[] BudgetItemDataSet) {
            budgetItemDataset = BudgetItemDataSet;
        }


        public BudgetItemAdapter.BudgetItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View budgetItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_layout, parent, false);
            BudgetItemViewHolder budgetItemViewHolder = new BudgetItemViewHolder(budgetItemView);

            return budgetItemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BudgetItemViewHolder viewHolder, int position) {
            TextView itemView = viewHolder.budgetItemTextView;
            itemView.setText("one item again");
        }

        @Override
        public int getItemCount() {
            return budgetItemDataset.length;
        }
    }
}





