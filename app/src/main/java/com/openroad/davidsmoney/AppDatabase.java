package com.openroad.davidsmoney;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {BudgetLineItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LineItemDao userDao();
}