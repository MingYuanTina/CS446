package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;

public class GoalSettingActivity extends AppCompatActivity {

    private String mCurrentCost = "";
    private EditText mCostEdit;
    private EditText mDateEdit;
    private final Calendar mCalendar = Calendar.getInstance();
    private AutoCompleteTextView mDropdown;
    private Button mButton;
    private int mCategoryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);

        //get all the view components
        mCostEdit = findViewById(R.id.goal_setting_cost);
        mDateEdit = findViewById(R.id.goal_setting_date);
        mDropdown = findViewById(R.id.goal_setting_category);
        mButton = findViewById(R.id.goal_setting_button);

        // Initialize Cost EditText
        mCostEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        mCostEdit.setText(mCurrentCost);
        mCostEdit.addTextChangedListener(new TextWatcher(){
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
                    mCostEdit.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    mCurrentCost = formatted;
                    mCostEdit.setText(formatted);
                    mCostEdit.setSelection(formatted.length());

                    mCostEdit.addTextChangedListener(this);
                }
            }
        });

        // Initialize Date EditText
        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GoalSettingActivity.this, datePicker, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Initialize Category spinner
        final List<TransactionCategory> transactionCategories = TransactionCategory.getDefaults();

        ArrayAdapter<TransactionCategory> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, transactionCategories);
        mDropdown.setAdapter(adapter);
        mDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                mCategoryIndex = position;
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String cleanString = mCostEdit.getText().toString().replaceAll("[$,]", "");
                    double parsedCost = Double.parseDouble(cleanString);
                    Date date = mCalendar.getTime();
                    TransactionCategory transactionCategory = transactionCategories.get(mCategoryIndex);
                    Transaction goal = new Transaction(date, parsedCost, "", transactionCategory);

                    Intent i = new Intent();
                    i.putExtra("goal", goal);
                    setResult(RESULT_OK, i);
                    finish();
                } catch (Exception e) {

                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateEdit.setText(sdf.format(mCalendar.getTime()));
    }
}