package com.example.diet2.ui.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diet2.R;
import com.example.diet2.databinding.FragmentDailyCalorieCalculatorBinding;

public class DailyCalorieCalculatorFragment extends Fragment {

    private FragmentDailyCalorieCalculatorBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentDailyCalorieCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender_options,
                android.R.layout.simple_list_item_1
        );
        binding.inputGender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.activity_level_options,
                android.R.layout.simple_list_item_1
        );
        binding.inputActivity.setAdapter(activityAdapter);

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.goal_type_options,
                android.R.layout.simple_list_item_1
        );
        binding.inputGoal.setAdapter(goalAdapter);

        // Default mock selections (UI only)
        binding.inputGender.setText(getString(R.string.gender_male), false);
        binding.inputActivity.setText(getString(R.string.activity_moderate), false);
        binding.inputGoal.setText(getString(R.string.goal_maintain), false);

        binding.btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.btnPreview.setOnClickListener(v -> {
            clearErrors();

            String height = text(binding.inputHeight);
            String weight = text(binding.inputWeight);
            String age = text(binding.inputAge);
            String gender = text(binding.inputGender);
            String activity = text(binding.inputActivity);
            String goal = text(binding.inputGoal);

            boolean ok = true;

            if (TextUtils.isEmpty(height)) {
                binding.tilHeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(weight)) {
                binding.tilWeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(age)) {
                binding.tilAge.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(gender)) {
                binding.tilGender.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(activity)) {
                binding.tilActivity.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(goal)) {
                binding.tilGoal.setError(getString(R.string.error_required));
                ok = false;
            }

            if (!ok) return;

            // Result placeholders (no calculations yet)
            binding.txtBmrValue.setText(getString(R.string.calc_result_bmr_value));
            binding.txtMaintenanceValue.setText(getString(R.string.calc_result_maintenance_value));
            binding.txtTargetValue.setText(getString(R.string.calc_result_target_value));
            binding.cardResults.setVisibility(View.VISIBLE);

            Toast.makeText(requireContext(), R.string.calc_preview_mock_toast, Toast.LENGTH_SHORT).show();
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
        binding.tilAge.setError(null);
        binding.tilGender.setError(null);
        binding.tilActivity.setError(null);
        binding.tilGoal.setError(null);
    }

    private static String text(@NonNull com.google.android.material.textfield.TextInputEditText editText) {
        CharSequence cs = editText.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private static String text(@NonNull com.google.android.material.textfield.MaterialAutoCompleteTextView view) {
        CharSequence cs = view.getText();
        return cs == null ? "" : cs.toString().trim();
    }
}
