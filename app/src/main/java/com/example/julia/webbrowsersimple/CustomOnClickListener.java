package com.example.julia.webbrowsersimple;

import android.view.View;
import android.widget.EditText;

/**
 * Created by julia on 20/10/2017.
 */

public class CustomOnClickListener implements View.OnClickListener {

    EditText url_entry;

    CustomOnClickListener(EditText url_entry) {
        super();
        this.url_entry = url_entry;
    }

    @Override
    public void onClick(View v) {
    }
}
