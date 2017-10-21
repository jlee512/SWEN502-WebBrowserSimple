package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by julia on 22/10/2017.
 */

public class BookmarksViewCustomAdapter extends BaseAdapter {

    //The list of bookmarks stored (as url strings)
    private List<String> bookmarks;

    //An object that can create/inflate views using the context/activity provided to the constructor
    private LayoutInflater inflater;

    public BookmarksViewCustomAdapter(Activity activity, List<String> bookmarks) {
        super();
        this.bookmarks = bookmarks;
        this.inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    //Abstract methods used by parent
    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public Object getItem(int position) {
        return bookmarks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderB viewHolderB;

        if (convertView == null) {
            //No previous view, instatiate viewHolderB, inflate the view and setup view items
            viewHolderB = new ViewHolderB();
            convertView = inflater.inflate(R.layout.bookmark_item_layout, null);

            viewHolderB.bookmarksItemButton = (ImageView) convertView.findViewById(R.id.bookmarkItem_button);
            viewHolderB.bookmarksItem_text = (TextView) convertView.findViewById(R.id.bookmarkItem_text);

            //Store the holder inside the view
            convertView.setTag(viewHolderB);
        } else {
            //Get viewHolder to prepare for update with new data
            viewHolderB = (ViewHolderB) convertView.getTag();
        }

        //Update views
        //Get bookmark_url url
        String bookmark_url = bookmarks.get(position);
        viewHolderB.bookmarksItem_text.setText(bookmark_url);

        return convertView;
    }

    //Inner class to store element tags in getView function
    public static class ViewHolderB {
        ImageView bookmarksItemButton;
        TextView bookmarksItem_text;
    }
}
