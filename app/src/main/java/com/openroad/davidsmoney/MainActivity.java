package com.openroad.davidsmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String DATA_SAVED_CONFIRMATION = "com.openroad.davidsmoney.DATA_SAVED_CONFIRMATION";
    ListView simpleList;
    String budgetCategories[] = {"Big Toys","Clothes","Dakshina" ,"Entertainment", "Food","Home Maintenance","Staples"};

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        String message = new StringBuilder().append("Saved $").append(editAmount.getText().toString()).append(" to\n").append(editCategory.getSelectedItem().toString()).append(" - \n").append(editDescription.getText().toString()).toString();
        intent.putExtra(DATA_SAVED_CONFIRMATION, message);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getApplicationContext(), budgetCategories[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
