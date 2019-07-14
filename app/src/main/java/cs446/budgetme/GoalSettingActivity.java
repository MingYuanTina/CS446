package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;

public class GoalSettingActivity extends AppCompatActivity {

    private String mCurrentCost = "";
    private EditText mLimitEdit;
    private EditText mStartDateEdit;
    private EditText mEndDateEdit;
    private final Calendar mCalendar = Calendar.getInstance();
    private Date mStartDate;
    private Date mEndDate;
    private EditText mCategoriesEdit;
    private Button mButton;
    private AlertDialog mDialog;
    private List<TransactionCategory> mChosenCategories;
    private ArrayList<TransactionCategory> availableCategories;
    private ArrayList<Boolean> mCheckedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);

        //get all the view components
        mLimitEdit = findViewById(R.id.goal_setting_limit);
        mStartDateEdit = findViewById(R.id.goal_setting_start_date);
        mEndDateEdit = findViewById(R.id.goal_setting_end_date);
        mCategoriesEdit = findViewById(R.id.goal_setting_categories);
        mButton = findViewById(R.id.goal_setting_button);

        // Initialize Cost EditText
        mLimitEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        mLimitEdit.setText(mCurrentCost);
        mLimitEdit.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(mCurrentCost)){
                    mLimitEdit.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    mCurrentCost = formatted;
                    mLimitEdit.setText(formatted);
                    mLimitEdit.setSelection(formatted.length());

                    mLimitEdit.addTextChangedListener(this);
                }
            }
        });

        // Initialize Date EditText
        final DatePickerDialog.OnDateSetListener startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mStartDate = mCalendar.getTime();
                mStartDateEdit.setText(sdf.format(mCalendar.getTime()));
            }
        };

        // Initialize Date EditText
        final DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                mEndDate = mCalendar.getTime();
                mEndDateEdit.setText(sdf.format(mCalendar.getTime()));
            }

        };

        mStartDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GoalSettingActivity.this, startDatePicker, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mEndDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GoalSettingActivity.this, endDatePicker, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        availableCategories = TransactionCategory.getDefaults(); // This should be the user's available categories.
        mCategoriesEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> items = new ArrayList<>();
                items.add("Select All");
                for (TransactionCategory category : availableCategories) {
                    items.add(category.toString());
                }
                final String[] listItems = items.toArray(new String[items.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(GoalSettingActivity.this);
                builder.setTitle("Choose items");

                final boolean[] checkedItems = new boolean[items.size()]; //this will checked the items when user open the dialog
                if (mCheckedItems == null) {
                    Arrays.fill(checkedItems, true);
                } else {
                    checkedItems[0] = true;
                    for (int i = 0; i<mCheckedItems.size(); i++) {
                        checkedItems[i+1] = mCheckedItems.get(i);
                    }
                }
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        if (which == 0) {
                            checkedItems[0] = isChecked;
                            for (int i = 1; i < listItems.length; i++) {
                                checkedItems[i] = isChecked;
                                mDialog.getListView().setItemChecked(i, isChecked);
                            }
                        }
                        //Toast.makeText(GoalSettingActivity.this, "Position: " + which + " Value: " + listItems[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_LONG).show();
                        //mDialog.getListView().setItemChecked(0, false);
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenCategories = new ArrayList<>();
                        mCheckedItems = new ArrayList<>();
                        ArrayList<String> text = new ArrayList<>();
                        for (int i = 1; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                mChosenCategories.add(availableCategories.get(i-1));
                                text.add(availableCategories.get(i-1).toString());
                            }
                            mCheckedItems.add(checkedItems[i]);
                        }
                        mCategoriesEdit.setText(TextUtils.join(", ", text));
                    }
                });

                mDialog = builder.create();
                mDialog.show();
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStartDate.getTime() > mEndDate.getTime()) {
                    Toast.makeText(GoalSettingActivity.this, "Start date cannot be after end date.", Toast.LENGTH_LONG).show();
                    return;
                } else if (mChosenCategories.isEmpty()) {
                    Toast.makeText(GoalSettingActivity.this, "Please choose applicable categories for this goal.", Toast.LENGTH_LONG).show();
                }
                try {
                    // Required Categories
                    String cleanString = mLimitEdit.getText().toString().replaceAll("[$,]", "");
                    double parsedCost = Double.parseDouble(cleanString);
                    Goal.GoalBuilder builder = new Goal.GoalBuilder(parsedCost, mStartDate, mEndDate, mChosenCategories);

                    Intent i = new Intent();
                    i.putExtra("goal", builder.build());
                    setResult(RESULT_OK, i);
                    finish();
                } catch (IllegalStateException e) {

                } catch (Exception e) {

                }
            }
        });
    }
}
