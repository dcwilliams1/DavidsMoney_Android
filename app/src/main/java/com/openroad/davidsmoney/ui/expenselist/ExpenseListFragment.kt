package com.openroad.davidsmoney.ui.expenselist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView

import com.openroad.davidsmoney.R
import com.openroad.davidsmoney.database.BudgetLineItem
import com.openroad.davidsmoney.database.MoneyDatabase
import com.openroad.davidsmoney.ui.MainActivity
import com.openroad.davidsmoney.ui.common.Popup
import kotlinx.android.synthetic.main.content_activity_list_data.*

class ExpenseListFragment : Fragment() {
    private val dataListRecycler: RecyclerView = recycler_data_list
    private var dataListAdapter: RecyclerView.Adapter<*>? = null
    private var budgetItemDataset: List<BudgetLineItem>? = null
    private var budgetLineItemObserver: Observer<List<BudgetLineItem>>? = null
    private var db: MoneyDatabase? = null

    companion object {
        fun newInstance():ExpenseListFragment = ExpenseListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var viewModel: ExpenseListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.expense_list_fragment, container, false)
    }
    //https://stackoverflow.com/questions/4930398/can-the-android-layout-folder-contain-subfolders
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
//        setContentView(R.layout.activity_list_data)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        db = MoneyDatabase.getDatabase(getActivity())
        val dataListLayoutMgr: RecyclerView.LayoutManager

         dataListRecycler?.setHasFixedSize(true)

        dataListLayoutMgr = LinearLayoutManager(getActivity())
        dataListRecycler?.setLayoutManager(dataListLayoutMgr)

        dataListAdapter = BudgetItemAdapter(activity, budgetItemDataset, dataListRecycler)

        // Create the observer which updates the UI.
        budgetLineItemObserver = object : Observer<List<BudgetLineItem>> {
            override fun onChanged(data: List<BudgetLineItem>?) {
                budgetItemDataset = data

                dataListRecycler.setAdapter(dataListAdapter)
                if (data!!.size < 1) {
                    this.toggleListView(View.GONE)
                    this.toggleEmptyDataMessage(View.VISIBLE)
                } else {
                    this.toggleListView(View.VISIBLE)
                    this.toggleEmptyDataMessage(View.GONE)
                }
            }

            private fun toggleListView(visibility: Int) {
                val dataList = dataListRecycler
                dataList?.setVisibility(visibility)
            }

            private fun toggleEmptyDataMessage(visibility: Int) {
                val dataList = findViewById<TextView>(R.id.view_empty_data_message)
                dataList.setVisibility(visibility)
            }
        }

        FetchBudgetData()
    }

    private fun FetchBudgetData() {
        val results = db.userDao().getAll()
        results.observe(this, budgetLineItemObserver)
    }

    private fun clearDatabase() {
        deleteItems(budgetItemDataset)
    }

    private fun deleteItems(itemsToDelete: List<BudgetLineItem>): Boolean {
        var success = false
        val dbThread = object : Thread() {
            override fun run() {
                db.userDao().deleteAll(itemsToDelete)
            }
        }
        dbThread.start()
        val multipleItemsDeleted = itemsToDelete.size > 1
        var returnMessage = getDeletionSuccessMessage(multipleItemsDeleted)
        try {
            dbThread.join()
            success = true
        } catch (ex: Exception) {
            returnMessage = getString(R.string.friendly_error_with_detail, getDeletionFailureMessage(multipleItemsDeleted), ex.message)
        }

        ShowMessage(returnMessage)
        return success
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExpenseListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.menu_budget_item_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_database -> {
                this.clearDatabase()
                return true
            }
            R.id.enter_data -> {
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                mainActivityIntent.setAction(Intent.ACTION_MAIN)
                item.intent = mainActivityIntent
                startActivityForResult(mainActivityIntent, 0)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun ShowMessage(message: String) {
        val context = getApplicationContext()
        val view = findViewById<View>(R.id.list_display_view)
        Popup.ShowMessageWindow(context, view, message)
    }

    private fun getDeletionSuccessMessage(multipleDeletions: Boolean): String {
        val successMessage = StringBuilder()
        if (multipleDeletions) {
            successMessage.append(this.getString(R.string.multiple_expense_deletion_success_message))
        } else {
            successMessage.append(this.getString(R.string.single_expense_deletion_success_message))
        }
        return successMessage.toString()
    }

    private fun getDeletionFailureMessage(multipleDeletions: Boolean): String {
        val failureMessage = StringBuilder()
        if (multipleDeletions) {
            failureMessage.append(this.getString(R.string.multiple_expense_deletion_failure_message))
        } else {
            failureMessage.append(this.getString(R.string.single_expense_deletion_failure_message))
        }
        return failureMessage.toString()
    }
}
