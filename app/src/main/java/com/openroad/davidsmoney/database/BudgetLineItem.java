package com.openroad.davidsmoney.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "BudgetLineItem")
public class BudgetLineItem {
    public static final String TABLE_NAME = "BudgetLineItem";

    @ColumnInfo(name="LineItemId")
    @PrimaryKey (autoGenerate = true)
    private Integer _lineItemId;

    @ColumnInfo(name="Description")
    private String _description;

    @ColumnInfo(name="Amount")
    private Long _amount;

    @ColumnInfo(name="Category")
    private String _category;

    @ColumnInfo(name="Date")
    private Date  _date;

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public Integer getLineItemId() {
        return _lineItemId;
    }

    public void setLineItemId(Integer lineItemId) {
        this._lineItemId = lineItemId;
    }

    public String getCategory() {
        return _category;
    }

    public void setCategory(String category){
        this._category = category;
    }

    public Long getAmount() {
        return _amount;
    }

    public void setAmount(Long amount){
        this._amount = amount;
    }

    public Date getDate() {
        return _date;
    }

    public void setDate(Date date){
        this._date = date;
    }
}
