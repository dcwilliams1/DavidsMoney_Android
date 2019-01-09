package com.openroad.davidsmoney;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataList extends AppCompatActivity {
    private RecyclerView dataListRecycler;
    private RecyclerView.Adapter dataListAdapter;
    private RecyclerView.LayoutManager dataListLayoutMgr;
    private LiveData<List<BudgetLineItem>> budgetItemDataset;

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

        FetchBudgetData();
    }

    public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
        private LiveData<List<BudgetLineItem>> budgetItemDataset;

        public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
            public TextView budgetItemDescriptionView;
            public TextView budgetItemDateView;
            public TextView budgetItemCategoryView;

            public BudgetItemViewHolder(View itemView) {
                super(itemView);
                budgetItemDescriptionView = itemView.findViewById(R.id.item_description_view);
                budgetItemDateView = itemView.findViewById(R.id.item_date_view);
                budgetItemCategoryView = itemView.findViewById(R.id.item_category_view);
            }
        }

        public BudgetItemAdapter(LiveData<List<BudgetLineItem>> BudgetItemDataSet) {
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
            TextView descriptionView = viewHolder.budgetItemDescriptionView;
            descriptionView.setText(budgetItemDataset.getValue().get(position).getDescription());
            TextView dateView = viewHolder.budgetItemDateView;
            dateView.setText(budgetItemDataset.getValue().get(position).getDate().toString());
            TextView categoryView = viewHolder.budgetItemCategoryView;
            categoryView.setText(budgetItemDataset.getValue().get(position).getCategory());
        }

        @Override
        public int getItemCount() {
            if (budgetItemDataset != null) {
                return budgetItemDataset.getValue().size();
            } else {
                return 0;
            }
        }
    }

    private void FetchBudgetData(){
        final MoneyDatabase db = MoneyDatabase.getDatabase(this);
        final LiveData<List<BudgetLineItem>> data = db.userDao().getAll();
        budgetItemDataset = data;
        String test = "stop here";
    }
}





