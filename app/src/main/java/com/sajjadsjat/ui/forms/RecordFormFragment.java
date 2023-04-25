package com.sajjadsjat.ui.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sajjadsjat.databinding.FragmentRecordFormBinding;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.Arrays;

public class RecordFormFragment extends Fragment {

    private FragmentRecordFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecordFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        String[] names = {"Ruhul Amin", "Sumon Hossain", "Rober Newbold"};
        String[] items = {"Item 1", "Item 2", "Item 3"};
        String[] units = {"Unit 1", "Unit 2", "Unit 3"};
        new SimpleSearchableDropdown(requireContext(), binding.recordNameDropdown, (s) -> s).showDropdown(names);
        new SimpleSearchableDropdown(requireContext(), binding.recordItemDropdown, (s) -> s).showDropdown(items);
        new SimpleSearchableDropdown(requireContext(), binding.recordUnitDropdown, (s) -> s).showDropdown(units);

        binding.recordSave.setOnClickListener(v -> {
            String name = binding.recordNameDropdown.getText().toString();
            String item = binding.recordItemDropdown.getText().toString();
            String unit = binding.recordUnitDropdown.getText().toString();
            double quantity = H.stringToDouble(binding.recordQuantity.getText().toString());
            double price = H.stringToDouble(binding.recordPrice.getText().toString());

            if (name.isEmpty() || Arrays.stream(names).noneMatch(name::equals)) {
                binding.recordNameDropdown.setError("Name is required");
                return;
            }
            binding.recordNameDropdown.setError(null);
            if (item.isEmpty() || Arrays.stream(items).noneMatch(item::equals)) {
                binding.recordItemDropdown.setError("Item is required");
                return;
            }
            binding.recordItemDropdown.setError(null);
            if (unit.isEmpty() || Arrays.stream(units).noneMatch(unit::equals)) {
                binding.recordUnitDropdown.setError("Unit is required");
                return;
            }
            binding.recordUnitDropdown.setError(null);
            if (quantity == 0) {
                binding.recordQuantity.setError("Quantity is required");
                return;
            }
            binding.recordQuantity.setError(null);
            if (price == 0) {
                binding.recordPrice.setError("Price is required");
                return;
            }
            binding.recordPrice.setError(null);
            Toast.makeText(requireContext(), "Record Saved", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}