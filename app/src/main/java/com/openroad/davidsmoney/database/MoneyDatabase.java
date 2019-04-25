package com.openroad.davidsmoney.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.openroad.davidsmoney.ui.common.Converters;

@Database(entities = {BudgetLineItem.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class MoneyDatabase extends RoomDatabase {
    public abstract LineItemDao userDao();

    private static MoneyDatabase INSTANCE;

    public static MoneyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MoneyDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MoneyDatabase.class, "money_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}