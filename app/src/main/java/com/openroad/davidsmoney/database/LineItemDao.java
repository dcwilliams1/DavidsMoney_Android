package com.openroad.davidsmoney.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openroad.davidsmoney.database.BudgetLineItem;

import java.util.List;

@Dao
    public interface LineItemDao {
    @Query("SELECT * FROM BudgetLineItem")
    LiveData<List<BudgetLineItem>> getAll();

    @Query("SELECT * FROM BudgetLineItem WHERE LineItemId = (:lineItemID)")
    List<BudgetLineItem> loadAllByIds(int[] lineItemID);

    @Query("SELECT * FROM BudgetLineItem WHERE Description LIKE :description LIMIT 1")
    BudgetLineItem findByDescription(String description);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insertBudgetLineItem(BudgetLineItem item);

    @Insert
    void insertAll(List<BudgetLineItem> lineItems);

    @Update
    void updateBudgetLineItem(BudgetLineItem item);

    @Delete
    void delete(BudgetLineItem lineItem);

    @Delete
    void deleteAll(List<BudgetLineItem> lineItems);

}
