package com.sajjadsjat.ui.forms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentRecordFormBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Record;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordFormFragment extends Fragment {

    private FragmentRecordFormBinding binding;
    private final String ITEM_PAYMENT = "Payment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecordFormBinding.inflate(inflater, container, false);
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
        new SimpleSearchableDropdown(requireContext(), binding.recordNameDropdown, (s) -> s).showDropdown(names);

        ArrayAdapter<CharSequence> itemAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.arrays_items, android.R.layout.simple_spinner_item);
        binding.recordItemDropdown.setAdapter(itemAdapter);

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.arrays_units, android.R.layout.simple_spinner_item);
        binding.recordUnitDropdown.setAdapter(unitAdapter);

        ArrayAdapter<CharSequence> sellerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.arrays_employees, android.R.layout.simple_spinner_item);
        binding.recordSellerDropdown.setAdapter(sellerAdapter);

        LocalDateTime now = LocalDateTime.now();
        binding.recordTimestamp1.setText(String.format("%s", H.datetimeToTimestamp(LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0))));
        binding.recordTimestamp1.setOnClickListener(v -> {
            int year = now.getYear();
            int month = now.getMonthValue() - 1;
            int day = now.getDayOfMonth();
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
                LocalDateTime dateTime = LocalDateTime.of(year1, month1 + 1, dayOfMonth, 0, 0);
                long timestamp = H.datetimeToTimestamp(dateTime);
                binding.recordTimestamp1.setText(String.valueOf(timestamp));
            }, year, month, day);

            datePickerDialog.show();
        });

        binding.recordTimestamp2.setText(String.format("%s", (now.getHour() * 60 + now.getMinute()) * 60));
        binding.recordTimestamp2.setOnClickListener(v -> {
            int hour = now.getHour() % 12;
            if (hour == 0) hour = 12;
            int minute = now.getMinute();
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute1) -> {
                int totalMinutes = (hourOfDay * 60 + minute1) * 60;
                binding.recordTimestamp2.setText(String.valueOf(totalMinutes));
            }, hour, minute, false);


            timePickerDialog.show();
        });

        binding.recordItemDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = binding.recordItemDropdown.getSelectedItem().toString();
                if (item.equals(ITEM_PAYMENT)) {
                    binding.recordQuantity.setVisibility(View.GONE);
                    binding.recordUnitDropdown.setVisibility(View.GONE);
                    binding.recordPrice.setHint("Amount");
                } else {
                    binding.recordQuantity.setVisibility(View.VISIBLE);
                    binding.recordUnitDropdown.setVisibility(View.VISIBLE);
                    binding.recordPrice.setHint("Price");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.recordSave.setOnClickListener(v -> {
            String name = binding.recordNameDropdown.getText().toString();
            String item = binding.recordItemDropdown.getSelectedItem().toString();
            double quantity = H.stringToDouble(binding.recordQuantity.getText().toString());
            String unit = binding.recordUnitDropdown.getSelectedItem().toString();
            double price = H.stringToDouble(binding.recordPrice.getText().toString());
            long timestamp1 = H.stringToNumber(binding.recordTimestamp1.getText().toString());
            long timestamp2 = H.stringToNumber(binding.recordTimestamp2.getText().toString());
            long createdAt = timestamp1 + timestamp2;
            String seller = binding.recordSellerDropdown.getSelectedItem().toString();

            if (name.isEmpty() || names.stream().noneMatch(name::equals)) {
                binding.recordNameDropdown.setError("Name is required");
                return;
            }
            binding.recordNameDropdown.setError(null);
            if (!item.equals(ITEM_PAYMENT) && quantity == 0) {
                binding.recordQuantity.setError("Quantity is required");
                return;
            }
            binding.recordQuantity.setError(null);
            if (price == 0) {
                binding.recordPrice.setError("Price is required");
                return;
            }
            binding.recordPrice.setError(null);

            Record record;
            if (item.equals(ITEM_PAYMENT)) {
                record = new Record(namesMap.get(name), createdAt, price, item, 0, seller, "TK", 0);
            } else {
                record = new Record(namesMap.get(name), createdAt, 0.0, item, quantity, seller, unit, price / quantity);
            }
            showSendSmsPop(record);

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showSendSmsPop(Record record) {
        String name = record.getClient().getName();
        String item = record.getItem();
        String message;
        if (item.equals(ITEM_PAYMENT)) {
            message = String.format(Locale.getDefault(), "Send message to %s for payment of %.0f", name, record.getTotal());
        } else {
            message = String.format(Locale.getDefault(), "Send message to %s for buying %f %s with price %.0f", name, record.getQuantity(), record.getUnit(), record.getTotal());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send message");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                H.sendMessage(record.getClient());
                Toast.makeText(requireContext(), "Sending message...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}