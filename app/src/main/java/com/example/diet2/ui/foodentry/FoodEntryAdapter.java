package com.example.diet2.ui.foodentry;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diet2.R;
import com.example.diet2.databinding.ItemFoodEntryRowBinding;

import java.util.List;

public class FoodEntryAdapter extends RecyclerView.Adapter<FoodEntryAdapter.VH> {

    public interface Listener {
        void onDetailsClicked(@NonNull FoodEntryItem item);
    }

    private final List<FoodEntryItem> items;
    private final Listener listener;

    public FoodEntryAdapter(@NonNull List<FoodEntryItem> items, @NonNull Listener listener) {
        this.items = items;
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodEntryRowBinding binding = ItemFoodEntryRowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        FoodEntryItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemFoodEntryRowBinding binding;

        private TextWatcher foodWatcher;
        private TextWatcher amountWatcher;

        VH(@NonNull ItemFoodEntryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Setup dropdown adapters once per holder.
            ArrayAdapter<CharSequence> weightAdapter = ArrayAdapter.createFromResource(
                    binding.getRoot().getContext(),
                    R.array.weight_type_options,
                    android.R.layout.simple_list_item_1
            );
            binding.inputWeightType.setAdapter(weightAdapter);

            ArrayAdapter<CharSequence> cookingAdapter = ArrayAdapter.createFromResource(
                    binding.getRoot().getContext(),
                    R.array.cooking_method_options,
                    android.R.layout.simple_list_item_1
            );
            binding.inputCookingMethod.setAdapter(cookingAdapter);
        }

        void bind(@NonNull FoodEntryItem item, @NonNull Listener listener) {
            // Remove watchers before setting text to avoid feedback loops.
            if (foodWatcher != null) binding.inputFoodName.removeTextChangedListener(foodWatcher);
            if (amountWatcher != null) binding.inputAmount.removeTextChangedListener(amountWatcher);

            if (!stringEquals(binding.inputFoodName.getText(), item.getFoodName())) {
                binding.inputFoodName.setText(item.getFoodName());
            }
            if (!stringEquals(binding.inputAmount.getText(), item.getAmount())) {
                binding.inputAmount.setText(item.getAmount());
            }

            binding.inputWeightType.setText(item.getWeightType(), false);
            binding.inputCookingMethod.setText(item.getCookingMethod(), false);

            foodWatcher = new SimpleTextWatcher(s -> item.setFoodName(s));
            amountWatcher = new SimpleTextWatcher(s -> item.setAmount(s));
            binding.inputFoodName.addTextChangedListener(foodWatcher);
            binding.inputAmount.addTextChangedListener(amountWatcher);

            binding.inputWeightType.setOnItemClickListener((parent, view, position, id) -> {
                Object v = parent.getItemAtPosition(position);
                item.setWeightType(v == null ? "" : v.toString());
            });

            binding.inputCookingMethod.setOnItemClickListener((parent, view, position, id) -> {
                Object v = parent.getItemAtPosition(position);
                item.setCookingMethod(v == null ? "" : v.toString());
            });

            binding.btnDetails.setOnClickListener(v -> listener.onDetailsClicked(item));

            // Make the whole row clickable for details too.
            binding.getRoot().setOnClickListener(v -> listener.onDetailsClicked(item));
        }

        private static boolean stringEquals(Editable editable, String value) {
            String current = editable == null ? "" : editable.toString();
            String other = value == null ? "" : value;
            return current.equals(other);
        }
    }

    static class SimpleTextWatcher implements TextWatcher {

        interface Consumer {
            void accept(String s);
        }

        private final Consumer consumer;

        SimpleTextWatcher(@NonNull Consumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no-op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // no-op
        }

        @Override
        public void afterTextChanged(Editable s) {
            consumer.accept(s == null ? "" : s.toString().trim());
        }
    }
}
