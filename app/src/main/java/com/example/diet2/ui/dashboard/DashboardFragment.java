package com.example.diet2.ui.dashboard;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diet2.R;
import com.example.diet2.data.preferences.UserPrefs;
import com.example.diet2.databinding.FragmentDashboardBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private long selectedDateUtcMs;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedDateUtcMs = System.currentTimeMillis();
        updateSelectedDateLabel();

        // Default mode: daily
        binding.toggleMode.check(R.id.btnDaily);
        setMode(Mode.DAILY);

        binding.toggleMode.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            if (checkedId == R.id.btnDaily) setMode(Mode.DAILY);
            else if (checkedId == R.id.btnWeekly) setMode(Mode.WEEKLY);
            else if (checkedId == R.id.btnMonthly) setMode(Mode.MONTHLY);
        });

        binding.btnPickDate.setOnClickListener(v -> openCalendar());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openCalendar() {
        long registrationMs = UserPrefs.getRegistrationEpochMs(requireContext());
        if (registrationMs == 0L) {
            registrationMs = System.currentTimeMillis();
            Toast.makeText(requireContext(), R.string.charts_registration_not_set, Toast.LENGTH_SHORT).show();
        }

        CalendarConstraints.Builder constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.from(registrationMs))
                .setEnd(System.currentTimeMillis());

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.charts_pick_date)
                .setSelection(selectedDateUtcMs)
                .setCalendarConstraints(constraints.build())
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) return;
            selectedDateUtcMs = selection;
            updateSelectedDateLabel();
        });

        picker.show(getChildFragmentManager(), "charts_date_picker");
    }

    private void updateSelectedDateLabel() {
        if (binding == null) return;
        String formatted = DateFormat.getMediumDateFormat(requireContext()).format(new Date(selectedDateUtcMs));
        binding.txtSelectedDate.setText(getString(R.string.charts_selected_date, formatted));
    }

    private void setMode(@NonNull Mode mode) {
        if (binding == null) return;

        binding.chartDaily.setVisibility(mode == Mode.DAILY ? View.VISIBLE : View.GONE);
        binding.chartWeekly.setVisibility(mode == Mode.WEEKLY ? View.VISIBLE : View.GONE);
        binding.chartMonthly.setVisibility(mode == Mode.MONTHLY ? View.VISIBLE : View.GONE);

        int title;
        switch (mode) {
            case WEEKLY:
                title = R.string.charts_mode_weekly;
                break;
            case MONTHLY:
                title = R.string.charts_mode_monthly;
                break;
            case DAILY:
            default:
                title = R.string.charts_mode_daily;
                break;
        }
        binding.txtChartTitle.setText(title);

        // Keep the summary mock; later we can inject real numbers.
        // (No calculations / backend here.)
    }

    enum Mode {
        DAILY,
        WEEKLY,
        MONTHLY
    }
}
