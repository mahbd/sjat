package com.sajjadsjat.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.sajjadsjat.adapter.CustomArrayAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleSearchableDropdown {
    Context context;
    CustomArrayAdapter adapter;
    int layout;
    AutoCompleteTextView dropdownList;
    List<String> optionList;

    // get on change listener
    public SimpleSearchableDropdown(Context context, AutoCompleteTextView dropdownList, Function onChange) {
        this.context = context;
        this.dropdownList = dropdownList;
        this.layout = android.R.layout.simple_list_item_1;

        dropdownList.setOnItemClickListener((parent, view, position, id) -> {
        });

        dropdownList.setThreshold(0);

        dropdownList.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dropdownList.showDropDown();
            } else {
                dropdownList.dismissDropDown();
            }
        });

        dropdownList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("TEXT: " + s);
                onChange.apply(s);
                adapter.getFilter().filter(s);
            }
        });
    }

    public void showDropdown(List<String> optionList) {
        this.optionList = optionList;
        adapter = new CustomArrayAdapter(context, layout, optionList);
        dropdownList.setAdapter(adapter);
    }

    public List<String> doFilter(String constraint) {
        if (constraint == null) constraint = "";
        constraint = constraint.toLowerCase().trim();
        if (constraint.length() == 0) return optionList;
        String finalConstraint = constraint;
        Stream<String> filtered = optionList.stream().filter(item -> item.toLowerCase().contains(finalConstraint));
        return Arrays.asList(filtered.toArray(String[]::new));
    }
}
