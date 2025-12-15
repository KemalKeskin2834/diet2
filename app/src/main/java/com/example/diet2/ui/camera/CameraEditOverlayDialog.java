package com.example.diet2.ui.camera;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.diet2.R;
import com.example.diet2.databinding.DialogCameraEditOverlayBinding;

public class CameraEditOverlayDialog extends DialogFragment {

    public static final String RESULT_KEY = "camera_edit_overlay_result";
    public static final String RESULT_LABEL = "label";

    private static final String ARG_LABEL = "arg_label";

    private DialogCameraEditOverlayBinding binding;

    public static CameraEditOverlayDialog newInstance(@NonNull String currentLabel) {
        CameraEditOverlayDialog dialog = new CameraEditOverlayDialog();
        Bundle args = new Bundle();
        args.putString(ARG_LABEL, currentLabel);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_Diet2_TransparentDialog);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = DialogCameraEditOverlayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String label = args == null ? "" : args.getString(ARG_LABEL, "");
        binding.inputLabel.setText(label);

        binding.btnCancel.setOnClickListener(v -> dismiss());

        binding.btnSave.setOnClickListener(v -> {
            String newLabel = binding.inputLabel.getText() == null ? "" : binding.inputLabel.getText().toString();
            Bundle result = new Bundle();
            result.putString(RESULT_LABEL, newLabel);
            getParentFragmentManager().setFragmentResult(RESULT_KEY, result);
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
