package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by julia on 19/10/2017.
 */

public class IndividualBrowserControlCenter {

    Activity activity;
    WebView webView;
    HistoryViewCustomAdapter historyViewAdapter;
    BookmarksViewCustomAdapter bookmarksViewAdapter;

    //To be stored in shared preferences (for persistence)
    //History
    List<HistoryItem> browsing_history = new ArrayList<>();
    //Bookmarks
    List<String> bookmarks = new ArrayList<>();
    //Settings
    //Set session Te Reo homepage and store history options with defaults (to be overwritten by shared preferences if stored
    boolean te_reo_homepage = false;
    boolean store_history = true;
    int current_page = 0;

    //Session history to be shifted to WebBrowserSimpleAcitivity class as a global variable (which will be restarted with each application open/close)
    List<String> session_history = new ArrayList<>();

    IndividualBrowserControlCenter(Activity individual_browser) {
        this.activity = individual_browser;
        home_screen_logic();
    }

    // Session variables - populated from shared preferences if available

    public List<HistoryItem> getBrowsing_history() {
        return browsing_history;
    }

    public List<String> getBookmarks() {
        return bookmarks;
    }

    // -------------------------- Core methods related to primary application layouts ---------------------

    // This method encapsulates the logic (backend/dynamic frontend) for the browser home screen
    void home_screen_logic() {

        // Load Shared Preferences (which will include arraylist of history and arraylist of bookmarks
//        SharedPreferences sharedPreferences = activity.getSharedPreferences("History", 0);
//        for (int i = 0; i < ; i++) {
//
//        }

        activity.setContentView(R.layout.browser_home);

        //Apply Te Reo logo depending on setting
        if (te_reo_homepage) {
            ImageView home_page_logo = (ImageView) activity.findViewById(R.id.home_page_logo);
            home_page_logo.setImageResource(R.drawable.te_haerenga_logo);
        }

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
        setup_viewHistoryOnClickListener(history_button);
        setup_viewBookmarksOnClickListener(bookmarks_button);
        setup_googleIconOnClickListener(googleSearch_button, url_entry);

    }

    // This method encapsulates the logic (backend/dynamic frontend) for the main browser interface
    void browser_main_logic(String url_string) {
        activity.setContentView(R.layout.browser_main);

        //Apply Te Reo logo depending on setting
        if (te_reo_homepage) {
            ImageView home_page_logo = (ImageView) activity.findViewById(R.id.journey_home);
            home_page_logo.setImageResource(R.drawable.te_haerenga_logo);
        }

        // Get all interactive components as objects and setup event listeners
        EditText url_entry = (EditText) activity.findViewById(R.id.url_entry2);
        View overallLayout = activity.findViewById(R.id.overallLayout);
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
        setup_backOnClickListener(back_button, url_entry);
        setup_forwardOnClickListener(forward_button, url_entry);
        setup_viewHistoryOnClickListener(history_button);
        setup_viewBookmarksOnClickListener(bookmarks_button);
        setup_googleIconOnClickListener(googleSearch_button, url_entry);
        setup_targetIconOnClickListener(target_icon, url_entry);

        //Render the webview
        webView = (WebView) activity.findViewById(R.id.main_webview);

        render_webViewClient(webView, url_string, url_entry);

        setup_expandableMenu(menu_button);

    }

