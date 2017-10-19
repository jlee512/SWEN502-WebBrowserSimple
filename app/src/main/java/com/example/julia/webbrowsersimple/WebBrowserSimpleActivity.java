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

        IndividualBrowserControlCenter browserInstance = new IndividualBrowserControlCenter(this);
        browserInstance.home_screen_logic();

    }
}
