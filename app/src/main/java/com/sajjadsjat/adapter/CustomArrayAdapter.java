package com.sajjadsjat.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import com.sajjadsjat.utils.ScoreMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    private List<String> originalItems;
    private List<String> filteredItems;

    public CustomArrayAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.originalItems = items;
        this.filteredItems = items;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> filteredList;

                if (constraint == null || constraint.length() == 0) {
                    filteredList = originalItems;
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    filteredList = ScoreMatch.filter(filterPattern, originalItems);
                }

                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return Math.min(filteredItems.size(), 8);
    }

    @Override
    public String getItem(int position) {
        return filteredItems.get(position);
    }
}
