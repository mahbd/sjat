package com.sajjadsjat.ui.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sajjadsjat.databinding.FragmentPaymentFormBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.ClientRecord;
import com.sajjadsjat.model.Employee;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

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

        List<Client> clients = Client.get(new HashMap<>());
        List<String> names = new ArrayList<>();
        Map<String, Client> clientMap = new HashMap<>();
        for (Client client : clients) {
            clientMap.put(client.getName(), client);
            assert client.getExtraIdentifier() != null;
            if (!client.getExtraIdentifier().isEmpty()) {
                names.add(String.format("%s %s %s", client.getName(), client.getPara(), client.getExtraIdentifier()));
            } else {
                names.add(String.format("%s %s", client.getName(), client.getPara()));
            }
        }

        List<Employee> employees = Employee.getAll();
        List<String> employeeNames = new ArrayList<>();
        for (Employee employee : employees) {
            employeeNames.add(employee.getName());
        }

        new SimpleSearchableDropdown(requireContext(), binding.paymentNameDropdown, (s) -> s).showDropdown(names);
        new SimpleSearchableDropdown(requireContext(), binding.paymentReceiverDropdown, (s) -> s).showDropdown(employeeNames);

        binding.paymentSave.setOnClickListener(v -> {
            String name = binding.paymentNameDropdown.getText().toString();
            int amount = H.stringToNumber(binding.paymentAmount.getText().toString());
            String receiver = binding.paymentReceiverDropdown.getText().toString();

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
            if (receiver.isEmpty() || names.stream().noneMatch(receiver::equals)) {
                binding.paymentReceiverDropdown.setError("Receiver is required");
                return;
            }
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