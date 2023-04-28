package com.sajjadsjat.ui.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentPriceFormBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Price;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PriceFormFragment extends Fragment {
    private FragmentPriceFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPriceFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        long priceId = 0L;
        if (getArguments() != null) {
            priceId = getArguments().getLong("price", 0L);
        }
        Price price = Price.get(priceId);
        if (price != null) {
            binding.etItemName.setText(price.getItem());
            binding.etUnit.setText(price.getUnit());
            binding.etPrice.setText(String.valueOf(price.getPrice()));
            binding.btnSavePrice.setText("Update Price");
        }

        List<String> prices = new ArrayList<>();
        Set<String> units = new HashSet<>();
        Set<String> items = new HashSet<>();
        Map<String, Long> priceMap = new HashMap<>();
        for (Price a : Price.getAll()) {
            String addressString = a.toString();
            prices.add(addressString);
            priceMap.put(addressString, a.getId());
            units.add(a.getUnit());
            items.add(a.getItem());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, prices);
        binding.lvPrices.setAdapter(adapter);
        new SimpleSearchableDropdown(requireContext(), binding.etItemName, s -> s)
                .showDropdown(new ArrayList<>(items));
        new SimpleSearchableDropdown(requireContext(), binding.etUnit, s -> s)
                .showDropdown(new ArrayList<>(units));

        binding.lvPrices.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.long_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    String addressString = prices.get(position);
                    long addressId1 = priceMap.get(addressString);
                    Bundle bundle = new Bundle();
                    bundle.putLong("price", addressId1);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_prices, bundle);
                } else if (item.getItemId() == R.id.action_delete) {
                    String addressString = prices.get(position);
                    long addressId1 = priceMap.get(addressString);
                    Price.delete(addressId1);
                    Toast.makeText(requireContext(), "Price deleted", Toast.LENGTH_SHORT).show();
                    prices.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        binding.btnSavePrice.setOnClickListener(v -> {
            String itemName = binding.etItemName.getText().toString();
            String unit = binding.etUnit.getText().toString();
            double unitPrice = H.stringToDouble(binding.etPrice.getText().toString());
            if (itemName.isEmpty()) {
                binding.etItemName.setError("Item name is required");
                return;
            }
            binding.etItemName.setError(null);
            if (unit.isEmpty()) {
                binding.etUnit.setError("Unit is required");
                return;
            }
            binding.etPrice.setError(null);
            if (unitPrice == 0) {
                binding.etPrice.setError("Union is required");
                return;
            }
            binding.etPrice.setError(null);
            if (Price.doesExist(itemName, unit)) {
                Toast.makeText(requireContext(), "Price already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            if (price == null) {
                Price newPrice = new Price(itemName, unitPrice, unit);
                Toast.makeText(requireContext(), "Price Saved", Toast.LENGTH_SHORT).show();
                binding.etItemName.setText("");
                binding.etUnit.setText("");
                binding.etPrice.setText("");
                prices.add(newPrice.toString());
                priceMap.put(newPrice.toString(), newPrice.getId());
                adapter.notifyDataSetChanged();
            } else {
                price.setItem(itemName);
                price.setUnit(unit);
                price.setPrice(unitPrice);
                Toast.makeText(requireContext(), "Price Updated", Toast.LENGTH_SHORT).show();
                prices.clear();
                priceMap.clear();
                for (Price a : Price.getAll()) {
                    prices.add(a.toString());
                    priceMap.put(a.toString(), a.getId());
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