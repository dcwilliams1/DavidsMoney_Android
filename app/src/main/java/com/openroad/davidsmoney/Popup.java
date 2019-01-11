package com.openroad.davidsmoney;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Popup {
    public static void ShowMessageWindow(Context context, View view, String message){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View messageWindow = inflater.inflate(R.layout.message_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow messagePopup = new PopupWindow(messageWindow, width, height, focusable);
        TextView messageDisplay = messageWindow.findViewById(R.id.message_display_view);
        messageDisplay.setText(message);
        messagePopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        messageWindow.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){
                messagePopup.dismiss();
                return true;
            }
        });
    }
}
