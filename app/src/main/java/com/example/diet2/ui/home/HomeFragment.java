package com.example.diet2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diet2.databinding.FragmentHomeBinding;
import com.example.diet2.ui.foodentry.FoodEntryAdapter;
import com.example.diet2.ui.foodentry.FoodEntryDetailsBottomSheet;
import com.example.diet2.ui.foodentry.FoodEntryItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final List<FoodEntryItem> rows = new ArrayList<>();
    private FoodEntryAdapter adapter;
    private long nextRowId = 1L;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mock starting rows (UI only)
        if (rows.isEmpty()) {
            FoodEntryItem a = new FoodEntryItem(nextRowId++);
            a.setFoodName("Chicken breast");
            a.setAmount("150");
            a.setWeightType("g");
            a.setCookingMethod("Grilled");

            FoodEntryItem b = new FoodEntryItem(nextRowId++);
            b.setFoodName("Brown rice");
            b.setAmount("1");
            b.setWeightType("serving");
            b.setCookingMethod("Boiled");

            rows.add(a);
            rows.add(b);
        }

        adapter = new FoodEntryAdapter(rows, item -> {
            String title = item.getFoodName() == null || item.getFoodName().isEmpty()
                    ? getString(com.example.diet2.R.string.food_entry_details_title)
                    : item.getFoodName();
            String subtitle = (safe(item.getAmount()) + " " + safe(item.getWeightType()) + " • " + safe(item.getCookingMethod())).trim();
            FoodEntryDetailsBottomSheet.newInstance(title, subtitle)
                    .show(getChildFragmentManager(), "food_entry_details");
        });

        binding.recyclerFoodRows.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerFoodRows.setAdapter(adapter);

        binding.btnScanCamera.setOnClickListener(v ->
                androidx.navigation.fragment.NavHostFragment.findNavController(this)
                        .navigate(com.example.diet2.R.id.action_home_to_cameraSelection)
        );

        binding.btnAddFoodRow.setOnClickListener(v -> {
            FoodEntryItem newRow = new FoodEntryItem(nextRowId++);
            newRow.setFoodName("New food");
            newRow.setAmount("100");
            newRow.setWeightType("g");
            newRow.setCookingMethod("Raw");
            rows.add(newRow);
            adapter.notifyItemInserted(rows.size() - 1);
            Toast.makeText(requireContext(), "+ row added (mock)", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
