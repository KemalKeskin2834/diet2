package com.example.diet2.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diet2.R;
import com.example.diet2.databinding.DialogChangePasswordBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangePasswordDialog extends BottomSheetDialogFragment {

    private DialogChangePasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = DialogChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnSave.setOnClickListener(v -> {
            clearErrors();

            String current = text(binding.inputCurrent);
            String next = text(binding.inputNew);
            String confirm = text(binding.inputConfirm);

            boolean ok = true;

            if (TextUtils.isEmpty(current)) {
                binding.tilCurrent.setError(getString(R.string.error_required));
                ok = false;
            }

            if (next.length() < 8) {
                binding.tilNew.setError(getString(R.string.error_password_invalid));
                ok = false;
            }

            if (!TextUtils.equals(next, confirm)) {
                binding.tilConfirm.setError(getString(R.string.error_password_mismatch));
                ok = false;
            }

            if (!ok) return;

            // UI-only (no backend)
            Toast.makeText(requireContext(), R.string.profile_password_changed_mock, Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clearErrors() {
        binding.tilCurrent.setError(null);
        binding.tilNew.setError(null);
        binding.tilConfirm.setError(null);
    }

    private static String text(@NonNull com.google.android.material.textfield.TextInputEditText editText) {
        CharSequence cs = editText.getText();
        return cs == null ? "" : cs.toString();
    }
}
