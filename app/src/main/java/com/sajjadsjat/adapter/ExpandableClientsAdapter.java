package com.sajjadsjat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.sajjadsjat.R;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.ClientRecord;
import com.sajjadsjat.model.Payment;
import com.sajjadsjat.model.Record;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExpandableClientsAdapter extends BaseExpandableListAdapter {
    private String paraFilter = "";
    private String nameFilter = "";
    private final Context context;
    private List<Client> clients;
    private final List<Client> originalClients;
    private final HashMap<Long, List<ClientRecord>> userRecords;

    public ExpandableClientsAdapter(Context context, List<Client> clients, HashMap<Long, List<ClientRecord>> userRecords) {
        this.context = context;
        this.clients = clients;
        this.originalClients = clients;
        this.userRecords = userRecords;
    }

    @Override
    public int getGroupCount() {
        return clients.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Client group = clients.get(groupPosition);
        return Objects.requireNonNull(userRecords.get(group.getId())).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return clients.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Client group = clients.get(groupPosition);
        return Objects.requireNonNull(userRecords.get(group.getId())).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_client, null);
        }
        TextView groupTextView = convertView.findViewById(R.id.short_user);
        Client client = clients.get(groupPosition);
        groupTextView.setText(String.format(Locale.getDefault(), "%s (%s)", client.getName(), client.getAddress().getPara()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_client_record, null);
        }
        TextView childTextView = convertView.findViewById(R.id.record_brief_text);
        Client client = clients.get(groupPosition);
        ClientRecord clientRecord = Objects.requireNonNull(userRecords.get(client.getId())).get(childPosition);
        if (clientRecord.record != null) {
            Record record = clientRecord.record;
            double price = record.getUnitPrice() * record.getQuantity() - record.getDiscount();

            childTextView.setText(String.format(Locale.getDefault(), "%s  %s    %.0fTk", record.getDateTime(), record.getItem(), price));
            TextView recordDetailQuantity = convertView.findViewById(R.id.record_detail_quantity);
            recordDetailQuantity.setText(String.format(Locale.getDefault(), "%.2f %s", record.getQuantity(), record.getUnit()));
            TextView recordDetailItem = convertView.findViewById(R.id.record_detail_item);
            recordDetailItem.setText(record.getItem());
            TextView recordDetailSeller = convertView.findViewById(R.id.record_detail_seller);
            recordDetailSeller.setText(record.getSeller());
            TextView recordDetailUnitPrice = convertView.findViewById(R.id.record_detail_unit_price);
            recordDetailUnitPrice.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getUnitPrice()));
            TextView recordDetailDiscount = convertView.findViewById(R.id.record_detail_discount);
            recordDetailDiscount.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getDiscount()));
            TextView recordDetailFinalPrice = convertView.findViewById(R.id.record_detail_final_price);
            recordDetailFinalPrice.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getUnitPrice() * record.getQuantity() - record.getDiscount()));

            View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableLayout tableLayout = finalConvertView.findViewById(R.id.record_detail_table);
                    tableLayout.setVisibility(tableLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });
            return convertView;
        } else if (clientRecord.payment != null) {
            Payment payment = clientRecord.payment;
            childTextView.setText(String.format(Locale.getDefault(), "%s     %.0fTk", payment.getDateTime(), payment.getAmount()));
            return convertView;
        } else {
            childTextView.setText("No record found");
            return convertView;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

