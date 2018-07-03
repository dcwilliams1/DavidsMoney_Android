package com.openroad.davidsmoney;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
    public interface LineItemDao {
    @Query("SELECT * FROM BudgetLineItem")
    List<BudgetLineItem> getAll();

    @Query("SELECT * FROM BudgetLineItem WHERE ID = (:lineItemID)")
    List<BudgetLineItem> loadAllByIds(int[] lineItemID);

    @Query("SELECT * FROM BudgetLineItem WHERE Description LIKE :description LIMIT 1")
    BudgetLineItem findByName(String first, String last);

    @Insert
    void insertAll(BudgetLineItem... lineItem);

    @Delete
    void delete(BudgetLineItem lineItem);

}
