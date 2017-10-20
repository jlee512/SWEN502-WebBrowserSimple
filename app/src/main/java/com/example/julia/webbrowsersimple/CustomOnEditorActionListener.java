package com.example.julia.webbrowsersimple;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by julia on 20/10/2017.
 */

public class CustomOnEditorActionListener implements TextView.OnEditorActionListener {

    EditText url_entry;

    CustomOnEditorActionListener(EditText url_entry) {
        super();
        this.url_entry = url_entry;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
}
