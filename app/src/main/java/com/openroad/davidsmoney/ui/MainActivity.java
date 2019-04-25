package com.openroad.davidsmoney.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.openroad.davidsmoney.R;
import com.openroad.davidsmoney.ui.budgetitem.SaveDataActivity;
import com.openroad.davidsmoney.ui.common.Popup;
import com.openroad.davidsmoney.ui.datalist.DataList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String[] budgetCategories;
    private final Calendar dateCalendar = Calendar.getInstance();
    private EditText editDate;
    private View pageView;
    private List<View> pageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setIcon(R.drawable.money_icon_png);
        pageView = findViewById(R.id.include);
        pageViews = pageView.getFocusables(View.FOCUS_FORWARD);

        Spinner categoryDropdown = findViewById(R.id.editCategory);
        categoryDropdown.setOnItemSelectedListener(this);
        Resources resources = getResources();
        budgetCategories = resources.getStringArray(R.array.budget_categories);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this, R.layout.category_spinner, budgetCategories);
        categoryDropdown.setAdapter(categoryArrayAdapter);

        editDate = findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new DatePickerDialog(MainActivity.this, date, dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                SaveData();
            }
        });

        for(View pageView : pageViews){
            if (pageView instanceof EditText) {
                ((EditText) pageView).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View textView, boolean hasFocus) {
                        if (!hasFocus && TextIsValid((EditText)textView)) {
                            ((TextView) textView).setError(null);
                        }
                    }
                });
            }
        }

        if (InEditMode()){
            this.PopulateData(getIntent());
            Button cancelButton = findViewById(R.id.cancelButton);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener( new View.OnClickListener(){
                public void onClick(View view){
                    ShowBudgetItemList();
                }
            });
            saveButton.setText(R.string.update_button_label);
        }
        editDate.requestFocus();
    }

    @Override
    protected void onStart(){
        super.onStart();
        final String message = GetMessageFromWorkflow();
        if (message != null){
            pageView.post(new Runnable() {
                public void run() {
                    View pageView = findViewById(R.id.include);
                    Popup.ShowMessageWindow(getApplicationContext(), pageView, message);
                }
            });
        }
    }

    private void PopulateData(Intent intent) {
        EditText editAmount = findViewById(R.id.editAmount);
        EditText editDescription = findViewById(R.id.editDescription);
        Spinner editCategory = findViewById(R.id.editCategory);
        EditText editDate = findViewById(R.id.editDate);

        editAmount.setText(Long.toString(intent.getLongExtra(this.getString(R.string.amount_property), 0)));
        editDescription.setText(intent.getStringExtra(this.getString(R.string.description_property)));
        ArrayAdapter<String> categoryAdapter = (ArrayAdapter<String>)editCategory.getAdapter();
        editCategory.setSelection(categoryAdapter.getPosition(intent.getStringExtra(this.getString(R.string.category_property))));
        java.util.Date itemDate = new java.util.Date(intent.getLongExtra(this.getString(R.string.date_property), 0));
        editDate.setText(new SimpleDateFormat(this.getString(R.string.DEFAULT_DATE_FORMAT), Locale.getDefault()).format(itemDate));
        dateCalendar.setTime(itemDate);
    }

    private void SaveData() {
        if (DataIsValid()) {
            EditText editAmount = findViewById(R.id.editAmount);
            EditText editDescription =  findViewById(R.id.editDescription);
            Spinner editCategory = findViewById(R.id.editCategory);
            EditText editDate =  findViewById(R.id.editDate);
            Intent intent = new Intent(this, SaveDataActivity.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putString(this.getString(R.string.amount_property), editAmount.getText().toString());
            dataBundle.putString(this.getString(R.string.description_property), editDescription.getText().toString());
            dataBundle.putString(this.getString(R.string.category_property), editCategory.getSelectedItem().toString());
            dataBundle.putString(this.getString(R.string.date_property), editDate.getText().toString());
            if (InEditMode()) {
                dataBundle.putInt(this.getString(R.string.line_item_id_property), getIntent().getIntExtra(this.getString(R.string.line_item_id_property), 0));
                dataBundle.putBoolean(this.getString(R.string.in_edit_mode), true);
            }
            intent.putExtras(dataBundle);

            startActivity(intent);
        }
    }

    private final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            dateCalendar.set(Calendar.YEAR, year);
            dateCalendar.set(Calendar.MONTH, month);
            dateCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        }
    };

    private void updateLabel(){
        String budgetDateFormat = this.getString(R.string.DEFAULT_DATE_FORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(budgetDateFormat, Locale.US);
        editDate.setText(sdf.format(dateCalendar.getTime()));
    }

    private boolean InEditMode(){
        boolean returnValue = false;
        String action = getIntent().getAction();
        if (action != null){
            returnValue = action.equals(Intent.ACTION_EDIT);
        }
        return returnValue;
    }

    private String GetMessageFromWorkflow(){
        String returnValue = null;
        String message = getIntent().getStringExtra(this.getString(R.string.message_key));
        if (message != null){
            returnValue = message;
        }
        return  returnValue;
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
        dataListIntent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(dataListIntent, 0);
    }

    private boolean DataIsValid(){
        boolean returnValue = true;
        for (View textView : pageViews){
            if (textView instanceof EditText){
                if (!TextIsValid((EditText)textView)){
                    ((EditText) textView).setError(getString(R.string.required_field_error_message));
                    returnValue = false;
                }
            }
        }
        return returnValue;
    }

    private boolean TextIsValid(EditText textView){
        int len = textView.getText().length();
        return textView.getText().length() > 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getApplicationContext(), budgetCategories[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
