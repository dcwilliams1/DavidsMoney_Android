package com.openroad.davidsmoney;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {BudgetLineItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MoneyDatabase extends RoomDatabase {
    public abstract LineItemDao userDao();

    private static MoneyDatabase INSTANCE;

    public static MoneyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MoneyDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MoneyDatabase.class, "money_database").build();
                }
            }
        }
        return INSTANCE;
    }
}