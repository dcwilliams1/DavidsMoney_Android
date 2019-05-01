package com.openroad.davidsmoney.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.openroad.davidsmoney.R;
import com.openroad.davidsmoney.ui.expense.EditExpenseFragment.OnFragmentInteractionListener;
import com.openroad.davidsmoney.ui.common.Popup;
import com.openroad.davidsmoney.ui.expenselist.ExpenseList;

import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements
        OnFragmentInteractionListener
{

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
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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
        Intent dataListIntent = new Intent(this, ExpenseList.class);
        dataListIntent.setAction(Intent.ACTION_VIEW);
        startActivityForResult(dataListIntent, 0);
    }
}
