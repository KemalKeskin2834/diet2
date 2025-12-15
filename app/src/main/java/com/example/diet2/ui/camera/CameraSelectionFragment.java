package com.example.diet2.ui.camera;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diet2.R;
import com.example.diet2.databinding.FragmentCameraSelectionBinding;

import java.io.File;

public class CameraSelectionFragment extends Fragment {

    private FragmentCameraSelectionBinding binding;

    private Uri pendingCameraUri;

    private final ActivityResultLauncher<String> galleryPicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri == null) return;
                goToPreview(uri);
            }
    );

    private final ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (Boolean.TRUE.equals(success) && pendingCameraUri != null) {
                    goToPreview(pendingCameraUri);
                } else {
                    Toast.makeText(requireContext(), R.string.camera_flow_cancel, Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCameraSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = NavHostFragment.findNavController(this);

        binding.btnCamera.setOnClickListener(v -> {
            try {
                pendingCameraUri = createTempImageUri();
                takePicture.launch(pendingCameraUri);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Camera unavailable (UI only)", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnGallery.setOnClickListener(v -> galleryPicker.launch("image/*"));

        binding.btnCancel.setOnClickListener(v -> navController.navigateUp());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void goToPreview(@NonNull Uri uri) {
        Bundle args = new Bundle();
        args.putString(CameraPreviewFragment.ARG_IMAGE_URI, uri.toString());
        NavHostFragment.findNavController(this).navigate(R.id.action_cameraSelection_to_cameraPreview, args);
    }

    private Uri createTempImageUri() {
        File dir = new File(requireContext().getCacheDir(), "camera");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        File file = new File(dir, "capture_" + System.currentTimeMillis() + ".jpg");
        String authority = requireContext().getPackageName() + ".fileprovider";
        return FileProvider.getUriForFile(requireContext(), authority, file);
    }
}
