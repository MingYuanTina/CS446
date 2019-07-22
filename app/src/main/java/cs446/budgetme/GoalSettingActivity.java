package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.MultipleChoiceWithSelectAllDialogCallback;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.Model.User;
import cs446.budgetme.Widgets.MultipleChoiceWithSelectAllDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalSettingActivity extends AppCompatActivity implements MultipleChoiceWithSelectAllDialogCallback<TransactionCategory> {

    private String mCurrentCost = "";
    private EditText mLimitEdit;
    private EditText mStartDateEdit;
    private EditText mEndDateEdit;
    private final Calendar mCalendar = Calendar.getInstance();
    private Date mStartDate;
    private Date mEndDate;
    private EditText mCategoriesEdit;
    private Button mButton;
    private List<TransactionCategory> mChosenCategories = new ArrayList<>();
    private List<TransactionCategory> availableCategories = new ArrayList<>();
    private ArrayList<Boolean> mCheckedItems;
    private MultipleChoiceWithSelectAllDialog<TransactionCategory> mDialog;
    private String USER_TOKEN;
    private String groupID;
    private static final String TAG = GoalSettingActivity.class.getName();
    private User mUser;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_goal);
        setSupportActionBar(toolbar);
        setTitle("Create a new Goal");

        Intent intent = getIntent();
        mUser = intent.getExtras().getParcelable("user");

        //set up the group id and user token for all the api calls
        groupID= mUser.getDefaultGroupId();
        USER_TOKEN= mUser.getUserAuthToken();

        mProgressDialog = new ProgressDialog(this);

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

        mDialog = new MultipleChoiceWithSelectAllDialog<>(GoalSettingActivity.this, availableCategories, this);
        mCategoriesEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Goal.GoalBuilder builder = new Goal.GoalBuilder(parsedCost, mStartDate, mEndDate);

                    if (!mChosenCategories.isEmpty()) {
                        builder.setCategories(mChosenCategories);
                    }

                    builder.setNote("");
                    Goal goal = builder.build();
                    mProgressDialog.setMessage("Adding new goal...");
                    mProgressDialog.show();
                    APIUtils.getInstance().postGoal(goal, USER_TOKEN, groupID, new APIUtils.APIUtilsCallback<JsonElement>() {
                        @Override
                        public void onResponseSuccess(JsonElement jsonElement) {
                            mProgressDialog.dismiss();
                            completeGoalPost();
                        }

                        @Override
                        public void onResponseFailure() {
                            mProgressDialog.dismiss();
                        }
                    });

                } catch (IllegalStateException e) {

                } catch (Exception e) {

                }
            }
        });

        APIUtils.getInstance().loadCategoryList(USER_TOKEN, groupID, new APIUtils.APIUtilsCallback<List<TransactionCategory>>() {
            @Override
            public void onResponseSuccess(List<TransactionCategory> transactionCategories) {
                updateTransactionCategoryList(transactionCategories);
            }

            @Override
            public void onResponseFailure() {}
        });
    }

    private void completeGoalPost(){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void multipleChoiceWithSelectAllDialogCallback(List<TransactionCategory> chosenCategories) {
        mChosenCategories = chosenCategories;
        if (mChosenCategories.isEmpty()) {
            mCategoriesEdit.setText(getResources().getString(R.string.label_all_categories));
        } else {
            List<String> text = new ArrayList<>();
            for (int i = 0; i < mChosenCategories.size(); i++) {
                text.add(mChosenCategories.get(i).toString());
            }
            mCategoriesEdit.setText(TextUtils.join(", ", text));
        }
    }

    public void updateTransactionCategoryList(List<TransactionCategory> transactionCategories) {
        availableCategories = transactionCategories;
        mDialog = new MultipleChoiceWithSelectAllDialog<>(GoalSettingActivity.this, availableCategories, this);
    }
}
