package com.sajjadsjat.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.sajjadsjat.adapter.CustomArrayAdapter;

import java.util.Arrays;
import java.util.function.Function;

public class SimpleSearchableDropdown {
    Context context;
    CustomArrayAdapter adapter;
    int layout;
    AutoCompleteTextView dropdownList;

    // get on change listener
    public SimpleSearchableDropdown(Context context, AutoCompleteTextView dropdownList, Function onChange) {
        this.context = context;
        this.dropdownList = dropdownList;
        this.layout = android.R.layout.simple_list_item_1;

        dropdownList.setOnItemClickListener((parent, view, position, id) -> {
        });

        dropdownList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdownList.showDropDown();
                return false;
            }
        });
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
                onChange.apply(s);
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void showDropdown(String[] optionList) {
        adapter = new CustomArrayAdapter(context, layout, Arrays.asList(optionList));
        dropdownList.setAdapter(adapter);
    }
}
