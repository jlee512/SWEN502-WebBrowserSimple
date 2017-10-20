package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by julia on 19/10/2017.
 */

public class IndividualBrowserControlCenter {

    Activity activity;
    ArrayList<String> browsing_history = new ArrayList<>();
    ArrayList<Long> browsing_timestamps = new ArrayList<>();
    ArrayList<String> bookmarks = new ArrayList<>();

    IndividualBrowserControlCenter(Activity individual_browser) {
        this.activity = individual_browser;
    }

    public ArrayList<String> getBrowsing_history() {
        return browsing_history;
    }

    public ArrayList<Long> getBrowsing_timestamps() {
        return browsing_timestamps;
    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    // This method encapsulates the logic (backend/dynamic frontend) for the browser home screen
    void home_screen_logic() {

        // Load Shared Preferences (which will include arraylist of history and arraylist of bookmarks
//        SharedPreferences sharedPreferences = activity.getSharedPreferences("History", 0);
//        for (int i = 0; i < ; i++) {
//
//        }

        if (browsing_history != null && browsing_history.size() != 0) {
            for (int i = 0; i < browsing_history.size(); i++) {
                Log.d("test-browser-history", browsing_history.get(i));
                DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss");
                Log.d("test-browser-timestamp", "" + dateFormatter.format(new Date(browsing_timestamps.get(i))));
            }

        }

        activity.setContentView(R.layout.browser_home);

        EditText url_entry = (EditText) activity.findViewById(R.id.url_entry1);
        ImageView target_icon = (ImageView) activity.findViewById(R.id.target_icon);

        // Floating Action Buttons
        FloatingActionButton history_button = (FloatingActionButton) activity.findViewById(R.id.history_button);
        FloatingActionButton bookmarks_button = (FloatingActionButton) activity.findViewById(R.id.bookmarks_button);
        FloatingActionButton googleSearch_button = (FloatingActionButton) activity.findViewById(R.id.google_button);

        // Overall Layout Object
        View overallLayout = (View) activity.findViewById(R.id.overallLayout);

        //Setup Event Listeners
        setup_urlEntryOnClickListener(url_entry);
        setup_overallLayoutOnClickListener(overallLayout, url_entry);
        setup_urlEntryOnEditListener(url_entry);
        setup_targetIconOnClickListener(target_icon, url_entry);
        setup_googleIconOnClickListener(googleSearch_button, url_entry);

    }

    // This method encapsulates the logic (backend/dynamic frontend) for the main browser interface
    void browser_main_logic(String url_string) {
        activity.setContentView(R.layout.browser_main);

        // Get all interactive components as objects and setup event listeners
        EditText url_entry = (EditText) activity.findViewById(R.id.url_entry2);
        View overallLayout = (View) activity.findViewById(R.id.overallLayout);
        ImageView target_icon = (ImageView) activity.findViewById(R.id.target_icon);
        ImageView home = (ImageView) activity.findViewById(R.id.journey_home);
        //Floating Action Buttons
        FloatingActionButton back_button = (FloatingActionButton) activity.findViewById(R.id.back_button);
        FloatingActionButton history_button = (FloatingActionButton) activity.findViewById(R.id.history_button);
        FloatingActionButton bookmarks_button = (FloatingActionButton) activity.findViewById(R.id.bookmarks_button);
        FloatingActionButton googleSearch_button = (FloatingActionButton) activity.findViewById(R.id.google_button);
        FloatingActionButton forward_button = (FloatingActionButton) activity.findViewById(R.id.forward_button);
        FloatingActionButton menu_button = (FloatingActionButton) activity.findViewById(R.id.browser_menu_expand);

        //Setup event listeners
        setup_urlEntryOnClickListener(url_entry);
        setup_overallLayoutOnClickListener(overallLayout, url_entry);
        setup_urlEntryOnEditListener(url_entry);
        setup_homeButtonOnClickListener(home);
        setup_googleIconOnClickListener(googleSearch_button, url_entry);
        setup_targetIconOnClickListener(target_icon, url_entry);

        //Render the webview
        WebView webView = (WebView) activity.findViewById(R.id.main_webview);

        render_webViewClient(webView, url_string, url_entry);

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the popup menu styles
                Context wrapper = new ContextThemeWrapper(activity, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.browser_menu, popup.getMenu());
                popup.show();
            }
        });

    }

    boolean url_validity_check(String url_string) {
        return URLUtil.isValidUrl(url_string);
    }

    String url_formatting_check(String url_string) {

        String processed_url_string = "";

        //If https://www. or http://www. not prefixing url, modify string to smooth user experience
        if (!(url_string.startsWith("http://www.") || url_string.startsWith("https://www."))) {
            // If url ends with contains .co. || .com || .org || .govt
        }


        return "";
    }

    void setup_urlEntryOnClickListener(EditText url_entry) {
        url_entry.setHintTextColor(Color.parseColor("#057eaa"));
        //Text onclick listener to detect if text is selected
        url_entry.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
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

    void setup_overallLayoutOnClickListener(View overallLayout, EditText url_entry) {
        //Overall layout click listener to detect if anything other than text is selected
        overallLayout.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
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
        url_entry.setOnEditorActionListener(new CustomOnEditorActionListener(url_entry) {
            @Override
            public boolean onEditorAction(TextView url_entry, int actionId, KeyEvent event) {
                String url_string = url_entry.getText().toString();
                if (url_validity_check(url_string)) {
                    hideKeyboard(activity);
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
                    EditText url_failure = (EditText) activity.findViewById(R.id.url_entry1);
                    url_failure.setText(url_string);
                    hideKeyboard(activity);
                    return false;
                }
            }
        });
    }

    void setup_targetIconOnClickListener(ImageView target_icon, EditText url_entry) {
        //This method allows the user to access the url they've entered by clicking the target icon
        target_icon.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
                String url_string = url_entry.getText().toString();
                if (url_validity_check(url_string)) {
                    hideKeyboard(activity);
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
                    EditText url_failure = (EditText) activity.findViewById(R.id.url_entry1);
                    url_failure.setText(url_string);
                    hideKeyboard(activity);
                }
            }
        });
    }

    void setup_googleIconOnClickListener(FloatingActionButton googleSearch_button, EditText url_entry) {
        // Setup google search button event listener
        googleSearch_button.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
                String googleSearch_prefix = "https://www.google.com/search?q=";
                String search_string = url_entry.getText().toString();

                String google_url = googleSearch_prefix + search_string;

                browser_main_logic(google_url);
            }
        });
    }

    void render_webViewClient(WebView webView, String url, EditText url_entry) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient(url_entry) {

            // This method populates the url_entry EditText with any navigation by the user in the browser
            // Note: deprecated method user for broadest compatibilty with devices
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Capture timestamp and url to store in history list
                addHistoryItem(url);
                url_entry.setText(url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
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
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry1);
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
                WebView webview = (WebView) activity.findViewById(R.id.main_webview);
                webview.stopLoading();
                home_screen_logic();
                EditText url_entry = (EditText) activity.findViewById(R.id.url_entry1);
                url_entry.setText("");
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void addHistoryItem(String url) {
        Date current_datetime = new Date();
        browsing_history.add(url);
        browsing_timestamps.add(current_datetime.getTime());
    }

    void addBookmark(String url) {
        bookmarks.add(url);
    }
}
