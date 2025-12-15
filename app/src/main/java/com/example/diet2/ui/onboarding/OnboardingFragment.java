package com.example.diet2.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diet2.R;
import com.example.diet2.data.preferences.OnboardingPrefs;
import com.example.diet2.databinding.FragmentOnboardingBinding;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class OnboardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;
    private OnboardingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (OnboardingPrefs.isCompleted(requireContext())) {
            NavHostFragment.findNavController(this).navigate(
                    R.id.action_onboarding_to_login
            );
            return;
        }

        List<OnboardingSlide> slides = new ArrayList<>();
        slides.add(new OnboardingSlide(R.string.onboarding_title_1, R.string.onboarding_body_1));
        slides.add(new OnboardingSlide(R.string.onboarding_title_2, R.string.onboarding_body_2));
        slides.add(new OnboardingSlide(R.string.onboarding_title_3, R.string.onboarding_body_3));

        adapter = new OnboardingAdapter(slides);
        binding.pager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabDots, binding.pager, (tab, position) -> {
            tab.setText("•");
        }).attach();

        binding.btnSkip.setOnClickListener(v -> complete());

        binding.btnNext.setOnClickListener(v -> {
            int pos = binding.pager.getCurrentItem();
            if (pos >= slides.size() - 1) {
                complete();
            } else {
                binding.pager.setCurrentItem(pos + 1, true);
            }
        });

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == slides.size() - 1) {
                    binding.btnNext.setText(R.string.onboarding_done);
                } else {
                    binding.btnNext.setText(R.string.onboarding_next);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void complete() {
        OnboardingPrefs.setCompleted(requireContext(), true);
        NavHostFragment.findNavController(this).navigate(R.id.action_onboarding_to_login);
    }
}