    void viewHistory_main_logic() {
        activity.setContentView(R.layout.history_main);

        //Setup buttons and listeners (other than the item on lick listener
        Button clear_history_button = (Button) activity.findViewById(R.id.clear_history_from_list);
        setup_clearHistoryOnClickListener(clear_history_button);
        Button go_back_from_history = (Button) activity.findViewById(R.id.back_to_browser_button);
        setup_goBackFromSpecialScreenOnClickListener(go_back_from_history);


        //Get the listview object
        ListView history = (ListView) activity.findViewById(R.id.history_list);

        if (browsing_history.size() < 1) {
            List<HistoryItem> blank_history = new ArrayList<>();
            blank_history.add(new HistoryItem("Looks like there's nothing here yet", -1));
            historyViewAdapter = new HistoryViewCustomAdapter(activity, blank_history);
        } else {
            historyViewAdapter = new HistoryViewCustomAdapter(activity, browsing_history);
        }

        history.setAdapter(historyViewAdapter);

        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem historyItem = (HistoryItem) parent.getItemAtPosition(position);

                // Add the selected historyItem to the history (a new entry to accurately represent the user's navigation)
                addHistoryItem(historyItem.getHistory_url());
                current_page = 0;
                browser_main_logic(historyItem.getHistory_url());
            }
        });

    }

    void viewBookmarks_main_logic() {
        activity.setContentView(R.layout.bookmarks_main);

        //Setup buttons and listeners (other than the item on lick listener
        Button clear_bookmarks_button = (Button) activity.findViewById(R.id.clear_bookmarks_from_list);
        setup_clearBookmarksOnClickListener(clear_bookmarks_button);
        Button go_back_from_bookmarks = (Button) activity.findViewById(R.id.back_to_browser_from_bookmarks_button);
        setup_goBackFromSpecialScreenOnClickListener(go_back_from_bookmarks);

        //Get the listview object
        final ListView bookmarks_list = (ListView) activity.findViewById(R.id.bookmarks_list);

        if (bookmarks.size() < 1) {
            List<String> blank_bookmarks = new ArrayList<>();
            blank_bookmarks.add("Looks like there's nothing here yet");
            bookmarksViewAdapter = new BookmarksViewCustomAdapter(activity, blank_bookmarks);
        } else {
            bookmarksViewAdapter = new BookmarksViewCustomAdapter(activity, bookmarks);
        }

        bookmarks_list.setAdapter(bookmarksViewAdapter);

        bookmarks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookmarksItem = (String) parent.getItemAtPosition(position);

                //Initialise popup menu
                Context wrapper = new ContextThemeWrapper(activity, R.style.PopupMenu);
                final PopupMenu popup = new PopupMenu(wrapper, view);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.bookmarks_options, popup.getMenu());

                popup.setOnMenuItemClickListener(new CustomBookmarksMenuOnClickListener(bookmarksItem, position) {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Option to view the bookmark web resource and return to the browser
                            case R.id.bookmark_view_popup:
                                // Add the selected historyItem to the history (a new entry to accurately represent the user's navigation)
                                addHistoryItem(bookmarksItem);
                                current_page = 0;
                                browser_main_logic(bookmarksItem);
                                return true;
                            // Option to delete the bookmark item
                            case R.id.bookmark_delete_popup:
                                // Delete the bookmark item at the position returned by the onItemClick method
                                bookmarks.remove(position);
                                viewBookmarks_main_logic();
                                return true;
                            default:
                                return false;

                        }
                    }
                });
                popup.show();
            }
        });

    }

    // -------------------------- Helper methods that are used within the core layout methods ---------------------

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

    void setup_backOnClickListener(FloatingActionButton back_button, EditText url_entry) {
        // Setup back button event listener
        back_button.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
                if (session_history.size() > 1 && current_page < (session_history.size() - 1)) {
                    current_page++;
                    browser_main_logic(session_history.get(session_history.size() - 1 - current_page));
                }
            }
        });
    }

    void setup_forwardOnClickListener(FloatingActionButton forward_button, EditText url_entry) {
        // Setup forward button event listener
        forward_button.setOnClickListener(new CustomOnClickListener(url_entry) {
            @Override
            public void onClick(View v) {
                if (session_history.size() > 1 && current_page > 0) {
                    current_page--;
                    browser_main_logic(session_history.get(session_history.size() - 1 - current_page));
                }
            }
        });
    }

    void setup_viewHistoryOnClickListener(FloatingActionButton history_button) {
        //Setup history button event listener
        history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHistory_main_logic();
            }
        });
    }

    void setup_viewBookmarksOnClickListener(FloatingActionButton bookmarks_button) {
        //Setup bookmarks button event listener
        bookmarks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBookmarks_main_logic();
            }
        });
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
                    //Switch to home screen logic to display error message
                    home_screen_logic();
                    TextView user_feedback1 = (TextView) activity.findViewById(R.id.user_feedback1);
                    TextView user_feedback2 = (TextView) activity.findViewById(R.id.user_feedback2);
                    user_feedback1.setText("Sorry, we couldn't find where you want to go");
                    user_feedback2.setText("Click G to search for your destination on Google");
                    url_entry.setHintTextColor(activity.getResources().getColor(R.color.blueFontColor));
                    url_entry.setCursorVisible(false);
                    EditText url_failure = (EditText) activity.findViewById(R.id.url_entry1);
                    url_failure.setText(url_string);
                    url_failure.setTextColor(activity.getResources().getColor(R.color.blueFontColor));
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
                    url_entry.setCursorVisible(false);
                    EditText url_failure = (EditText) activity.findViewById(R.id.url_entry1);
                    url_failure.setText(url_string);
                    url_failure.setTextColor(activity.getResources().getColor(R.color.blueFontColor));
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
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new CustomWebViewClient(url_entry) {

            // This method populates the url_entry EditText with any navigation by the user in the browser
            // Note: deprecated method user for broadest compatibilty with devices
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //When a user navigates forward by interacting with the browser,
                // (1) Reset the session history to ignore or pages ahead of this (if navigation back has occurred)
                // (2) Rest the current page to zero as there is no longer the ability to go forward
                session_history = session_history.subList(0, session_history.size() - current_page);
                current_page = 0;

                // Capture timestamp and url to store in history list
                addHistoryItem(url);

                // Update url_entry EditText and load the url
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
        if (store_history) {
            //Add the url to the session history (links with forward/back functionality)
            session_history.add(url);
            //Create a history item for use within the listview as required
            Date current_datetime = new Date();
            HistoryItem historyItemToAdd = new HistoryItem(url, current_datetime.getTime());
            browsing_history.add(historyItemToAdd);
        }
    }

    void setup_clearHistoryOnClickListener(Button clear_history_button) {
        clear_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsing_history.clear();
                viewHistory_main_logic();
            }
        });
    }

    void setup_clearBookmarksOnClickListener(Button clear_bookmarks_button) {
        clear_bookmarks_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarks.clear();
                viewBookmarks_main_logic();
            }
        });
    }

    void setup_goBackFromSpecialScreenOnClickListener(Button goBack_from_special_screen_button) {
        goBack_from_special_screen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If there is any browsing history (i.e. it hasn't been cleared) return the user to their previous page
                if (browsing_history.size() > 0) {
                    browser_main_logic(browsing_history.get(browsing_history.size() - 1 - current_page).getHistory_url());
                } else {
                    //If the browsing history is empty, redirect the user to the home screen
                    home_screen_logic();
                }
            }
        });
    }

    void addBookmark(String url) {
        bookmarks.add(url);
    }

    // Method to setup an expandable menu
    void setup_expandableMenu(FloatingActionButton menu_button) {
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the popup menu styles
                Context wrapper = new ContextThemeWrapper(activity, R.style.PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, v);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.browser_menu, popup.getMenu());

                //Set state of history storage setting and Te Reo homepage options
                MenuItem history_storage_checkable = popup.getMenu().findItem(R.id.hide_history_menu);
                MenuItem teReo_homepage_checkable = popup.getMenu().findItem(R.id.tereo_homepage_menu);
                history_storage_checkable.setChecked(store_history);
                teReo_homepage_checkable.setChecked(te_reo_homepage);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Option to return the browser home screen (second option to logo in search bar)
                            case R.id.menu_home:
                                home_screen_logic();
                                return true;
                            // Option to go back (secondary to navbar buttons)
                            case R.id.back_menu:
                                if (session_history.size() > 1 && current_page < (session_history.size() - 1)) {
                                    current_page++;
                                    browser_main_logic(session_history.get(session_history.size() - 1 - current_page));
                                }
                                return true;
                            // Option to go forward (secondary to navbar buttons)
                            case R.id.forward_menu:
                                if (session_history.size() > 1 && current_page > 0) {
                                    current_page--;
                                    browser_main_logic(session_history.get(session_history.size() - 1 - current_page));
                                }
                                return true;
                            // Option to add a bookmark to the users stored bookmarks
                            case R.id.add_bookmark_menu:
                                EditText current_url = (EditText) activity.findViewById(R.id.url_entry2);
                                String current_url_string = current_url.getText().toString();
                                bookmarks.add(current_url_string);
                                return true;
                            // TO DO
                            case R.id.view_bookmarks_menu:
                                viewBookmarks_main_logic();
                                return true;
                            // Option to clear all bookmarks from the users stored bookmarks
                            case R.id.clear_bookmarks_menu:
                                if (bookmarks != null && bookmarks.size() > 0) {
                                    bookmarks.clear();
                                }
                                return true;
                            // Option to clear all history from the users stored history
                            case R.id.clear_history_menu:
                                if (browsing_history != null && browsing_history.size() > 0) {
                                    browsing_history.clear();
                                }
                                return true;
                            // Option to view the users browsing history (secondary to navbar buttons)
                            case R.id.view_history_menu:
                                viewHistory_main_logic();
                                return true;
                            // Toggle option to not log history (i.e. incognito mode)
                            case R.id.hide_history_menu:
                                store_history = !item.isChecked();
                                item.setChecked(store_history);
                                return true;
                            // Toggle option to have all logo instances on the browser modified into the Te Reo version
                            case R.id.tereo_homepage_menu:
                                te_reo_homepage = !item.isChecked();
                                item.setChecked(te_reo_homepage);
                                return true;
                            default:
                                return false;

                        }
                    }
                });
                popup.show();
            }
        });
    }
}
