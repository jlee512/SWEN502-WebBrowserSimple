package com.example.julia.webbrowsersimple;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class WebBrowserSimpleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_home);

        EditText text = (EditText) findViewById(R.id.editText);
        text.setHintTextColor(Color.parseColor("#057eaa"));

        //Text onclick listener to detect if text is selected
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.editText);
                text.setHintTextColor(getResources().getColor(R.color.orangeFontColor));
                text.setTextColor(getResources().getColor(R.color.orangeFontColor));
                if (text.getText().length() > 0) {
                    text.setCursorVisible(true);
                } else {
                    text.setCursorVisible(false);
                }
            }
        });

        View overallLayout = (View) findViewById(R.id.overallLayout);

        //Overall layout click listener to detect if anything other than text is selected
        overallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.editText);
                text.setHintTextColor(getResources().getColor(R.color.blueFontColor));
                text.setTextColor(getResources().getColor(R.color.blueFontColor));
                text.setCursorVisible(false);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }
}
