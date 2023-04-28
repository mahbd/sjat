package com.sajjadsjat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.sajjadsjat.R;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Record;
import com.sajjadsjat.utils.H;

import java.util.List;
import java.util.Locale;

import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

public class ExpandableClientsAdapter extends BaseExpandableListAdapter {
    private final Realm realm = Realm.getDefaultInstance();
    private String paraFilter = "";
    private String nameFilter = "";
    private final Context context;
    private List<Client> clients;
    private final int queryLimit;

    public ExpandableClientsAdapter(Context context, List<Client> clients, int queryLimit) {
        this.context = context;
        this.clients = clients;
        this.queryLimit = queryLimit;
    }

    @Override
    public int getGroupCount() {
        return clients.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Client group = clients.get(groupPosition);
        return (int) realm.where(Record.class).equalTo("client.id", group.getId()).count();
    }

    @Override
    public Client getGroup(int groupPosition) {
        return clients.get(groupPosition);
    }

    @Override
    public Record getChild(int groupPosition, int childPosition) {
        Client group = clients.get(groupPosition);
        return realm.where(Record.class).equalTo("client.id", group.getId()).findAll().get(childPosition);
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
        Record record = getChild(groupPosition, childPosition);

        if (record != null && record.getItem().equals(H.ITEM_DEPOSIT)) {
            childTextView.setText(String.format(Locale.getDefault(), "%s     %.0fTk", record.getDateTimeShort(), record.getDiscount()));
            TextView recordFullTime = convertView.findViewById(R.id.record_detail_full_date);
            recordFullTime.setText(record.getDateTime());
            TextView recordDetailQuantity = convertView.findViewById(R.id.record_detail_quantity);
            recordDetailQuantity.setVisibility(View.GONE);
            TextView recordDetailSeller = convertView.findViewById(R.id.record_detail_seller);
            recordDetailSeller.setText(record.getSeller());

            View finalConvertView = convertView;
            convertView.setOnClickListener(v -> {
                LinearLayout normalLayout = finalConvertView.findViewById(R.id.record_normal_layout);
                normalLayout.setVisibility(normalLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });
            return convertView;
        } else if (record != null && record.getItem().equals(H.ITEM_DISCOUNT)) {
            childTextView.setText(String.format(Locale.getDefault(), "%s Discounted %.0fTk and paid", record.getDateTimeShort(), record.getDiscount()));
            return convertView;
        } else if (record != null && !record.getItem().equals("Payment")) {
            double price = record.getUnitPrice() * record.getQuantity() - record.getDiscount();

            childTextView.setText(String.format(Locale.getDefault(), "%s  %s    %.0fTk", record.getDateTimeShort(), record.getItem(), price));
            TextView recordFullTime = convertView.findViewById(R.id.record_detail_full_date);
            recordFullTime.setText(record.getDateTime());
            TextView recordDetailQuantity = convertView.findViewById(R.id.record_detail_quantity);
            recordDetailQuantity.setText(String.format(Locale.getDefault(), "%.2f %s", record.getQuantity(), record.getUnit()));
            recordDetailQuantity.setVisibility(View.VISIBLE);
            TextView recordDetailSeller = convertView.findViewById(R.id.record_detail_seller);
            recordDetailSeller.setText(record.getSeller());
            TextView recordDetailUnitPrice = convertView.findViewById(R.id.record_detail_unit_price);
            recordDetailUnitPrice.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getUnitPrice()));
            TextView recordDetailDiscount = convertView.findViewById(R.id.record_detail_discount);
            recordDetailDiscount.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getDiscount()));
            TextView recordDetailFinalPrice = convertView.findViewById(R.id.record_detail_final_price);
            recordDetailFinalPrice.setText(String.format(Locale.getDefault(), "%.2f Tk", record.getUnitPrice() * record.getQuantity() - record.getDiscount()));

            View finalConvertView = convertView;
            convertView.setOnClickListener(v -> {
                TableLayout tableLayout = finalConvertView.findViewById(R.id.record_detail_table);
                LinearLayout normalLayout = finalConvertView.findViewById(R.id.record_normal_layout);
                tableLayout.setVisibility(tableLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                normalLayout.setVisibility(normalLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });
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
        Realm realm = Realm.getDefaultInstance();
        clients = realm.where(Client.class).contains("name", nameFilter, Case.INSENSITIVE).contains("address.para", paraFilter, Case.INSENSITIVE).sort("due", Sort.DESCENDING).limit(queryLimit).findAll();
        this.notifyDataSetChanged();
    }
}

