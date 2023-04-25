package com.sajjadsjat.ui.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.sajjadsjat.databinding.FragmentPaymentFormBinding;

public class PaymentFormFragment extends Fragment {

    private FragmentPaymentFormBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPaymentFormBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AutoCompleteTextView nameDropdown = binding.paymentNameDropdown;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}