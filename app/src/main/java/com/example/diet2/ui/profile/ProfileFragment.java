package com.example.diet2.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diet2.R;
import com.example.diet2.data.preferences.ProfilePrefs;
import com.example.diet2.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private final ActivityResultLauncher<String> pickPhoto = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri == null || binding == null) return;
                binding.imgProfile.setImageURI(uri);
                ProfilePrefs.setPhotoUri(requireContext(), uri.toString());
            }
    );

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore saved profile
        binding.inputHeight.setText(ProfilePrefs.getHeightCm(requireContext()));
        binding.inputWeight.setText(ProfilePrefs.getWeightKg(requireContext()));

        String photo = ProfilePrefs.getPhotoUri(requireContext());
        if (photo != null && !photo.isEmpty()) {
            binding.imgProfile.setImageURI(Uri.parse(photo));
        }

        binding.btnPickPhoto.setOnClickListener(v -> pickPhoto.launch("image/*"));

        binding.btnSaveProfile.setOnClickListener(v -> {
            binding.tilHeight.setError(null);
            binding.tilWeight.setError(null);

            String h = text(binding.inputHeight);
            String w = text(binding.inputWeight);

            boolean ok = true;
            if (TextUtils.isEmpty(h)) {
                binding.tilHeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (TextUtils.isEmpty(w)) {
                binding.tilWeight.setError(getString(R.string.error_required));
                ok = false;
            }
            if (!ok) return;

            ProfilePrefs.setHeightCm(requireContext(), h);
            ProfilePrefs.setWeightKg(requireContext(), w);

            Toast.makeText(requireContext(), R.string.profile_saved, Toast.LENGTH_SHORT).show();
        });

        binding.btnChangePassword.setOnClickListener(v ->
                new ChangePasswordDialog().show(getChildFragmentManager(), "change_password")
        );

        // Language selection
        String currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (currentTag == null) currentTag = "";
        if (currentTag.startsWith("tr")) {
            binding.toggleLanguage.check(R.id.btnLangTr);
        } else {
            binding.toggleLanguage.check(R.id.btnLangEn);
        }

        binding.toggleLanguage.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            if (checkedId == R.id.btnLangTr) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("tr"));
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static String text(@NonNull com.google.android.material.textfield.TextInputEditText editText) {
        CharSequence cs = editText.getText();
        return cs == null ? "" : cs.toString().trim();
    }
}
