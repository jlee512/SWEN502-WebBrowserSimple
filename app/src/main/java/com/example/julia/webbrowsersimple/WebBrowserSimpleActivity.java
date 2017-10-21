package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class WebBrowserSimpleActivity extends AppCompatActivity {

    IndividualBrowserControlCenter browserInstance;
    WebView webView;
    HistoryViewCustomAdapter historyViewAdapter;
    BookmarksViewCustomAdapter bookmarksViewAdapter;

    //----------Stored even when user closes application (SharedPreferences) i.e. persistent data------------------------------------
    //To be stored in shared preferences (for persistence)
    //History
    List<HistoryItem> browsing_history = new ArrayList<>();
    //Bookmarks
    List<String> bookmarks = new ArrayList<>();
    //Settings
    //Set session Te Reo homepage and store history options with defaults (to be overwritten by shared preferences if stored
    boolean te_reo_homepage = false;
    boolean store_history = true;
    //--------------------------------------------------------------------------------------------------------------------------------
    

    //-----------------Stored only if user does not close application (savedInstanceState)--------------------------------------------
    //Session history to be shifted to WebBrowserSimpleAcitivity class as a global variable (which will be restarted with each application open/close)
    // Includes: session history (array), layout_state when destroyed and current page (in order to accurately restore)
    List<String> session_history;
    int layout_state = R.layout.browser_home;
    int current_page;
    //--------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            layout_state = savedInstanceState.getInt("layoutId", R.layout.browser_home);
        }

        //Setup session_history variable (load from savedInstanceState if available
        session_history = new ArrayList<>();
        if (savedInstanceState != null  && savedInstanceState.containsKey("sessionHistory")) {
            session_history = savedInstanceState.getStringArrayList("sessionHistory");
        }

        //Setup current_page variable (load from savedInstanceState if available
        current_page = 0;
        if (savedInstanceState != null && savedInstanceState.containsKey("current_page")) {
            current_page = savedInstanceState.getInt("current_page");
        }

        browserInstance = new IndividualBrowserControlCenter(this, webView, historyViewAdapter, bookmarksViewAdapter, browsing_history, bookmarks, te_reo_homepage, store_history, current_page, session_history, layout_state);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("layoutId", browserInstance.getLayoutState());
        outState.putInt("current_page", browserInstance.getCurrent_page());
        outState.putStringArrayList("sessionHistory", new ArrayList<String>(browserInstance.getSession_history()));
        super.onSaveInstanceState(outState);
    }
}
