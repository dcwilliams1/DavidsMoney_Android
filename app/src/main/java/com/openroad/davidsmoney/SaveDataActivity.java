package com.openroad.davidsmoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaveDataActivity extends AppCompatActivity {

    private MoneyDatabase db;
    private boolean InEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MoneyDatabase.getDatabase(this);
        setContentView(R.layout.activity_save_data);
        SaveBudgetLineItem();

    }

    private void SaveBudgetLineItem() {
        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        InEditMode = intent.getBooleanExtra(this.getString(R.string.in_edit_mode), false);
        final String amount = intent.getStringExtra(this.getString(R.string.amount_property));
        final String description = intent.getStringExtra(this.getString(R.string.description_property));
        final String category = intent.getStringExtra(this.getString(R.string.category_property));
        final String dateString = intent.getStringExtra(this.getString(R.string.date_property));
        final Integer lineItemId = intent.getIntExtra(this.getString(R.string.line_item_id_property), 0);

        final java.util.Date expenseDate;
        java.util.Date tempDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.getString(R.string.DEFAULT_DATE_FORMAT), Locale.ENGLISH);
        try {
            tempDate = dateFormat.parse(dateString);
        } catch (java.text.ParseException parsingError){
            tempDate = new java.util.Date();
        }
        expenseDate = tempDate;
        String successMessage = this.getString(R.string.expense_saving_success_messge, amount, category, description, dateFormat.format(expenseDate));
        String failureMessage = this.getString(R.string.expense_saving_failure_messge);

        Thread dbThread = new Thread() {
            @Override
            public void run() {
                BudgetLineItem expense =  new BudgetLineItem();
                expense.setAmount(Long.parseLong(amount));
                expense.setDescription(description);
                expense.setCategory(category);
                expense.setDate(expenseDate);

                if (InEditMode) {
                    expense.setLineItemId(lineItemId);
                    db.userDao().updateBudgetLineItem(expense);
                } else {
                    db.userDao().insertBudgetLineItem(expense);
                }
            }
        };
        dbThread.start();
        TextView textView = findViewById(R.id.ConfirmationMessage);
        try {
            dbThread.join();
            Intent finishedSavingIntent = new Intent(this, MainActivity.class);
            finishedSavingIntent.setAction(Intent.ACTION_MAIN);
            if(InEditMode){
                finishedSavingIntent.setClass(this, DataList.class);
                finishedSavingIntent.setAction(Intent.ACTION_VIEW);
            }
            startActivityForResult(finishedSavingIntent, 0);
        } catch (Exception ex){
            textView.setText(this.getString(R.string.friendly_error_with_detail, failureMessage,  ex.getMessage()));
        }
    }
}
