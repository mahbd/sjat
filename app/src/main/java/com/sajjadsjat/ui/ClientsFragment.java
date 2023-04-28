package com.sajjadsjat.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.sajjadsjat.R;
import com.sajjadsjat.adapter.ClientsAdapter;
import com.sajjadsjat.databinding.FragmentClientsBinding;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Record;
import com.sajjadsjat.utils.H;
import com.sajjadsjat.utils.SimpleSearchableDropdown;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ClientsFragment extends Fragment {

    private FragmentClientsBinding binding;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClientsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int searchNameQueryLimit = prefs.getInt("search_name_query_limit", 10);

        RealmResults<Client> clients = Realm.getDefaultInstance().where(Client.class).limit(searchNameQueryLimit).sort("due", Sort.DESCENDING).findAll();
        ClientsAdapter adapter = new ClientsAdapter(requireContext(), R.layout.client_item, searchNameQueryLimit, clients);
        binding.clientList.setAdapter(adapter);

        List<String> paras = new ArrayList<>();
        for (Address address : Address.getAll()) {
            paras.add(address.getPara());
        }
        new SimpleSearchableDropdown(this.getContext(), binding.paraDropdown, (s) -> {
            adapter.filterByPara(s.toString());
            return null;
        }).showDropdown(Arrays.asList(paras.toArray(new String[0])));

        binding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filterByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.clientList.setOnItemLongClickListener((parent, view, position, id) -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.long_popup_client, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("client", id);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                    navController.navigate(R.id.nav_client_form, bundle);
                } else if (item.getItemId() == R.id.action_delete) {
                    Client.delete(id);
                    Toast.makeText(requireContext(), "Address deleted", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else if (item.getItemId() == R.id.action_call) {
                    Client client = Client.get(id);
                    if (client != null) {
                        String phone = client.getPhone();
                        if (phone != null && !phone.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phone));
                            startActivity(intent);
                            return true;
                        }
                    }
                    Toast.makeText(requireContext(), "No phone number found", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.action_mark_paid) {
                    Client client = Client.get(id);
                    if (client != null) {
                        double due = client.getDue();
                        H.showAlert(requireContext(), "Mark as paid", String.format(Locale.getDefault(), "%s has %.2f tk due. Are you sure you want to mark as paid?", client.getName(), due), new H.AlertCallback() {
                            @Override
                            public void onOk() {
                                String owner = prefs.getString("owner", "Md. Ibrahim Khalil");
                                new Record(client, H.datetimeToTimestamp(LocalDateTime.now()), due, H.ITEM_DISCOUNT, 0, owner, "TK", 0);
                                H.sendMessage(requireContext(), client, true);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(requireContext(), "Marked as paid", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                }
                return true;
            });
            popupMenu.show();
            return true;
        });

        binding.addClient.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_client_form);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}