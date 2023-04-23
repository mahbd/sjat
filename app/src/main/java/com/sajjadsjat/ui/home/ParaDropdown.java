package com.sajjadsjat.ui.home;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CustomArrayAdapter extends ArrayAdapter<String> {
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
                List<String> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(originalItems);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (String item : originalItems) {
                        if (item.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
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
        return filteredItems.size();
    }

    @Override
    public String getItem(int position) {
        return filteredItems.get(position);
    }
}


public class ParaDropdown {
    Context context;
    CustomArrayAdapter adapter;
    int layout;
    AutoCompleteTextView dropdownList;

    public ParaDropdown(Context context, AutoCompleteTextView dropdownList, int layout) {
        this.context = context;
        this.dropdownList = dropdownList;
        this.layout = layout;

        dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when an item is selected from the dropdown list
            }
        });

        dropdownList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownList.showDropDown();
                return false;
            }
        });
        dropdownList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dropdownList.showDropDown();
                } else {
                    dropdownList.dismissDropDown();
                }
            }
        });

        dropdownList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public AutoCompleteTextView showParaDropdown(String[] villageList) {
        adapter = new CustomArrayAdapter(context, layout, Arrays.asList(villageList));
        dropdownList.setAdapter(adapter);
        return dropdownList;
    }
}
