package com.sajjadsjat.ui;

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
import com.sajjadsjat.databinding.FragmentEmployeesBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmployeesFragment extends Fragment {
    private FragmentEmployeesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        long employeeId = 0L;
        if (getArguments() != null) {
            employeeId = getArguments().getLong("employee", 0L);
        }
        Employee address = Employee.get(employeeId);
        if (address != null) {
            binding.etEmployeeName.setText(address.getName());
            binding.etJob.setText(address.getJob());
            binding.etPhone.setText(address.getPhone());
            binding.btnSaveEmployee.setText("Update Employee");
        }

        List<String> employees = new ArrayList<>();
        Map<String, Long> employeeMap = new HashMap<>();
        for (Employee a : Employee.getAll()) {
            String addressString = a.toString();
            employees.add(addressString);
            employeeMap.put(addressString, a.getId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, employees);
        binding.lvEmployees.setAdapter(adapter);

        binding.lvEmployees.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.long_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    String addressString = employees.get(position);
                    long addressId1 = employeeMap.get(addressString);
                    Bundle bundle = new Bundle();
                    bundle.putLong("employee", addressId1);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_employees, bundle);
                } else if (item.getItemId() == R.id.action_delete) {
                    String addressString = employees.get(position);
                    long addressId1 = employeeMap.get(addressString);
                    Address.delete(addressId1);
                    Toast.makeText(requireContext(), "Employee deleted", Toast.LENGTH_SHORT).show();
                    employees.remove(position);
                    adapter.notifyDataSetChanged();
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        binding.btnSaveEmployee.setOnClickListener(v -> {
            String name = binding.etEmployeeName.getText().toString();
            String job = binding.etJob.getText().toString();
            String phone = binding.etPhone.getText().toString();
            if (Employee.validateName(name, false) != null) {
                binding.etEmployeeName.setError(Employee.validateName(name));
                return;
            }
            binding.etEmployeeName.setError(null);
            if (job.isEmpty()) {
                binding.etJob.setError("Job is required");
                return;
            }
            binding.etJob.setError(null);
            if (Employee.validatePhone(phone, false) != null) {
                binding.etPhone.setError(Employee.validatePhone(phone));
                return;
            }
            binding.etPhone.setError(null);
            if (Employee.doesExist(name, job, phone)) {
                Toast.makeText(requireContext(), "Phone already exists", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address == null) {
                Employee newEmployee = new Employee(name, job, phone);
                Toast.makeText(requireContext(), "Employee Saved", Toast.LENGTH_SHORT).show();
                binding.etEmployeeName.setText("");
                binding.etJob.setText("");
                binding.etPhone.setText("");
                employees.add(newEmployee.toString());
                employeeMap.put(newEmployee.toString(), newEmployee.getId());
                adapter.notifyDataSetChanged();
            } else {
                address.setName(name);
                address.setJob(job);
                address.setPhone(phone);
                Toast.makeText(requireContext(), "Employee Updated", Toast.LENGTH_SHORT).show();
                employees.clear();
                employeeMap.clear();
                for (Employee a : Employee.getAll()) {
                    employees.add(a.toString());
                    employeeMap.put(a.toString(), a.getId());
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