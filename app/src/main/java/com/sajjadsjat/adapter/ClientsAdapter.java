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

import java.util.List;
import java.util.Locale;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class ClientsAdapter extends ArrayAdapter<Client> {
    private final Realm realm = Realm.getDefaultInstance();
    private final int queryLimit;
    private String paraFilter = "";
    private String nameFilter = "";
    private RealmResults<Client> clients;

    public ClientsAdapter(@NonNull Context context, int resource, int queryLimit, RealmResults<Client> clients) {
        super(context, resource, clients);
        this.queryLimit = queryLimit;
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
        holder.dueView.setText(String.format(Locale.getDefault(), "%.2f", client.getDue()));
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

    @Override
    public long getItemId(int position) {
        assert clients.get(position) != null;
        return clients.get(position).getId();
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
        clients = realm.where(Client.class).contains("name", nameFilter, Case.INSENSITIVE).contains("address.para", paraFilter, Case.INSENSITIVE).limit(queryLimit).findAll();
        this.notifyDataSetChanged();
    }
}
