package com.openroad.davidsmoney;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SaveDataActivity extends AppCompatActivity {
    MoneyDatabase db = Room.databaseBuilder(getApplicationContext(),
            MoneyDatabase.class, "money_database").build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);
        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        String amount = intent.getStringExtra("amount");
        String description = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");
        String date = intent.getStringExtra("date");
        String message = intent.getStringExtra(MainActivity.DATA_SAVED_CONFIRMATION);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView2);
        textView.setText(message);
    }


}
