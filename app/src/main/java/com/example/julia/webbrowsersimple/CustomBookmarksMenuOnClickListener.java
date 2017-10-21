package com.example.julia.webbrowsersimple;

import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

/**
 * Created by julia on 22/10/2017.
 */

public class CustomBookmarksMenuOnClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {

    String bookmarksItem;
    int position;

    CustomBookmarksMenuOnClickListener(String bookmarkItem, int position) {
        this.bookmarksItem = bookmarkItem;
        this.position = position;
    }

    //Method will be overridden
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
