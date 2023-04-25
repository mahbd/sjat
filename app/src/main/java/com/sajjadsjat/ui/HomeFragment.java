package com.sajjadsjat.ui;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.adapter.ExpandableClientsAdapter;
import com.sajjadsjat.databinding.FragmentHomeBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.ClientRecord;
import com.sajjadsjat.model.Item;
import com.sajjadsjat.model.Record;
import com.sajjadsjat.model.Unit;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText searchBox = binding.searchBox;
        AutoCompleteTextView paraDropdown = binding.paraDropdown;

        ExpandableListView expandableListView = binding.expandableListView;
        List<Client> clients = Client.get(new HashMap<>());

        HashMap<Long, List<ClientRecord>> childItems = new HashMap<>();
        for (Client client : clients) {
            List<ClientRecord> clientRecords = new ArrayList<>();
            if (client.records != null) {
                for (Record record : client.records) {
                    if (record.getId() == client.getId()) {
                        clientRecords.add(new ClientRecord(record));
                    }
                }
            }
            childItems.put(client.getId(), clientRecords);
        }


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
            paras.add(client.getPara());
        }

        new SimpleSearchableDropdown(this.getContext(), paraDropdown, (s) -> {
            adapter.filterByPara(s.toString());
            return null;
        }).showDropdown(Arrays.asList(paras.toArray(new String[0])));

        binding.addRecord.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_record_form);
        });

        binding.addPayment.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_payment_form);
        });

        return root;
    }

    Item getOrCreateItem(String name) {
        Item item = Item.get(name);
        if (item == null) {
            item = new Item(name);
        }
        return item;
    }

    Unit getOrCreateUnit(String name) {
        Unit unit = Unit.get(name);
        if (unit == null) {
            unit = new Unit(name);
        }
        return unit;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}