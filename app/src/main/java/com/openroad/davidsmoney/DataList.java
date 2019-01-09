package com.openroad.davidsmoney;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class DataList extends AppCompatActivity {
    private RecyclerView dataListRecycler;
    private RecyclerView.Adapter dataListAdapter;
    private RecyclerView.LayoutManager dataListLayoutMgr;
    private List<BudgetLineItem> budgetItemDataset;
    private Observer<List<BudgetLineItem>> budgetLineItemObserver;

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



        // Create the observer which updates the UI.
        budgetLineItemObserver = new Observer<List<BudgetLineItem>>() {
            @Override
            public void onChanged(@Nullable final List<BudgetLineItem> data) {
                budgetItemDataset = data;
                dataListAdapter = new BudgetItemAdapter(budgetItemDataset);
                dataListRecycler.setLayoutManager(dataListLayoutMgr);
                dataListRecycler.setAdapter(dataListAdapter);
            }
        };
        FetchBudgetData();
    }

    public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {
        private List<BudgetLineItem> budgetItemDataset;

        public class BudgetItemViewHolder extends RecyclerView.ViewHolder {
            public TextView budgetItemDescriptionView;
            public TextView budgetItemDateView;
            public TextView budgetItemCategoryView;
            public TextView budgetItemAmountView;

            public BudgetItemViewHolder(View itemView) {
                super(itemView);
                budgetItemDescriptionView = itemView.findViewById(R.id.item_description_view);
                budgetItemDateView = itemView.findViewById(R.id.item_date_view);
                budgetItemCategoryView = itemView.findViewById(R.id.item_category_view);
                budgetItemAmountView = itemView.findViewById(R.id.item_amount_view);
            }
        }

        public BudgetItemAdapter(List<BudgetLineItem> BudgetItemDataSet) {
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
            TextView amountView = viewHolder.budgetItemAmountView;
            amountView.setText("$" + budgetItemDataset.get(position).getAmount().toString());
            TextView descriptionView = viewHolder.budgetItemDescriptionView;
            descriptionView.setText(budgetItemDataset.get(position).getDescription());
            TextView dateView = viewHolder.budgetItemDateView;
            java.util.Date complexDate = budgetItemDataset.get(position).getDate();
            dateView.setText(new SimpleDateFormat("M/d/yyyy", Locale.getDefault()).format(complexDate));
            TextView categoryView = viewHolder.budgetItemCategoryView;
            categoryView.setText(budgetItemDataset.get(position).getCategory());
        }

        @Override
        public int getItemCount() {
            if (budgetItemDataset != null) {
                return budgetItemDataset.size();
            } else {
                return 0;
            }
        }
    }

    private void FetchBudgetData(){

        final MoneyDatabase db = MoneyDatabase.getDatabase(this);
        final LiveData<List<BudgetLineItem>> results = db.userDao().getAll();
        results.observe(this, budgetLineItemObserver);

//        List<BudgetLineItem> data = new ArrayList<BudgetLineItem>();
//
//        BudgetLineItem firstRecord = new BudgetLineItem();
//        firstRecord.setLineItemId(1);
//        firstRecord.setDate(new java.util.Date());
//        firstRecord.setAmount(new Long("1249"));
//        firstRecord.setCategory("Clothes");
//        firstRecord.setDescription("new record - bought some nice clothes to wear");
//        data.add(firstRecord);
//
//        BudgetLineItem secondRecord = new BudgetLineItem();
//        secondRecord.setLineItemId(2);
//        secondRecord.setDate(new java.util.Date());
//        secondRecord.setAmount(new Long("808"));
//        secondRecord.setCategory("Home Maintenance");
//        secondRecord.setDescription("stuff from Home Depot");
//        data.add(secondRecord);
//
//        budgetItemDataset = data;
        String test = "stop here";
    }
}





