package com.example.diet2.ui.onboarding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diet2.databinding.ItemOnboardingSlideBinding;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.VH> {

    private final List<OnboardingSlide> slides;

    public OnboardingAdapter(@NonNull List<OnboardingSlide> slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnboardingSlideBinding binding = ItemOnboardingSlideBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(slides.get(position));
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemOnboardingSlideBinding binding;

        VH(@NonNull ItemOnboardingSlideBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull OnboardingSlide slide) {
            binding.txtTitle.setText(slide.titleRes);
            binding.txtBody.setText(slide.bodyRes);
        }
    }
}
