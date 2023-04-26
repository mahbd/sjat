package com.sajjadsjat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sajjadsjat.model.Client;
import com.sajjadsjat.R;

import java.util.Arrays;
import java.util.List;

public class ClientsAdapter extends ArrayAdapter<Client> {
    private String paraFilter = "";
    private String nameFilter = "";
    private final List<Client> originalClients;
    private List<Client> clients;

    public ClientsAdapter(@NonNull Context context, int resource, @NonNull List<Client> clients) {
        super(context, resource, clients);
        originalClients = clients;
        this.clients = clients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = View.inflate(getContext(), R.layout.client_item, null);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Client client = getItem(position);
        if (client == null) {
            return row;
        }

        holder.nameView.setText(client.getName());
        holder.fathersNameView.setText(client.getFathersName());
        holder.phoneView.setText(client.getPhone());
        holder.addressView.setText(client.getAddress().toString());
        holder.extraView.setText(client.getExtra());
        holder.dueView.setText(String.valueOf(client.getDue()));
        return row;
    }

    public static class ViewHolder {
        public TextView nameView;
        public TextView fathersNameView;
        public TextView phoneView;
        public TextView addressView;
        public TextView extraView;
        public TextView dueView;

        public ViewHolder(View view) {
            nameView = view.findViewById(R.id.tv_client_name);
            fathersNameView = view.findViewById(R.id.tv_client_fathers_name);
            phoneView = view.findViewById(R.id.tv_client_phone);
            addressView = view.findViewById(R.id.tv_client_address);
            extraView = view.findViewById(R.id.tv_client_extra);
            dueView = view.findViewById(R.id.tv_client_due);
        }
    }

    @Nullable
    @Override
    public Client getItem(int position) {
        return clients.get(position);
    }

    public int getPosition(Client item) {
        return clients.indexOf(item);
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    public void filterByName(String searchQuery) {
        this.nameFilter = searchQuery;
        this.applyFilters();
    }

    public void filterByPara(String searchQuery) {
        this.paraFilter = searchQuery;
        this.applyFilters();
    }

    private void applyFilters() {
        Object[] filtered = originalClients.stream()
                .filter(client -> client.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                .filter(client -> client.getAddress().getPara().toLowerCase().contains(paraFilter.toLowerCase()))
                .toArray();
        clients = Arrays.asList(Arrays.copyOf(filtered, filtered.length, Client[].class));
        this.notifyDataSetChanged();
    }
}
