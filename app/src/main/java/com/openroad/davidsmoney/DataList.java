package com.openroad.davidsmoney;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DataList extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    private RecyclerView dataListRecycler;
    private RecyclerView.Adapter dataListAdapter;
    private List<BudgetLineItem> budgetItemDataset;
    private Observer<List<BudgetLineItem>> budgetLineItemObserver;
    private MoneyDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = MoneyDatabase.getDatabase(this);
        RecyclerView.LayoutManager dataListLayoutMgr;

        dataListRecycler = findViewById(R.id.recycler_data_list);
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
                if (data.size() < 1){
                    this.toggleListView(View.GONE);
                    this.toggleEmptyDataMessage(View.VISIBLE);
                } else {
                    this.toggleListView(View.VISIBLE);
                    this.toggleEmptyDataMessage(View.GONE);
                }
            }

            private void toggleListView(int visibility){
                RecyclerView dataList = findViewById(R.id.recycler_data_list);
                dataList.setVisibility(visibility);
            }

            private void toggleEmptyDataMessage(int visibility){
                TextView dataList = findViewById(R.id.view_empty_data_message);
                dataList.setVisibility(visibility);
            }
        };

        FetchBudgetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                mainActivityIntent.setAction(Intent.ACTION_MAIN);
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
        private final List<BudgetLineItem> budgetItemDataset;
        private  Context appContext;

        public class BudgetItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView budgetItemDescriptionView;
            final TextView budgetItemDateView;
            TextView budgetItemCategoryView;
            TextView budgetItemAmountView;

            BudgetItemViewHolder(View itemView) {
                super(itemView);
                budgetItemDescriptionView = itemView.findViewById(R.id.item_description_view);
                budgetItemDateView = itemView.findViewById(R.id.item_date_view);
                budgetItemCategoryView = itemView.findViewById(R.id.item_category_view);
                budgetItemAmountView = itemView.findViewById(R.id.item_amount_view);
                itemView.setOnClickListener(this);
                appContext = getApplicationContext();
            }

            @Override
            public void onClick(final View view) {
                int itemPosition = dataListRecycler.getChildLayoutPosition(view);
                BudgetLineItem item = budgetItemDataset.get(itemPosition);
                EditBudgetLineItem(item, view);
            }

            void EditBudgetLineItem(BudgetLineItem item, View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setAction(Intent.ACTION_EDIT);
                Bundle dataBundle = new Bundle();
                dataBundle.putLong(appContext.getString(R.string.amount_property), item.getAmount());
                dataBundle.putString(appContext.getString(R.string.description_property), item.getDescription());
                dataBundle.putString(appContext.getString(R.string.category_property), item.getCategory());
                dataBundle.putLong(appContext.getString(R.string.date_property), Converters.dateToTimestamp(item.getDate()));
                dataBundle.putInt(appContext.getString(R.string.line_item_id_property), item.getLineItemId());
                dataBundle.putBoolean(appContext.getString(R.string.in_edit_mode), true);
                intent.putExtras(dataBundle);

                startActivity(intent);
            }
        }

        BudgetItemAdapter(List<BudgetLineItem> BudgetItemDataSet) {
            budgetItemDataset = BudgetItemDataSet;
        }


        @NonNull
        public BudgetItemAdapter.BudgetItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View budgetItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_layout, parent, false);
            BudgetItemViewHolder budgetItemViewHolder = new BudgetItemViewHolder(budgetItemView);

            return budgetItemViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BudgetItemViewHolder viewHolder, int position) {
            TextView amountView = viewHolder.budgetItemAmountView;
            amountView.setText(appContext.getString(R.string.dollar_currency_amount, budgetItemDataset.get(position).getAmount().toString()));
            TextView descriptionView = viewHolder.budgetItemDescriptionView;
            descriptionView.setText(budgetItemDataset.get(position).getDescription());
            TextView dateView = viewHolder.budgetItemDateView;
            java.util.Date complexDate = budgetItemDataset.get(position).getDate();
            dateView.setText(new SimpleDateFormat(appContext.getString(R.string.DEFAULT_DATE_FORMAT), Locale.getDefault()).format(complexDate));
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

    private void clearDatabase(){
        deleteItems(budgetItemDataset);
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
        boolean multipleItemsDeleted = itemsToDelete.size() > 1;
        String returnMessage = getDeletionSuccessMessage(multipleItemsDeleted);
        try {
            dbThread.join();
            success = true;
        } catch (Exception ex){
            returnMessage = getString(R.string.friendly_error_with_detail, getDeletionFailureMessage(multipleItemsDeleted), ex.getMessage());
        }
        ShowMessage(returnMessage);
        return success;
    }

    private void ShowMessage(String message){
        Context context = getApplicationContext();
        View view = findViewById(R.id.list_display_view);
        Popup.ShowMessageWindow(context, view, message);
    }

    private String getDeletionSuccessMessage(boolean multipleDeletions){
        StringBuilder successMessage = new StringBuilder();
        if (multipleDeletions) {
            successMessage.append(this.getString(R.string.multiple_expense_deletion_success_message));
        } else {
            successMessage.append(this.getString(R.string.single_expense_deletion_success_message));
        }
        return successMessage.toString();
    }

    private String getDeletionFailureMessage(boolean multipleDeletions){
        StringBuilder failureMessage = new StringBuilder();
        if (multipleDeletions) {
            failureMessage.append(this.getString(R.string.multiple_expense_deletion_failure_message));
        } else {
            failureMessage.append(this.getString(R.string.single_expense_deletion_failure_message));
        }
        return failureMessage.toString();
    }

}





