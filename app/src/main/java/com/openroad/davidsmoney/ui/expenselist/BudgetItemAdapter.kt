package com.openroad.davidsmoney.ui.expenselist

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.openroad.davidsmoney.R
import com.openroad.davidsmoney.database.BudgetLineItem
import com.openroad.davidsmoney.ui.MainActivity
import com.openroad.davidsmoney.ui.common.Converters
import java.text.SimpleDateFormat
import java.util.*

class BudgetItemAdapter(context: FragmentActivity?, BudgetItemDataSet: List<BudgetLineItem>?, dataListRecycler: RecyclerView) : RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder>() {
    private var budgetItemDataset: List<BudgetLineItem>? = BudgetItemDataSet
    private val appContext = context
    private val dataListRecycler = dataListRecycler

    inner class BudgetItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal val budgetItemDescriptionView: TextView
        internal val budgetItemDateView: TextView
        internal var budgetItemCategoryView: TextView
        internal var budgetItemAmountView: TextView

        init {
            budgetItemDescriptionView = itemView.findViewById(R.id.item_description_view)
            budgetItemDateView = itemView.findViewById(R.id.item_date_view)
            budgetItemCategoryView = itemView.findViewById(R.id.item_category_view)
            budgetItemAmountView = itemView.findViewById(R.id.item_amount_view)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val itemPosition = dataListRecycler.getChildLayoutPosition(view)
            val item = budgetItemDataset!![itemPosition]
            EditBudgetLineItem(item, view)
        }

        internal fun EditBudgetLineItem(item: BudgetLineItem, view: View) {
            val intent = Intent(view.context, ExpenseListFragment::class.java)
            intent.action = Intent.ACTION_EDIT
            val dataBundle = Bundle().apply {
                putLong(appContext!!.getString(R.string.amount_property), item.amount!!)
                putString(appContext!!.getString(R.string.description_property), item.description)
                putString(appContext!!.getString(R.string.category_property), item.category)
                putLong(appContext!!.getString(R.string.date_property), Converters.dateToTimestamp(item.date)!!)
                putInt(appContext!!.getString(R.string.line_item_id_property), item.lineItemId!!)
                putBoolean(appContext!!.getString(R.string.in_edit_mode), true)
            }

            intent.putExtras(dataBundle)

            startActivity(appContext.con)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetItemAdapter.BudgetItemViewHolder {
        val budgetItemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_layout, parent, false)

        return BudgetItemViewHolder(budgetItemView)
    }

    override fun onBindViewHolder(viewHolder: BudgetItemViewHolder, position: Int) {
        val amountView = viewHolder.budgetItemAmountView
        amountView.text = appContext.getString(R.string.dollar_currency_amount, budgetItemDataset?.get(position)?.amount!!.toString())
        val descriptionView = viewHolder.budgetItemDescriptionView
        descriptionView.text = budgetItemDataset?.get(position)?.description
        val dateView = viewHolder.budgetItemDateView
        val complexDate = budgetItemDataset?.get(position)?.date
        dateView.text = SimpleDateFormat(appContext.getString(R.string.DEFAULT_DATE_FORMAT), Locale.getDefault()).format(complexDate)
        val categoryView = viewHolder.budgetItemCategoryView
        categoryView.text = budgetItemDataset.get(position).category
    }

    override fun getItemCount(): Int {
        return if (budgetItemDataset != null) {
            budgetItemDataset.size
        } else {
            0
        }
    }

    fun setData(data: List<BudgetLineItem>) {
        this.budgetItemDataset = data
    }
}

