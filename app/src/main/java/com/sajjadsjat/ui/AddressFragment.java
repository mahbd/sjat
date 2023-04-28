package com.sajjadsjat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentAddressBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddressFragment extends Fragment {
    private FragmentAddressBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        long addressId = 0L;
        if (getArguments() != null) {
            addressId = getArguments().getLong("address", 0L);
        }
        Address address = Address.get(addressId);
        if (address != null) {
            binding.etPara.setText(address.getPara());
            binding.etVillage.setText(address.getVillage());
            binding.etUnion.setText(address.getUnion());
            binding.btnSaveAddress.setText("Update Address");
        }

        List<String> addresses = new ArrayList<>();
        Set<String> villages = new HashSet<>();
        Set<String> unions = new HashSet<>();
        Map<String, Long> addressMap = new HashMap<>();
        for (Address a : Address.getAll()) {
            String addressString = a.toString();
            addresses.add(addressString);
            addressMap.put(addressString, a.getId());
            villages.add(a.getVillage());
            unions.add(a.getUnion());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, addresses);
        new SimpleSearchableDropdown(requireContext(), binding.etVillage, s -> s)
                .showDropdown(new ArrayList<>(villages));

        new SimpleSearchableDropdown(requireContext(), binding.etUnion, s -> s)
                .showDropdown(new ArrayList<>(unions));
        binding.lvAddresses.setAdapter(adapter);

        binding.lvAddresses.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.long_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    String addressString = addresses.get(position);
                    long addressId1 = addressMap.get(addressString);
                    Bundle bundle = new Bundle();
                    bundle.putLong("address", addressId1);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_address, bundle);
                } else if (item.getItemId() == R.id.action_delete) {
                    String addressString = addresses.get(position);
                    long addressId1 = addressMap.get(addressString);
                    Address.delete(addressId1);
                    Toast.makeText(requireContext(), "Address deleted", Toast.LENGTH_SHORT).show();
                    addresses.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        binding.btnSaveAddress.setOnClickListener(v -> {
            String para = binding.etPara.getText().toString();
            String village = binding.etVillage.getText().toString();
            String union = binding.etUnion.getText().toString();
            if (para.isEmpty()) {
                binding.etPara.setError("Para is required");
                return;
            }
            binding.etPara.setError(null);
            if (village.isEmpty()) {
                binding.etVillage.setError("Village is required");
                return;
            }
            binding.etVillage.setError(null);
            if (union.isEmpty()) {
                binding.etUnion.setError("Union is required");
                return;
            }
            binding.etUnion.setError(null);
            if (Address.doesExist(para, village, union)) {
                Toast.makeText(requireContext(), "Address already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address == null) {
                Address newAddress = new Address(para, village, union);
                Toast.makeText(requireContext(), "Address Saved", Toast.LENGTH_SHORT).show();
                binding.etPara.setText("");
                binding.etVillage.setText("");
                binding.etUnion.setText("");
                addresses.add(newAddress.toString());
                addressMap.put(newAddress.toString(), newAddress.getId());
                adapter.notifyDataSetChanged();
            } else {
                address.setPara(para);
                address.setVillage(village);
                address.setUnion(union);
                Toast.makeText(requireContext(), "Address Updated", Toast.LENGTH_SHORT).show();
                addresses.clear();
                addressMap.clear();
                for (Address a : Address.getAll()) {
                    addresses.add(a.toString());
                    addressMap.put(a.toString(), a.getId());
                }
                adapter.notifyDataSetChanged();
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