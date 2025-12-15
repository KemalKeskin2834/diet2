package com.example.diet2.ui.auth;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.example.diet2.databinding.FragmentPasswordResetBinding;

import java.util.Locale;

public class PasswordResetFragment extends Fragment {

    private static final long CODE_VALIDITY_MS = 5L * 60L * 1000L;
    private static final String STATE_CODE_EXPIRES_AT = "code_expires_at";

    private FragmentPasswordResetBinding binding;
    private CountDownTimer timer;
    private long codeExpiresAtEpochMs = 0L;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentPasswordResetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            codeExpiresAtEpochMs = savedInstanceState.getLong(STATE_CODE_EXPIRES_AT, 0L);
        }
        restoreTimerIfNeeded();

        NavController navController = NavHostFragment.findNavController(this);

        binding.btnRequestCode.setOnClickListener(v -> {
            clearErrors();

            String email = value(binding.inputEmail);
            if (!isValidEmail(email)) {
                binding.tilEmail.setError(getString(R.string.error_email_invalid));
                return;
            }

            // UI-only placeholder (no backend): start a 5-minute validity window
            codeExpiresAtEpochMs = System.currentTimeMillis() + CODE_VALIDITY_MS;
            startTimer(codeExpiresAtEpochMs);
            Toast.makeText(requireContext(), R.string.auth_reset_code_demo, Toast.LENGTH_SHORT).show();
        });

        binding.btnResetPassword.setOnClickListener(v -> {
            clearErrors();

            String email = value(binding.inputEmail);
            String code = value(binding.inputCode);
            String newPassword = value(binding.inputNewPassword);
            String confirmPassword = value(binding.inputConfirmPassword);

            boolean ok = true;

            if (!isValidEmail(email)) {
                binding.tilEmail.setError(getString(R.string.error_email_invalid));
                ok = false;
            }

            if (TextUtils.isEmpty(code)) {
                binding.tilCode.setError(getString(R.string.error_required));
                ok = false;
            } else if (!isValidActivationCode(code)) {
                binding.tilCode.setError(getString(R.string.error_code_invalid));
                ok = false;
            }

            if (!isValidPassword(newPassword)) {
                binding.tilNewPassword.setError(getString(R.string.error_password_invalid));
                ok = false;
            }

            if (!TextUtils.equals(newPassword, confirmPassword)) {
                binding.tilConfirmPassword.setError(getString(R.string.error_password_mismatch));
                ok = false;
            }

            if (!hasValidCodeWindow()) {
                binding.tilCode.setError(getString(R.string.error_code_expired));
                ok = false;
            }

            if (!ok) return;

            // UI-only placeholder (no backend)
            Toast.makeText(requireContext(), R.string.auth_reset_done_demo, Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_reset_to_login);
        });

        binding.btnBackToLogin.setOnClickListener(v -> navController.navigateUp());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_CODE_EXPIRES_AT, codeExpiresAtEpochMs);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
        binding = null;
    }

    private void clearErrors() {
        binding.tilEmail.setError(null);
        binding.tilCode.setError(null);
        binding.tilNewPassword.setError(null);
        binding.tilConfirmPassword.setError(null);
    }

    private void restoreTimerIfNeeded() {
        if (codeExpiresAtEpochMs <= 0L) {
            renderNoActiveCode();
            return;
        }

        if (System.currentTimeMillis() >= codeExpiresAtEpochMs) {
            renderNoActiveCode();
            return;
        }

        startTimer(codeExpiresAtEpochMs);
    }

    private boolean hasValidCodeWindow() {
        return codeExpiresAtEpochMs > 0L && System.currentTimeMillis() < codeExpiresAtEpochMs;
    }

    private void startTimer(long expiresAtEpochMs) {
        stopTimer();

        binding.btnRequestCode.setEnabled(false);
        binding.txtCodeTimer.setVisibility(View.VISIBLE);

        long remainingMs = Math.max(0L, expiresAtEpochMs - System.currentTimeMillis());
        renderRemaining(remainingMs);

        timer = new CountDownTimer(remainingMs, 1000L) {
            @Override
            public void onTick(long millisUntilFinished) {
                renderRemaining(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                renderNoActiveCode();
            }
        };
        timer.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void renderNoActiveCode() {
        stopTimer();
        codeExpiresAtEpochMs = 0L;
        if (binding == null) return;
        binding.btnRequestCode.setEnabled(true);
        binding.txtCodeTimer.setVisibility(View.GONE);
    }

    private void renderRemaining(long remainingMs) {
        long totalSeconds = Math.max(0L, remainingMs / 1000L);
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        String text = getString(R.string.reset_code_expires_in, String.format(Locale.US, "%d:%02d", minutes, seconds));
        binding.txtCodeTimer.setText(text);
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

    private static boolean isValidActivationCode(@NonNull String code) {
        // UI-only validation: 6 digits
        return code.matches("\\\\d{6}");
    }
}
