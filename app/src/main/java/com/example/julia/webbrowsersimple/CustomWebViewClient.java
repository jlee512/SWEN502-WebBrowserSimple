package com.example.julia.webbrowsersimple;

import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * Created by julia on 20/10/2017.
 */

public class CustomWebViewClient extends WebViewClient {

    EditText url_entry;

    CustomWebViewClient(EditText url_entry) {
        super();
        this.url_entry = url_entry;
    }

}
