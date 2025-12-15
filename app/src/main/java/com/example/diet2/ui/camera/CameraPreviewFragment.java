package com.example.diet2.ui.camera;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diet2.R;
import com.example.diet2.databinding.FragmentCameraPreviewBinding;

public class CameraPreviewFragment extends Fragment {

    public static final String ARG_IMAGE_URI = "image_uri";

    private FragmentCameraPreviewBinding binding;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCameraPreviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);

        Bundle args = getArguments();
        String uriStr = args == null ? null : args.getString(ARG_IMAGE_URI);
        if (uriStr != null) {
            imageUri = Uri.parse(uriStr);
            binding.imgPreview.setImageURI(imageUri);
        }

        getParentFragmentManager().setFragmentResultListener(
                CameraEditOverlayDialog.RESULT_KEY,
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    String label = result.getString(CameraEditOverlayDialog.RESULT_LABEL, "");
                    if (label == null || label.trim().isEmpty()) {
                        binding.txtOverlay.setText(R.string.camera_flow_overlay_placeholder);
                    } else {
                        binding.txtOverlay.setText(label.trim());
                    }
                }
        );

        binding.btnEdit.setOnClickListener(v -> {
            String current = binding.txtOverlay.getText() == null ? "" : binding.txtOverlay.getText().toString();
            CameraEditOverlayDialog.newInstance(current).show(getChildFragmentManager(), "camera_edit_overlay");
        });

        binding.btnCancel.setOnClickListener(v -> navController.navigateUp());

        binding.btnSave.setOnClickListener(v -> {
            // UI-only: no AI calculation yet.
            Toast.makeText(requireContext(), "Saved (mock)", Toast.LENGTH_SHORT).show();
            navController.popBackStack(R.id.homeFragment, false);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
