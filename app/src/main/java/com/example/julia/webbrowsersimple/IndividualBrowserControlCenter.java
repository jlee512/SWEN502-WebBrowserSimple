package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by julia on 19/10/2017.
 */

public class IndividualBrowserControlCenter {

    Activity activity;

    IndividualBrowserControlCenter(Activity individual_browser) {
        this.activity = individual_browser;
    }


    // This method encapsulates the logic (backend/dynamic frontend) for the browser home screen
    void home_screen_logic() {
        activity.setContentView(R.layout.browser_home);

        EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
        ImageView target_icon = (ImageView) activity.findViewById(R.id.target_icon);

        // Floating Action Buttons
        FloatingActionButton history_button = (FloatingActionButton) activity.findViewById(R.id.history_button);
        FloatingActionButton bookmarks_button = (FloatingActionButton) activity.findViewById(R.id.bookmarks_button);
        FloatingActionButton googleSearch_button = (FloatingActionButton) activity.findViewById(R.id.google_button);

        // Overall Layout Object
        View overallLayout = (View) activity.findViewById(R.id.overallLayout);

        //Setup Event Listeners
        setup_urlEntryOnClickListener(url_entry);
        setup_overallLayoutOnClickListener(overallLayout);
        setup_urlEntryOnEditListener(url_entry);
        setup_targetIconOnClickListener(target_icon);
        setup_googleIconOnClickListener(googleSearch_button);

    }

    // This method encapsulates the logic (backend/dynamic frontend) for the main browser interface
    void browser_main_logic(String url_string) {
        activity.setContentView(R.layout.browser_main);

        // Get all interactive components as objects and setup event listeners
        EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
        View overallLayout = (View) activity.findViewById(R.id.overallLayout);
        ImageView target_icon = (ImageView) activity.findViewById(R.id.target_icon);
        ImageView home = (ImageView) activity.findViewById(R.id.journey_home);
        //Floating Action Buttons
        FloatingActionButton back_button = (FloatingActionButton) activity.findViewById(R.id.back_button);
        FloatingActionButton history_button = (FloatingActionButton) activity.findViewById(R.id.history_button);
        FloatingActionButton bookmarks_button = (FloatingActionButton) activity.findViewById(R.id.bookmarks_button);
        FloatingActionButton googleSearch_button = (FloatingActionButton) activity.findViewById(R.id.google_button);
        FloatingActionButton forward_button = (FloatingActionButton) activity.findViewById(R.id.forward_button);

        //Setup event listeners
        setup_urlEntryOnClickListener(url_entry);
        setup_overallLayoutOnClickListener(overallLayout);
        setup_urlEntryOnEditListener(url_entry);
        setup_homeButtonOnClickListener(home);
        setup_googleIconOnClickListener(googleSearch_button);
        setup_targetIconOnClickListener(target_icon);

        //Render the webview
        WebView webView = (WebView) activity.findViewById(R.id.main_webview);

        render_webViewClient(webView, url_string);

        //Setup listener to asses whether webview has changes and update url_entry

    }

    boolean url_validity_check (String url_string) {
        return URLUtil.isValidUrl(url_string);
    }

    String url_formatting_check (String url_string) {
        return "";
    }

    void setup_urlEntryOnClickListener(EditText url_entry) {
        url_entry.setHintTextColor(Color.parseColor("#057eaa"));
        //Text onclick listener to detect if text is selected
        url_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                url_entry.setHintTextColor(activity.getResources().getColor(R.color.orangeFontColor));
                url_entry.setTextColor(activity.getResources().getColor(R.color.orangeFontColor));
                if (url_entry.getText().toString().length() > 0) {
                    url_entry.setCursorVisible(true);
                } else {
                    url_entry.setCursorVisible(false);
                }
            }
        });
    }

    void setup_overallLayoutOnClickListener(View overallLayout) {
        //Overall layout click listener to detect if anything other than text is selected
        overallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                url_entry.setHintTextColor(activity.getResources().getColor(R.color.blueFontColor));
                url_entry.setTextColor(activity.getResources().getColor(R.color.blueFontColor));
                url_entry.setCursorVisible(false);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    void setup_urlEntryOnEditListener(EditText url_entry) {
        // This method captures the 'Go' keyboard press when the url_entry field is edited
        url_entry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView url_entry, int actionId, KeyEvent event) {
                String url_string = url_entry.getText().toString();
                if (url_validity_check(url_string)) {
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    browser_main_logic(url_string);
                    return true;
                } else {
                    //Switch to home screen logic to displat error message
                    home_screen_logic();
                    TextView user_feedback1 = (TextView) activity.findViewById(R.id.user_feedback1);
                    TextView user_feedback2 = (TextView) activity.findViewById(R.id.user_feedback2);
                    user_feedback1.setText("Sorry, we couldn't find where you want to go");
                    user_feedback2.setText("Click G to search for your destination on Google");
                    url_entry.setHintTextColor(activity.getResources().getColor(R.color.blueFontColor));
                    url_entry.setTextColor(activity.getResources().getColor(R.color.blueFontColor));
                    url_entry.setCursorVisible(false);
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return false;
                }
            }
        });
    }

    void setup_targetIconOnClickListener(ImageView target_icon) {
        //This method allows the user to access the url they've entered by clicking the target icon
        target_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                String url_string = url_entry.getText().toString();
                if (url_validity_check(url_string)) {
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    browser_main_logic(url_string);
                } else {
                    //Switch to home screen logic to display error message
                    home_screen_logic();
                    TextView user_feedback1 = (TextView) activity.findViewById(R.id.user_feedback1);
                    TextView user_feedback2 = (TextView) activity.findViewById(R.id.user_feedback2);
                    user_feedback1.setText("Sorry, we couldn't find where you want to go");
                    user_feedback2.setText("Click G to search for your destination on Google");
                    url_entry.setHintTextColor(activity.getResources().getColor(R.color.blueFontColor));
                    url_entry.setTextColor(activity.getResources().getColor(R.color.blueFontColor));
                    url_entry.setCursorVisible(false);
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    void setup_googleIconOnClickListener(FloatingActionButton googleSearch_button) {
        // Setup google search button event listener
        googleSearch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String googleSearch_prefix = "https://www.google.com/search?q=";
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                String search_string = url_entry.getText().toString();

                String google_url = googleSearch_prefix + search_string;

                browser_main_logic(google_url);
            }
        });
    }

    void render_webViewClient(WebView webView, String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            // This method populates the url_entry EditText with any navigation by the user in the browser
            // Note: deprecated method user for broadest compatibilty with devices
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                url_entry.setText(url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                url_entry.setText(url);
            }

            // This method redirects the user to the home screen with an error message if a url does have a valid web resource
            // Note: deprecated method user for broadest compatibilty with devices
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                home_screen_logic();
                TextView user_feedback1 = (TextView) activity.findViewById(R.id.user_feedback1);
                TextView user_feedback2 = (TextView) activity.findViewById(R.id.user_feedback2);
                user_feedback1.setText("Sorry, there is not a webpage at your destination");
                user_feedback2.setText("Click G to search your destination on Google");
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry);
                url_entry.setText(failingUrl);

            }
        });
        webView.loadUrl(url);
    }

    void setup_homeButtonOnClickListener(ImageView home) {
        // Setup event listeners for buttons to carry out corresponding actions
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_screen_logic();
            }
        });
    }
}
