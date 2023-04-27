package com.sajjadsjat.ui;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeFragment extends Fragment {
    private final Realm realm = Realm.getDefaultInstance();
    SharedPreferences prefs;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int homeQueryLimit = prefs.getInt("home_query_limit", 20);

        EditText searchBox = binding.searchBox;
        AutoCompleteTextView paraDropdown = binding.paraDropdown;

        ExpandableListView expandableListView = binding.expandableListView;
        RealmResults<Client> clients = realm.where(Client.class).limit(homeQueryLimit).findAll();


        ExpandableClientsAdapter adapter = new ExpandableClientsAdapter(this.getContext(), clients, homeQueryLimit);

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

        List<String> paras = new ArrayList<>();
        for (Address address : Address.getAll()) {
            paras.add(address.getPara());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}