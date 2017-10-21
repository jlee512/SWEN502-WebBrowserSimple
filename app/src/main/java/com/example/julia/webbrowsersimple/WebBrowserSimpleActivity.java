package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        // -------------------Pull sharedInstanceState variables that are available--------------------
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", 0);
        //De-serialize history and bookmarks lists to json strings
        Gson gson = new Gson();
        String jsonHistory = sharedPreferences.getString("browsing_history", null);
        String jsonBookmarks = sharedPreferences.getString("bookmarks", null);
        if (jsonHistory != null) {
            Type type = new TypeToken<List<HistoryItem>>(){}.getType();
            browsing_history = gson.fromJson(jsonHistory, type);
        }

        if (jsonBookmarks != null) {
            Type type = new TypeToken<List<String>>(){}.getType();
            bookmarks = gson.fromJson(jsonBookmarks, type);
        }

        //Get Te Reo homepage and store_history settings from shared preferences
        te_reo_homepage = sharedPreferences.getBoolean("te_reo_homepage", false);
        store_history = sharedPreferences.getBoolean("store_history", true);


        // -------------------Pull savedInstanceState variables that are available---------------------
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

        // ---------------------------------------------------------------------------------------------

        browserInstance = new IndividualBrowserControlCenter(this, webView, historyViewAdapter, bookmarksViewAdapter, browsing_history, bookmarks, te_reo_homepage, store_history, current_page, session_history, layout_state);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("layoutId", browserInstance.getLayoutState());
        outState.putInt("current_page", browserInstance.getCurrent_page());
        outState.putStringArrayList("sessionHistory", new ArrayList<String>(browserInstance.getSession_history()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Store browsing history, bookmarks and settings in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", 0);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        //Serialize history and bookmarks lists to json strings
        Gson gson = new Gson();
        String jsonHistory = gson.toJson(browsing_history);
        String jsonBookmarks = gson.toJson(bookmarks);
        preferencesEditor.putString("browsing_history", jsonHistory);
        preferencesEditor.putString("bookmarks", jsonBookmarks);

        //Add Te Reo homepage and store_history settings to shared preferences
        preferencesEditor.putBoolean("te_reo_homepage", browserInstance.isTe_reo_homepage());
        preferencesEditor.putBoolean("store_history", browserInstance.isStore_history());

        //Commit the shared preferences
        preferencesEditor.commit();
    }
}
