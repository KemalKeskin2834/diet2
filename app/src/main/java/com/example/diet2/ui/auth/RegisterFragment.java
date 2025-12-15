package com.example.diet2.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.diet2.R;
import com.example.diet2.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);

        binding.btnCreateAccount.setOnClickListener(v -> {
            clearErrors();

            String firstName = value(binding.inputFirstName);
            String lastName = value(binding.inputLastName);
            String email = value(binding.inputEmail);
            String password = value(binding.inputPassword);

            boolean ok = true;

            if (TextUtils.isEmpty(firstName)) {
                binding.tilFirstName.setError(getString(R.string.error_required));
                ok = false;
            }

            if (TextUtils.isEmpty(lastName)) {
                binding.tilLastName.setError(getString(R.string.error_required));
                ok = false;
            }

            if (!isValidEmail(email)) {
                binding.tilEmail.setError(getString(R.string.error_email_invalid));
                ok = false;
            }

            if (!isValidPassword(password)) {
                binding.tilPassword.setError(getString(R.string.error_password_invalid));
                ok = false;
            }

            if (!ok) return;

            // UI-only placeholder (no backend)
            Toast.makeText(requireContext(), R.string.auth_register_demo, Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_register_to_home);
        });

        binding.btnBackToLogin.setOnClickListener(v -> navController.navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clearErrors() {
        binding.tilFirstName.setError(null);
        binding.tilLastName.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
    }

    private static String value(@NonNull com.google.android.material.textfield.TextInputEditText editText) {
        CharSequence cs = editText.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private static boolean isValidEmail(@NonNull String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPassword(@NonNull String password) {
        return password.length() >= 8;
    }
}
