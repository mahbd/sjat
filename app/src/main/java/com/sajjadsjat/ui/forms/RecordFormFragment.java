package com.sajjadsjat.ui.forms;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.databinding.FragmentRecordFormBinding;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Employee;
import com.sajjadsjat.model.Price;
import com.sajjadsjat.model.Record;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecordFormFragment extends Fragment {
    Realm realm = Realm.getDefaultInstance();

    private FragmentRecordFormBinding binding;

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

        RealmResults<Price> uniqueItemPrices = realm.where(Price.class).distinct("item").findAll();
        List<String> items = new ArrayList<>();
        for (Price price : uniqueItemPrices) {
            items.add(price.getItem());
        }
        if (items.size() > 1) items.add(1, H.ITEM_DEPOSIT);
        else items.add(H.ITEM_DEPOSIT);
        RealmResults<Price> uniqueUnitPrices = realm.where(Price.class).distinct("unit").findAll();
        List<String> units = new ArrayList<>();
        for (Price price : uniqueUnitPrices) {
            units.add(price.getUnit());
        }
        List<String> employees = new ArrayList<>();
        for (Employee employee : Employee.getAll()) {
            employees.add(employee.getName());
        }

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        binding.recordItemDropdown.setAdapter(itemAdapter);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, units);
        binding.recordUnitDropdown.setAdapter(unitAdapter);

        ArrayAdapter<String> sellerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, employees);
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
                if (item.equals(H.ITEM_DEPOSIT)) {
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

        binding.recordItemDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double price = calculatePrice();
                if (price != 0) {
                    binding.recordPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    binding.recordDiscount.setVisibility(View.GONE);
                }
                RealmResults<Price> uui = realm.where(Price.class).equalTo("item", binding.recordItemDropdown.getSelectedItem().toString()).distinct("unit").findAll();
                List<String> units = new ArrayList<>();
                if (uui.size() > 0) {
                    for (Price p : uui) {
                        units.add(p.getUnit());
                    }
                    unitAdapter.clear();
                    unitAdapter.addAll(units);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.recordQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double price = calculatePrice();
                if (price != 0) {
                    binding.recordPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    binding.recordDiscount.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.recordUnitDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                double price = calculatePrice();
                if (price != 0) {
                    binding.recordPrice.setText(String.format(Locale.getDefault(), "%.2f", price));
                    binding.recordDiscount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.recordPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double enteredPrice = H.stringToDouble(binding.recordPrice.getText().toString());
                double price = calculatePrice();
                if (price != enteredPrice) {
                    binding.recordDiscount.setText(String.format(Locale.getDefault(), "Discount: %.2f", price - enteredPrice));
                    binding.recordDiscount.setVisibility(View.VISIBLE);
                } else {
                    binding.recordDiscount.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
            if (!item.equals(H.ITEM_DEPOSIT) && quantity == 0) {
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
            if (item.equals(H.ITEM_DEPOSIT)) {
                record = new Record(namesMap.get(name), createdAt, price, item, 0, seller, "TK", 0);
            } else {
                double unitPrice, discount;
                Price priceObj = realm.where(Price.class).equalTo("item", item).equalTo("unit", unit).findFirst();
                if (priceObj == null) {
                    unitPrice = price / quantity;
                    discount = 0;
                } else {
                    unitPrice = priceObj.getPrice();
                    discount = unitPrice * quantity - price;
                }
                record = new Record(namesMap.get(name), createdAt, discount, item, quantity, seller, unit, unitPrice);
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
        if (item.equals(H.ITEM_DEPOSIT)) {
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
                H.sendMessage(requireContext(), record.getClient());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private double calculatePrice() {
        String item = binding.recordItemDropdown.getSelectedItem().toString();
        double quantity = H.stringToDouble(binding.recordQuantity.getText().toString());
        String unit = binding.recordUnitDropdown.getSelectedItem().toString();
        Price priceObj = realm.where(Price.class).equalTo("item", item).equalTo("unit", unit).findFirst();
        if (priceObj != null) {
            return priceObj.getPrice() * quantity;
        }
        return 0;
    }

}