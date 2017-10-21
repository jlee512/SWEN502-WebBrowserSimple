package com.example.julia.webbrowsersimple;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by julia on 21/10/2017.
 */

public class HistoryViewCustomAdapter extends BaseAdapter {

    //The list of HistoryItems stored (includes url and timestamp)
    private List<HistoryItem> history_items;

    //An object that can create/inflate views using the context/activity provided to the constructor
    private LayoutInflater inflater;

    public HistoryViewCustomAdapter(Activity activity, List<HistoryItem> history_items) {
        super();
        this.history_items = history_items;
        this.inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    //Abstract methods used by parent
    @Override
    public int getCount() {
        return history_items.size();
    }

    @Override
    public Object getItem(int position) {
        return history_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView==null) {
            //No previous view, instantiate viewHolder, inflate the view and setup view items
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.history_item_layout, null);

            viewHolder.historyItemButton = (ImageView) convertView.findViewById(R.id.historyItem_button);
            viewHolder.historyItem_text = (TextView) convertView.findViewById(R.id.historyItem_text);
            viewHolder.historyItem_timestamp = (TextView) convertView.findViewById(R.id.historyItemTime_text);

            //Store the holder inside the view
            convertView.setTag(viewHolder);
        } else {
            //Get viewHolder to prepare for update with new data
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Update views
        HistoryItem historyItem = (HistoryItem) history_items.get(position);
        //Get the historyItem url
        viewHolder.historyItem_text.setText(historyItem.getHistory_url());

        //Get the historyItem timestamp and format into date string (if exists)
        if (historyItem.getHistory_timestamp() == -1 ) {
            viewHolder.historyItem_timestamp.setText(". . .");
        } else {
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss");
            viewHolder.historyItem_timestamp.setText("Visited: " + dateFormatter.format(new Date(historyItem.getHistory_timestamp())));
        }
        return convertView;
    }

    //Inner class to store element tags in getView function
    public static class ViewHolder {
        ImageView historyItemButton;
        TextView historyItem_text;
        TextView historyItem_timestamp;
    }
}
