package com.example.diet2.ui.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diet2.R;
import com.example.diet2.databinding.FragmentBmiCalculatorBinding;

import java.util.Locale;

public class BmiCalculatorFragment extends Fragment {

    private FragmentBmiCalculatorBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentBmiCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.btnCalculate.setOnClickListener(v -> {
            clearErrors();

            String heightCmStr = text(binding.inputHeight);
            String weightKgStr = text(binding.inputWeight);

            boolean ok = true;
            if (TextUtils.isEmpty(heightCmStr)) {
                binding.tilHeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(weightKgStr)) {
                binding.tilWeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (!ok) return;

            double heightCm;
            double weightKg;
            try {
                heightCm = Double.parseDouble(heightCmStr);
                weightKg = Double.parseDouble(weightKgStr);
            } catch (NumberFormatException e) {
                binding.tilHeight.setError(getString(R.string.bmi_error_invalid_number));
                binding.tilWeight.setError(getString(R.string.bmi_error_invalid_number));
                return;
            }

            if (heightCm <= 0 || weightKg <= 0) {
                binding.tilHeight.setError(getString(R.string.bmi_error_positive));
                binding.tilWeight.setError(getString(R.string.bmi_error_positive));
                return;
            }

            double heightM = heightCm / 100.0;
            double bmi = weightKg / (heightM * heightM);

            binding.txtBmiValue.setText(String.format(Locale.US, "%.1f", bmi));
            binding.txtBmiCategory.setText(categoryFor(bmi));
            binding.cardResults.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clearErrors() {
        binding.tilHeight.setError(null);
        binding.tilWeight.setError(null);
    }

    private static String text(@NonNull com.google.android.material.textfield.TextInputEditText editText) {
        CharSequence cs = editText.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private String categoryFor(double bmi) {
        if (bmi < 18.5) return getString(R.string.bmi_category_underweight);
        if (bmi < 25.0) return getString(R.string.bmi_category_normal);
        if (bmi < 30.0) return getString(R.string.bmi_category_overweight);
        return getString(R.string.bmi_category_obese);
    }
}
