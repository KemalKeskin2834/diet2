package com.example.diet2.ui.foodentry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diet2.databinding.BottomSheetFoodEntryDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FoodEntryDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_SUBTITLE = "subtitle";

    private BottomSheetFoodEntryDetailsBinding binding;

    public static FoodEntryDetailsBottomSheet newInstance(String title, String subtitle) {
        FoodEntryDetailsBottomSheet sheet = new FoodEntryDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_SUBTITLE, subtitle);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = BottomSheetFoodEntryDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String title = args == null ? null : args.getString(ARG_TITLE);
        String subtitle = args == null ? null : args.getString(ARG_SUBTITLE);

        if (title != null && !title.isEmpty()) {
            binding.txtTitle.setText(title);
        }
        if (subtitle != null && !subtitle.isEmpty()) {
            binding.txtSubtitle.setText(subtitle);
        }

        // Mock values (no calculations yet)
        binding.txtCaloriesValue.setText("220 kcal");
        binding.btnClose.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
