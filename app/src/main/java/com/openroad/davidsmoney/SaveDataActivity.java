package com.openroad.davidsmoney;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.provider.LiveFolders.INTENT;
import static com.openroad.davidsmoney.R.id.ConfirmationMessage;

public class SaveDataActivity extends AppCompatActivity {

    public static final String DATA_SAVED_CONFIRMATION = "com.openroad.davidsmoney.DATA_SAVED_CONFIRMATION";
    public static final String DATA_NOT_SAVED_CONFIRMATION = "com.openroad.davidsmoney.DATA_NOT_SAVED_CONFIRMATION";
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
        InEditMode = intent.getBooleanExtra("inEditMode", false);
        final String amount = intent.getStringExtra("amount");
        final String description = intent.getStringExtra("description");
        final String category = intent.getStringExtra("category");
        final String dateString = intent.getStringExtra("date");
        final Integer lineItemId = intent.getIntExtra("lineItemId", 0);

        final java.util.Date expenseDate;
        java.util.Date tempDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
        try {
            tempDate = dateFormat.parse(dateString);
        } catch (java.text.ParseException parsingError){
            tempDate = new java.util.Date();
        }
        expenseDate = tempDate;
        String successMessage = new StringBuilder().append("Saved $").append(amount).append("\n").append("to " + category).append("\n").append("for " + description).append("\n").append("on ").append(dateFormat.format(expenseDate)).toString();
        String failureMessage = new StringBuilder().append("Unable to save this expense to the database.").toString();

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
            textView.setText(failureMessage + ex.getMessage());
        }
    }
}
