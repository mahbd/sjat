package com.sajjadsjat.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sajjadsjat.adapter.ExpandableClientsAdapter;
import com.sajjadsjat.databinding.FragmentHomeBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.UserData;
import com.sajjadsjat.model.ClientRecord;
import com.sajjadsjat.model.UserRecordData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText searchBox = binding.searchBox;
        AutoCompleteTextView paraDropdown = binding.paraDropdown;

        ExpandableListView expandableListView = binding.expandableListView;
        List<Client> clients = Arrays.asList(UserData.clients);

        HashMap<Integer, List<ClientRecord>> childItems = new HashMap<>();
        List<ClientRecord> group1Items = new ArrayList<>();
        group1Items.add(UserRecordData.instance1);
        group1Items.add(UserRecordData.instance2);
        childItems.put(clients.get(0).id, group1Items);
        List<ClientRecord> group2Items = new ArrayList<>();
        group2Items.add(UserRecordData.instance3);
        group2Items.add(UserRecordData.instance4);
        childItems.put(clients.get(1).id, group2Items);
        List<ClientRecord> group3Items = new ArrayList<>();
        group3Items.add(UserRecordData.instance5);
        group3Items.add(UserRecordData.instance6);
        childItems.put(clients.get(2).id, group3Items);

        ExpandableClientsAdapter adapter = new ExpandableClientsAdapter(this.getContext(), clients, childItems);

        expandableListView.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filterByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Set<String> paras = new HashSet<>();
        paras.add("");
        for (Client client : clients) {
            paras.add(client.para);
        }


        AutoCompleteTextView dropdownList = new ParaDropdown(this.getContext(), paraDropdown, android.R.layout.simple_list_item_1)
                .showParaDropdown(paras.toArray(new String[0]));
        dropdownList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filterByPara(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}