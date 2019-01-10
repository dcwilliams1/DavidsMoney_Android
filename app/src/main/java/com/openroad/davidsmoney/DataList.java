package com.openroad.davidsmoney;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class DataList extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private RecyclerView dataListRecycler;
    private RecyclerView.Adapter dataListAdapter;
    private RecyclerView.LayoutManager dataListLayoutMgr;
    private List<BudgetLineItem> budgetItemDataset;
    private Observer<List<BudgetLineItem>> budgetLineItemObserver;
    private MoneyDatabase db;
    public static final String MULTIPLE_DELETION_SUCCESS_CONFIRMATION = "com.openroad.davidsmoney.MULTIPLE_DELETION_SUCCESS_CONFIRMATION";
    public static final String MULTIPLE_DELETION_FAILURE_CONFIRMATION = "com.openroad.davidsmoney.MULTIPLE_DELETION_FAILURE_CONFIRMATION";
    public static final String SINGLE_DELETION_SUCCESS_CONFIRMATION = "com.openroad.davidsmoney.SINGLE_DELETION_SUCCESS_CONFIRMATION";
    public static final String SINGLE_DELETION_FAILURE_CONFIRMATION = "com.openroad.davidsmoney.SINGLE_DELETION_FAILURE_CONFIRMATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = MoneyDatabase.getDatabase(this);

        dataListRecycler = (RecyclerView) findViewById(R.id.recycler_data_list);
        dataListRecycler.setHasFixedSize(true);

        dataListLayoutMgr = new LinearLayoutManager(this);
        dataListRecycler.setLayoutManager(dataListLayoutMgr);

        // Create the observer which updates the UI.
        budgetLineItemObserver = new Observer<List<BudgetLineItem>>() {
            @Override
            public void onChanged(@Nullable final List<BudgetLineItem> data) {
                budgetItemDataset = data;
                dataListAdapter = new BudgetItemAdapter(budgetItemDataset);
                dataListRecycler.setAdapter(dataListAdapter);
            }
        };

        FetchBudgetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_database:
                this.clearDatabase();
                return true;
            case R.id.enter_data:
                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                item.setIntent(mainActivityIntent);
                startActivityForResult(mainActivityIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        final LiveData<List<BudgetLineItem>> results = db.userDao().getAll();
        results.observe(this, budgetLineItemObserver);
    }

    private boolean clearDatabase(){
        return deleteItems(budgetItemDataset);
    }

    private boolean deleteItems(List<BudgetLineItem> itemsToDelete){
        final List<BudgetLineItem> items = itemsToDelete;
        boolean success = false;
        Thread dbThread = new Thread() {
            @Override
            public void run() {
                db.userDao().deleteAll(items);
            }
        };
        dbThread.start();
//        TextView textView = findViewById(R.id.ConfirmationMessage);
        boolean multipleItemsDeleted = itemsToDelete.size() > 1;
        try {
            dbThread.join();
//            textView.setText(getDeletionSuccessMessage(multipleItemsDeleted));
            success = true;
        } catch (Exception ex){
//            textView.setText(getDeletionFalureMessage(multipleItemsDeleted) + ex.getMessage());
        }
        return success;
    }

    private String getDeletionSuccessMessage(boolean multipleDeletions){
        StringBuilder successMessage = new StringBuilder();
        if (multipleDeletions) {
            successMessage.append("The expenses were successfully deleted");
        } else {
            successMessage.append("The expense was successfully deleted");
        }
        return successMessage.toString();
    }

    private String getDeletionFalureMessage(boolean multipleDeletions){
        StringBuilder successMessage = new StringBuilder();
        if (multipleDeletions) {
            successMessage.append("The expenses could not be deleted");
        } else {
            successMessage.append("The expense could not be deleted");
        }
        return successMessage.toString();
    }
}





