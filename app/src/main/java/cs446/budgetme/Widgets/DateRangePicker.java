package cs446.budgetme.Widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cs446.budgetme.R;
import cs446.budgetme.Utils.DateUtils;

public class DateRangePicker extends Dialog implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private OnCalenderClickListener onCalenderClickListener;
    private Context context;
    private ViewFlipper viewFlipper;
    private CalendarView startDateCalendarView, endDateCalendarView;
    private TextView startDate, endDate;
    private TextView btnNegative, btnPositive, btnReset;
    private long selectedFromDate;
    private long selectedToDate = 0;
    private Calendar startDateCal = Calendar.getInstance();
    private Calendar endDateCal = Calendar.getInstance();
    private TabLayout tabLayout;

    private String startDateTitle = "start date";
    private String endDateTitle = "end date";
    private String startDateError = "Please select a start date";
    private String endDateError = "Please select an end date";

    public DateRangePicker(@NonNull Context context, OnCalenderClickListener onCalenderClickListener) {
        super(context);
        this.context = context;
        this.onCalenderClickListener = onCalenderClickListener;

        DateUtils.setCalendarToBeginningOfDay(startDateCal);
        DateUtils.setCalendarToBeginningOfDay(endDateCal);
    }

    public DateRangePicker(@NonNull Context context, Date startDate, Date endDate, OnCalenderClickListener onCalenderClickListener) {
        this(context, onCalenderClickListener);
        if (startDate != null && endDate != null) {
            selectedFromDate = startDate.getTime();
            selectedToDate = endDate.getTime();
        }
    }

    /**
     * interface to handle button clicks
     */
    public interface OnCalenderClickListener {
        void onDateSelected(Calendar selectedStartDate, Calendar selectedEndDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_range_picker);

        initView();
    }

    private void initView() {
        tabLayout = findViewById(R.id.drp_tabLayout);
        viewFlipper = findViewById(R.id.drp_viewFlipper);
        startDateCalendarView = findViewById(R.id.drp_calStartDate);
        endDateCalendarView = findViewById(R.id.drp_calEndDate);
        startDate = findViewById(R.id.drp_tvStartDate);
        endDate = findViewById(R.id.drp_tvEndDate);
        btnNegative = findViewById(R.id.drp_btnNegative);
        btnPositive = findViewById(R.id.drp_btnPositive);
        btnReset = findViewById(R.id.drp_btnReset);

        startDateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                startDateCal.set(year, month, dayOfMonth);
                selectedFromDate = startDateCal.getTimeInMillis();
                startDate.setText(dateFormatter.format(startDateCal.getTime()));
                tabLayout.getTabAt(1).select();
            }
        });

        endDateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                endDateCal.set(year, month, dayOfMonth);
                selectedToDate = endDateCal.getTimeInMillis();
                endDate.setText(dateFormatter.format(endDateCal.getTime()));
            }
        });

        if (selectedFromDate != 0 && selectedToDate != 0) {
            startDateCalendarView.setDate(selectedFromDate);
            endDateCalendarView.setDate(selectedToDate);
        }

        tabLayout.addTab(tabLayout.newTab().setText(startDateTitle), true);
        tabLayout.addTab(tabLayout.newTab().setText(endDateTitle));

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnPositive) {
            if (TextUtils.isEmpty(startDate.getText().toString())) {
                Snackbar.make(startDate, startDateError, Snackbar.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(endDate.getText().toString())) {
                Snackbar.make(endDate, endDateError, Snackbar.LENGTH_SHORT).show();
            } else {
                onCalenderClickListener.onDateSelected(startDateCal, endDateCal);
                dismiss();
            }
        } else if (view == btnNegative) {
            dismiss();
        } else if (view == btnReset) {
            onCalenderClickListener.onDateSelected(null, null);
            dismiss();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            viewFlipper.showPrevious();
        } else {
            showToDateCalender();
        }
    }

    /**
     * shows calendar to select the To Date
     */
    private void showToDateCalender() {
        //do not remove  calendarViewEndDate.setMinDate(0);
        //calenderView is full of bugs this is a workaround found by S.C. to setMiniDate properly
        endDateCalendarView.setMinDate(0);
        endDateCalendarView.setMinDate(selectedFromDate);

        if (selectedToDate != 0) {
            endDateCalendarView.setDate(selectedToDate);
        }

        viewFlipper.showNext();

        if (!TextUtils.isEmpty(endDate.getText())) {
            if (endDateCal.before(startDateCal)) {
                endDate.setText(startDate.getText().toString());
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    // public methods
    public void setBtnPositiveText(String text) {
        btnPositive.setText(text);
    }

    public void setBtnNegativeText(String text) {
        btnNegative.setText(text);
    }

    public SimpleDateFormat getDateFormatter() {
        return dateFormatter;
    }

    public String getStartDateTitle() {
        return startDateTitle;
    }

    public void setStartDateTitle(String startDateTitle) {
        this.startDateTitle = startDateTitle;
    }

    public CalendarView getStartDateCalendarView() {
        return startDateCalendarView;
    }

    public CalendarView getEndDateCalendarView() {
        return endDateCalendarView;
    }

    public TextView getStartDate() {
        return startDate;
    }

    public TextView getEndDate() {
        return endDate;
    }

    public TextView getBtnNegative() {
        return btnNegative;
    }

    public TextView getBtnPositive() {
        return btnPositive;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public String getEndDateTitle() {
        return endDateTitle;
    }

    public void setEndDateTitle(String endDateTitle) {
        this.endDateTitle = endDateTitle;
    }

    public String getStartDateError() {
        return startDateError;
    }

    public void setStartDateError(String startDateError) {
        this.startDateError = startDateError;
    }

    public String getEndDateError() {
        return endDateError;
    }

    public void setEndDateError(String endDateError) {
        this.endDateError = endDateError;
    }
}