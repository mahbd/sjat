package com.sajjadsjat.ui.forms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentPaymentFormBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Payment;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentFormFragment extends Fragment {

    private FragmentPaymentFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPaymentFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<String> names = new ArrayList<>();
        Map<String, Client> namesMap = new HashMap<>();
        for (Client client : Client.getAll()) {
            String name;
            assert client.getExtra() != null;
            if (!client.getExtra().isEmpty()) {
                name = String.format("%s %s %s", client.getName(), client.getExtra(), client.getAddress().getPara());
            } else {
                name = String.format("%s %s", client.getName(), client.getAddress().getPara());
            }
            names.add(name);
            namesMap.put(name, client);
        }
        new SimpleSearchableDropdown(requireContext(), binding.paymentNameDropdown, (s) -> s).showDropdown(names);

        ArrayAdapter<CharSequence> receiverAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.arrays_employees, android.R.layout.simple_spinner_item);
        binding.paymentReceiverDropdown.setAdapter(receiverAdapter);
        ArrayAdapter<CharSequence> methodAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.arrays_pay_methods, android.R.layout.simple_spinner_item);
        binding.paymentMethodDropdown.setAdapter(methodAdapter);

        LocalDateTime now = LocalDateTime.now();
        binding.paymentTimestamp1.setText(String.format("%s", H.datetimeToTimestamp(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0))));
        binding.paymentTimestamp1.setOnClickListener(v -> {
            int year = now.getYear();
            int month = now.getMonthValue() - 1;
            int day = now.getDayOfMonth();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
                LocalDateTime dateTime = LocalDateTime.of(year1, month1 + 1, dayOfMonth, 0, 0);
                long timestamp = H.datetimeToTimestamp(dateTime);
                binding.paymentTimestamp1.setText(String.valueOf(timestamp));
            }, year, month, day);

            datePickerDialog.show();
        });

        binding.paymentTimestamp2.setText(String.format("%s", (now.getHour() * 60 + now.getMinute()) * 60));
        binding.paymentTimestamp2.setOnClickListener(v -> {
            int hour = now.getHour() % 12;
            if (hour == 0) hour = 12;
            int minute = now.getMinute();
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
                int totalMinutes = (hourOfDay * 60 + minute1) * 60;
                binding.paymentTimestamp2.setText(String.valueOf(totalMinutes));
            }, hour, minute, false);


            timePickerDialog.show();
        });

        binding.paymentSave.setOnClickListener(v -> {
            String name = binding.paymentNameDropdown.getText().toString();
            int amount = (int) H.stringToNumber(binding.paymentAmount.getText().toString());
            String receiver = binding.paymentReceiverDropdown.getSelectedItem().toString();
            long timestamp1 = H.stringToNumber(binding.paymentTimestamp1.getText().toString());
            long timestamp2 = H.stringToNumber(binding.paymentTimestamp2.getText().toString());
            long createdAt = timestamp1 + timestamp2;
            String method = binding.paymentMethodDropdown.getSelectedItem().toString();

            if (name.isEmpty() || names.stream().noneMatch(name::equals)) {
                binding.paymentNameDropdown.setError("Name is required");
                return;
            }
            binding.paymentNameDropdown.setError(null);
            if (amount == 0) {
                binding.paymentAmount.setError("Amount is required");
                return;
            }
            binding.paymentAmount.setError(null);
            new Payment(amount, namesMap.get(name), createdAt, false, method, receiver);
            Toast.makeText(requireContext(), "Payment Saved", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}