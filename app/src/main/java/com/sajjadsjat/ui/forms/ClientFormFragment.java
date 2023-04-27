package com.sajjadsjat.ui.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentClientFormBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientFormFragment extends Fragment {
    private FragmentClientFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClientFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        long clientId = 0L;
        if (getArguments() != null) {
            clientId = getArguments().getLong("client");
        }
        Client client = Client.get(clientId);
        if (client != null) {
            binding.clientName.setText(client.getName());
            binding.clientFathersName.setText(client.getFathersName());
            binding.clientPhone.setText(client.getPhone());
            binding.clientAddressDropdown.setText(client.getAddress().toString());
            binding.clientExtra.setText(client.getExtra());
            binding.clientSave.setText("Update Client");
        }

        List<Address> addresseObjects = Address.getAll();
        List<String> addresses = new ArrayList<>();
        Map<String, Address> addressMap = new HashMap<>();
        for (Address a : addresseObjects) {
            addresses.add(a.toString());
            addressMap.put(a.toString(), a);
        }

        new SimpleSearchableDropdown(requireContext(), binding.clientAddressDropdown, s -> s)
                .showDropdown(addresses);

        binding.clientSave.setOnClickListener(v -> {
            String name = binding.clientName.getText().toString();
            String fathersName = binding.clientFathersName.getText().toString();
            String phone = binding.clientPhone.getText().toString();
            String address = binding.clientAddressDropdown.getText().toString();
            String extra = binding.clientExtra.getText().toString();

            if (name.isEmpty()) {
                binding.clientName.setError("Name is required");
                return;
            }
            binding.clientName.setError(null);
            if (Client.validatePhone(phone) != null) {
                binding.clientPhone.setError(Client.validatePhone(phone));
                return;
            }
            binding.clientPhone.setError(null);
            if (address.isEmpty()) {
                binding.clientAddressDropdown.setError("Address is required");
                return;
            }
            binding.clientAddressDropdown.setError(null);
            if (addresses.stream().noneMatch(address::equals)) {
                binding.clientAddressDropdown.setError("Address is not valid");
                return;
            }
            binding.clientAddressDropdown.setError(null);
            if (client == null) {
                new Client(name, fathersName, phone, addressMap.get(address), extra);
                Toast.makeText(requireContext(), "Client Saved", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_clients);
            }  else {
                client.setName(name);
                client.setFathersName(fathersName);
                client.setPhone(phone);
                client.setAddress(addressMap.get(address));
                client.setExtra(extra);
                Toast.makeText(requireContext(), "Client Updated", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_clients);
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