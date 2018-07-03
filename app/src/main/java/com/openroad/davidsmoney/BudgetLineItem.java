package com.openroad.davidsmoney;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity
public class BudgetLineItem {
    @ColumnInfo(name="Description")
    private String _description;

    @ColumnInfo(name="Amount")
    private Number _amount;

    @ColumnInfo(name="Category")
    private String _category;

    @ColumnInfo(name="Date")
    private Date  _date;

}
