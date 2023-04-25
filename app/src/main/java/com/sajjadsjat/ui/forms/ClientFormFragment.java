package com.sajjadsjat.ui.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sajjadsjat.databinding.FragmentClientFormBinding;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.Arrays;
import java.util.List;

public class ClientFormFragment extends Fragment {
    private FragmentClientFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClientFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        List<String> paras = Arrays.asList("Abdullahpur", "Bhurungamari", "Chilmari", "Kurigram Sadar", "Nageshwari", "Phulbari", "Rajarhat", "Raomari", "Rowmari", "Ulipur");
        new SimpleSearchableDropdown(requireContext(), binding.clientParaDropdown, (s) -> s).showDropdown(paras);
        new SimpleSearchableDropdown(requireContext(), binding.clientVillageDropdown, (s) -> s).showDropdown(paras);
        new SimpleSearchableDropdown(requireContext(), binding.clientUnionDropdown, (s) -> s).showDropdown(paras);

        binding.clientSave.setOnClickListener(v -> {
            String name = binding.clientName.getText().toString();
            String fathersName = binding.clientFathersName.getText().toString();
            String phone = binding.clientPhone.getText().toString();
            String para = binding.clientParaDropdown.getText().toString();
            String village = binding.clientVillageDropdown.getText().toString();
            String union = binding.clientUnionDropdown.getText().toString();
            String extra = binding.clientExtra.getText().toString();

            if (name.isEmpty()) {
                binding.clientName.setError("Name is required");
                return;
            }
            binding.clientName.setError(null);
            if (phone.isEmpty()) {
                binding.clientPhone.setError("Phone is required");
                return;
            }
            binding.clientPhone.setError(null);
            if (para.isEmpty()) {
                binding.clientParaDropdown.setError("Para is required");
                return;
            }
            binding.clientParaDropdown.setError(null);
            if (village.isEmpty()) {
                binding.clientVillageDropdown.setError("Village is required");
                return;
            }
            binding.clientVillageDropdown.setError(null);
            if (union.isEmpty()) {
                binding.clientUnionDropdown.setError("Union is required");
                return;
            }
            binding.clientUnionDropdown.setError(null);
            Toast.makeText(requireContext(), "Client Saved", Toast.LENGTH_SHORT).show();

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}