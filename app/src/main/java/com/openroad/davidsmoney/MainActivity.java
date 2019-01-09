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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView simpleList;
    String budgetCategories[] = {"Big Toys","Clothes","Dakshina" ,"Entertainment", "Food","Home Maintenance","Staples"};
    final Calendar dateCalendar = Calendar.getInstance();
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
        editDate.requestFocus();
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
                Intent dataListIntent = new Intent(this, DataList.class);
                item.setIntent(dataListIntent);
                startActivityForResult(dataListIntent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        intent.putExtras(dataBundle);


        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getApplicationContext(), budgetCategories[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        String budgetDateFormat = "MM/dd/yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(budgetDateFormat, Locale.US);
        editDate.setText(sdf.format(dateCalendar.getTime()));
    }
}
