package com.openroad.davidsmoney;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView simpleList;
    String[] budgetCategories = {"Big Toys","Clothes" ,"Entertainment", "Food","Home Maintenance","Savings", "Staples", "Tithing"};
    Calendar dateCalendar = Calendar.getInstance();
    EditText editDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Spinner categoryDropdown = (Spinner) findViewById(R.id.editCategory);
        categoryDropdown.setOnItemSelectedListener(this);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, budgetCategories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropdown.setAdapter(categoryArrayAdapter);

        editDate = (EditText) findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new DatePickerDialog(MainActivity.this, date, dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Intent intent = getIntent();
        if (InEditMode()){
            this.PopulateData(intent);
            Button cancelButton = findViewById(R.id.cancelButton);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener( new View.OnClickListener(){
                public void onClick(View view){
                    ShowBudgetItemList();
                }
            });
        }
        editDate.requestFocus();
    }

    private void PopulateData(Intent intent) {
        EditText editAmount = (EditText) findViewById(R.id.editAmount);
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        Spinner editCategory = (Spinner) findViewById(R.id.editCategory);
        EditText editDate = (EditText) findViewById(R.id.editDate);

        editAmount.setText(Long.toString(intent.getLongExtra("amount", 0)));
        editDescription.setText(intent.getStringExtra("description"));
        editCategory.setSelection(((ArrayAdapter)editCategory.getAdapter()).getPosition(intent.getStringExtra("category")));
        java.util.Date itemDate = new java.util.Date(intent.getLongExtra("date", 0));
        editDate.setText(new SimpleDateFormat("M/d/yyyy", Locale.getDefault()).format(itemDate));
        dateCalendar.setTime(itemDate);
    }

    public void saveData(View view) {
        Intent intent = new Intent(this, SaveDataActivity.class);
        EditText editAmount = (EditText) findViewById(R.id.editAmount);
        EditText editDescription = (EditText) findViewById(R.id.editDescription);
        Spinner editCategory = (Spinner) findViewById(R.id.editCategory);
        EditText editDate = (EditText) findViewById(R.id.editDate);
        Bundle dataBundle = new Bundle();
        dataBundle.putString("amount", editAmount.getText().toString());
        dataBundle.putString("description", editDescription.getText().toString());
        dataBundle.putString("category", editCategory.getSelectedItem().toString());
        dataBundle.putString("date", editDate.getText().toString());
        dataBundle.putInt("lineItemId", getIntent().getIntExtra("lineItemId", 0));
        intent.putExtras(dataBundle);


        startActivity(intent);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, month);
            dateCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        }
    };

    private void updateLabel(){
        String budgetDateFormat = "M/d/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(budgetDateFormat, Locale.US);
        editDate.setText(sdf.format(dateCalendar.getTime()));
    }

    private boolean InEditMode(){
        return getIntent().getAction().equals("android.intent.action.ACTION_EDIT");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.data_list:
                ShowBudgetItemList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ShowBudgetItemList() {
        Intent dataListIntent = new Intent(this, DataList.class);
        startActivityForResult(dataListIntent, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getApplicationContext(), budgetCategories[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
