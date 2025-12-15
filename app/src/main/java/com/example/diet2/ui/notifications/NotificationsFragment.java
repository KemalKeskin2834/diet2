package com.example.diet2.ui.notifications;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.diet2.R;
import com.example.diet2.data.preferences.WaterReminderPrefs;
import com.example.diet2.data.reminders.WaterReminderScheduler;
import com.example.diet2.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private final ActivityResultLauncher<String> requestPostNotifications =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (Boolean.TRUE.equals(granted)) {
                    applyScheduleFromUi();
                } else {
                    if (binding != null) {
                        binding.switchEnable.setChecked(false);
                        binding.tilInterval.setEnabled(false);
                    }
                    WaterReminderPrefs.setEnabled(requireContext(), false);
                    WaterReminderScheduler.cancel(requireContext());
                    Toast.makeText(requireContext(), R.string.water_permission_needed, Toast.LENGTH_SHORT).show();
                }
            });

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public android.view.View onCreateView(
            @NonNull android.view.LayoutInflater inflater,
            @Nullable android.view.ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> intervalAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.water_reminder_interval_options,
                android.R.layout.simple_list_item_1
        );
        binding.inputInterval.setAdapter(intervalAdapter);

        // Restore saved state
        boolean enabled = WaterReminderPrefs.isEnabled(requireContext());
        int minutes = WaterReminderPrefs.getIntervalMinutes(requireContext());

        binding.switchEnable.setChecked(enabled);
        binding.tilInterval.setEnabled(enabled);
        binding.inputInterval.setText(labelForMinutes(minutes), false);

        binding.switchEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.tilInterval.setEnabled(isChecked);

            if (!isChecked) {
                WaterReminderPrefs.setEnabled(requireContext(), false);
                WaterReminderScheduler.cancel(requireContext());
                Toast.makeText(requireContext(), R.string.water_saved, Toast.LENGTH_SHORT).show();
                return;
            }

            if (needsPostNotificationsPermission()) {
                requestPostNotifications.launch(Manifest.permission.POST_NOTIFICATIONS);
                return;
            }

            applyScheduleFromUi();
        });

        binding.inputInterval.setOnItemClickListener((parent, v, position, id) -> {
            if (binding.switchEnable.isChecked()) {
                applyScheduleFromUi();
                Toast.makeText(requireContext(), R.string.water_saved, Toast.LENGTH_SHORT).show();
            } else {
                // Store preference even if disabled
                WaterReminderPrefs.setIntervalMinutes(requireContext(), minutesForLabel(labelForPosition(position)));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void applyScheduleFromUi() {
        int minutes = minutesForLabel(text(binding.inputInterval));
        WaterReminderPrefs.setIntervalMinutes(requireContext(), minutes);
        WaterReminderPrefs.setEnabled(requireContext(), true);
        WaterReminderScheduler.schedule(requireContext(), minutes);
        Toast.makeText(requireContext(), R.string.water_saved, Toast.LENGTH_SHORT).show();
    }

    private boolean needsPostNotificationsPermission() {
        if (Build.VERSION.SDK_INT < 33) return false;
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED;
    }

    private String labelForMinutes(int minutes) {
        if (minutes <= 15) return getString(R.string.water_interval_15m);
        if (minutes <= 30) return getString(R.string.water_interval_30m);
        if (minutes <= 60) return getString(R.string.water_interval_60m);
        return getString(R.string.water_interval_120m);
    }

    private String labelForPosition(int position) {
        switch (position) {
            case 0:
                return getString(R.string.water_interval_15m);
            case 1:
                return getString(R.string.water_interval_30m);
            case 2:
                return getString(R.string.water_interval_60m);
            default:
                return getString(R.string.water_interval_120m);
        }
    }

    private int minutesForLabel(@NonNull String label) {
        String l = label.trim();
        if (l.equals(getString(R.string.water_interval_15m))) return 15;
        if (l.equals(getString(R.string.water_interval_30m))) return 30;
        if (l.equals(getString(R.string.water_interval_60m))) return 60;
        if (l.equals(getString(R.string.water_interval_120m))) return 120;
        return 60;
    }

    private static String text(@NonNull com.google.android.material.textfield.MaterialAutoCompleteTextView view) {
        CharSequence cs = view.getText();
        return cs == null ? "" : cs.toString();
    }
}
