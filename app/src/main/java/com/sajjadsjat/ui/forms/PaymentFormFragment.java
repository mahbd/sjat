package com.sajjadsjat.ui.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sajjadsjat.databinding.FragmentPaymentFormBinding;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.util.Arrays;

public class PaymentFormFragment extends Fragment {

    private FragmentPaymentFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPaymentFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] names = {"Ruhul Amin", "Sumon Hossain", "Rober Newbold"};
        new SimpleSearchableDropdown(requireContext(), binding.paymentNameDropdown, (s) -> s).showDropdown(names);
        new SimpleSearchableDropdown(requireContext(), binding.paymentReceiverDropdown, (s) -> s).showDropdown(names);

        binding.paymentSave.setOnClickListener(v -> {
            String name = binding.paymentNameDropdown.getText().toString();
            int amount = H.stringToNumber(binding.paymentAmount.getText().toString());
            String receiver = binding.paymentReceiverDropdown.getText().toString();

            if (name.isEmpty() || Arrays.stream(names).noneMatch(name::equals)) {
                binding.paymentNameDropdown.setError("Name is required");
                return;
            }
            if (amount == 0) {
                binding.paymentAmount.setError("Amount is required");
                return;
            }
            if (receiver.isEmpty() || Arrays.stream(names).noneMatch(receiver::equals)) {
                binding.paymentReceiverDropdown.setError("Receiver is required");
                return;
            }
            // clear all errors
            binding.paymentNameDropdown.setError(null);
            binding.paymentAmount.setError(null);
            binding.paymentReceiverDropdown.setError(null);
            Toast.makeText(requireContext(), "Payment Saved", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}