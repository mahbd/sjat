package com.sajjadsjat.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.adapter.ClientsAdapter;
import com.sajjadsjat.databinding.FragmentClientsBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.ui.forms.ClientFormFragment;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientsFragment extends Fragment {

    private FragmentClientsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Client> clients = Client.getAll();
        ClientsAdapter adapter = new ClientsAdapter(requireContext(), R.layout.client_item, clients);
        binding.clientList.setAdapter(adapter);

        List<String> paras = new ArrayList<>();
        for (Client client : clients) {
            paras.add(client.getAddress().getPara());
        }
        new SimpleSearchableDropdown(this.getContext(), binding.paraDropdown, (s) -> {
            adapter.filterByPara(s.toString());
            return null;
        }).showDropdown(Arrays.asList(paras.toArray(new String[0])));

        binding.searchBox.addTextChangedListener(new TextWatcher() {
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

        binding.clientList.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.long_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    Client client = clients.get(position);
                    ClientFormFragment addressFragment = new ClientFormFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("client", client.getId());
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_client_form, bundle);
                } else if (item.getItemId() == R.id.action_delete) {
                    Client client = clients.get(position);
                    Client.delete(client.getId());
                    Toast.makeText(requireContext(), "Address deleted", Toast.LENGTH_SHORT).show();
                    clients.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        binding.addClient.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_client_form);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}