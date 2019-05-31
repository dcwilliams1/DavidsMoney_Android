package com.openroad.davidsmoney.ui.expense

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.*

import com.openroad.davidsmoney.R
import com.openroad.davidsmoney.ui.expenselist.ExpenseList

import java.text.SimpleDateFormat
import java.util.*

import kotlinx.android.synthetic.main.expense_detail.*
import kotlinx.android.synthetic.main.fragment_edit_expense.*
import kotlinx.android.synthetic.main.category_spinner.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

public class EditExpenseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var budgetCategories: List<String>? = null
    private val dateCalendar = Calendar.getInstance()
    private var pageView: View? = null
    private var pageViews: ArrayList<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pageViews = include?.getFocusables(View.FOCUS_FORWARD)

        val resources = resources

        editCategory.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })

        budgetCategories = resources.getStringArray(R.array.budget_categories).toList();
        val categoryArrayAdapter: ArrayAdapter<String> = ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, budgetCategories)
        editCategory.setAdapter(categoryArrayAdapter)

        editDate?.setOnClickListener( { DatePickerDialog(getActivity(), date, dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DAY_OF_MONTH)).show() })

        saveButton.setOnClickListener( { SaveData() })

        pageViews?.forEach() {
            if (pageView is EditText) {
                (pageView as EditText).onFocusChangeListener = View.OnFocusChangeListener { textView, hasFocus ->
                    if (!hasFocus && TextIsValid(textView as EditText)) {
                        (textView as TextView).error = null
                    }
                }
            }
        }

        if (InEditMode()) {
            this.PopulateData(getActivity()!!.getIntent())
            val cancelButton = cancelButton
            cancelButton.setVisibility(View.VISIBLE)
            cancelButton.setOnClickListener(View.OnClickListener { ShowBudgetItemList() })
            saveButton.setText(R.string.update_button_label)
        }
        editDate!!.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.data_list -> {
                ShowBudgetItemList()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    private fun PopulateData(intent: Intent) {

        editAmount.setText(java.lang.Long.toString(intent.getLongExtra(this.getString(R.string.amount_property), 0)))
        editDescription.setText(intent.getStringExtra(this.getString(R.string.description_property)))
        val categoryAdapter = editCategory.getAdapter() as ArrayAdapter<String>
        editCategory.setSelection(categoryAdapter.getPosition(intent.getStringExtra(this.getString(R.string.category_property))))
        val itemDate = java.util.Date(intent.getLongExtra(this.getString(R.string.date_property), 0))
        editDate!!.setText(SimpleDateFormat(this.getString(R.string.DEFAULT_DATE_FORMAT), Locale.getDefault()).format(itemDate))
        dateCalendar.time = itemDate
    }

    private fun SaveData() {
        if (DataIsValid()) {
            val intent = Intent(getActivity(), SaveExpenseActivity::class.java)
            val dataBundle = Bundle()
            dataBundle.putString(this.getString(R.string.amount_property), editAmount.getText().toString())
            dataBundle.putString(this.getString(R.string.description_property), editDescription.getText().toString())
            dataBundle.putString(this.getString(R.string.category_property), editCategory.getSelectedItem().toString())
            dataBundle.putString(this.getString(R.string.date_property), editDate!!.getText().toString())
            if (InEditMode()) {
                dataBundle.putInt(this.getString(R.string.line_item_id_property), getActivity()!!.getIntent().getIntExtra(this.getString(R.string.line_item_id_property), 0))
                dataBundle.putBoolean(this.getString(R.string.in_edit_mode), true)
            }
            intent.putExtras(dataBundle)

            startActivity(intent)
        }
    }

    private val date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
        dateCalendar.set(Calendar.YEAR, year)
        dateCalendar.set(Calendar.MONTH, month)
        dateCalendar.set(Calendar.DAY_OF_MONTH, day)
        updateLabel()
    }

    private fun updateLabel() {
        val budgetDateFormat = this.getString(R.string.DEFAULT_DATE_FORMAT)
        val sdf = SimpleDateFormat(budgetDateFormat, Locale.US)
        editDate?.setText(sdf.format(dateCalendar.time))
    }

    private fun InEditMode(): Boolean {
        var returnValue = false
        var intent = getActivity()?.getIntent()
        val action = if (intent != null) intent.action else null
        if (action != null) {
            returnValue = action == Intent.ACTION_EDIT
        }
        return returnValue
    }

    private fun ShowBudgetItemList() {
        val dataListIntent = Intent(getActivity(), ExpenseList::class.java)
        dataListIntent.setAction(Intent.ACTION_VIEW)
        startActivityForResult(dataListIntent, 0)
    }

    private fun DataIsValid(): Boolean {
        var returnValue = true
        pageViews?.forEach() {
            if (it is EditText) {
                if (!TextIsValid(it)) {
                    (it).error = getString(R.string.required_field_error_message)
                    returnValue = false
                }
            }
        }
        return returnValue
    }

    private fun TextIsValid(textView: EditText): Boolean {
        val len = textView.text.length
        return textView.text.length > 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpenseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                EditExpenseFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
