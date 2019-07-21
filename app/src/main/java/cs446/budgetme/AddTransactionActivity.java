package cs446.budgetme;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTransactionActivity extends AppCompatActivity {
    private String mCurrentCost = "";
    private EditText mCostEdit;
    private EditText mDateEdit;
    private final Calendar mCalendar = Calendar.getInstance();
    private AutoCompleteTextView mDropdown;
    private EditText mNoteEdit;
    private Button mButton;
    private Button mNewCategoryButton;
    private int mCategoryIndex;
    private Button importImage;
    private int GET_FROM_IMAGE = 3;
    private ArrayList<Transaction> currTrans;
    private AlertDialog mNewCategoryDialog;
    private List<TransactionCategory> mTransactionCategories = new ArrayList<>();
    private String USER_TOKEN;
    private String groupID;
    private User mUser;
    private APIUtils.APIUtilsCallback<List<TransactionCategory>> mLoadCategoryCallback;
    private ProgressDialog mNewCategoryProgressDialog;
    private ProgressDialog mAddTransactionProgressDialog;

    private static final String TAG = AddTransactionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNewCategoryProgressDialog = new ProgressDialog(this);
        mAddTransactionProgressDialog = new ProgressDialog(this);

        mLoadCategoryCallback = new APIUtils.APIUtilsCallback<List<TransactionCategory>>() {
            @Override
            public void onResponseSuccess(List<TransactionCategory> transactionCategories) {
                updateTransactionCategoryList(transactionCategories);
            }

            @Override
            public void onResponseFailure() { }
        };

        //set the current transation list
        Intent intent = getIntent();

        //TODO: check if need to update the list
        currTrans = intent.getParcelableArrayListExtra("transactionList");
        mUser = intent.getExtras().getParcelable("user");

        //set up the group id and user token for all the api calls
        groupID= mUser.getDefaultGroupId();
        USER_TOKEN= mUser.getUserAuthToken();

        // Initialize Cost EditText
        mCostEdit = findViewById(R.id.add_transaction_cost);
        mCostEdit.setRawInputType(Configuration.KEYBOARD_12KEY);
        mCostEdit.setText(mCurrentCost);
        mCostEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(mCurrentCost)) {
                    mCostEdit.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = 0.0;
                    String formatted = mCurrentCost;
                    try {
                        parsed = Double.parseDouble(cleanString);
                        formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                        mCurrentCost = formatted;
                    } catch (Exception e) {

                    }
                    mCostEdit.setText(formatted);
                    mCostEdit.setSelection(formatted.length());

                    mCostEdit.addTextChangedListener(this);
                }
            }
        });

        // Initialize Date EditText
        mDateEdit = findViewById(R.id.add_transaction_date);
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
                new DatePickerDialog(AddTransactionActivity.this, datePicker, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Initialize Category spinner
        mDropdown = findViewById(R.id.add_transaction_category);

        ArrayAdapter<TransactionCategory> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, mTransactionCategories);
        mDropdown.setAdapter(adapter);
        mDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                mCategoryIndex = position;
            }
        });

        mNoteEdit = findViewById(R.id.add_transaction_note);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new category");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_input, null);
        final EditText newCategoryInput = viewInflated.findViewById(R.id.input_new_category);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String categoryName = newCategoryInput.getText().toString();
                if (categoryName.length() > 0) {
                    mNewCategoryProgressDialog.setMessage("Adding new category...");
                    mNewCategoryProgressDialog.show();
                    APIUtils.getInstance().postNewCategory(categoryName, USER_TOKEN, groupID, new APIUtils.APIUtilsCallback<JsonElement>() {
                        @Override
                        public void onResponseSuccess(JsonElement jsonElement) {
                            mNewCategoryProgressDialog.dismiss();
                            dialog.dismiss();
                            APIUtils.getInstance().loadCategoryList(USER_TOKEN, groupID, mLoadCategoryCallback);
                        }

                        @Override
                        public void onResponseFailure() {
                            mNewCategoryProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mNewCategoryDialog = builder.create();

        mNewCategoryButton = findViewById(R.id.new_category_button);
        mNewCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewCategoryDialog.show();
            }
        });

        mButton = findViewById(R.id.add_transaction_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Required Fields
                    String cleanString = mCostEdit.getText().toString().replaceAll("[$,]", "");
                    double parsedCost = Double.parseDouble(cleanString);
                    Date date = mCalendar.getTime();
                    TransactionCategory transactionCategory = mTransactionCategories.get(mCategoryIndex);
                    Transaction.TransactionBuilder builder = new Transaction.TransactionBuilder(parsedCost, date, transactionCategory);

                    // Optional Fields
                    if (!mNoteEdit.getText().toString().isEmpty()) {
                        builder.setNote(mNoteEdit.getText().toString());
                    } else {
                        builder.setNote("");
                    }

                    Transaction transaction = builder.build();
                    mAddTransactionProgressDialog.setMessage("Adding transaction...");
                    mAddTransactionProgressDialog.show();
                    APIUtils.getInstance().postTransaction(transaction, USER_TOKEN, groupID, new APIUtils.APIUtilsCallback<JsonElement>() {
                        @Override
                        public void onResponseSuccess(JsonElement jsonElement) {
                            mAddTransactionProgressDialog.dismiss();
                            completePostTransaction();
                        }

                        @Override
                        public void onResponseFailure() {
                            mAddTransactionProgressDialog.dismiss();
                        }
                    });

                } catch (IllegalStateException e) {
                    Toast.makeText(AddTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                }
            }
        });

        //import Image for Text extracting
        importImage= findViewById(R.id.import_from_image_button);
        importImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the import image activity
                Intent i = new Intent(AddTransactionActivity.this, ReceiptOCRActivity.class);
                startActivityForResult(i,GET_FROM_IMAGE );
            }
        });

        APIUtils.getInstance().loadCategoryList(USER_TOKEN, groupID, mLoadCategoryCallback);
    }

    private void completePostTransaction(){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //the activity information is passed back
        if (requestCode == GET_FROM_IMAGE && resultCode == RESULT_OK) {
            String imageTxt = data.getStringExtra("goal");
            parseTxt(imageTxt);
        }

    }
    private void parseTxt(String imageText){
        boolean findValuable = false;
        String[] txt = imageText.split("\t\r\n");
        String[] line;
        for(String s : txt){

            if(isContain(s.toLowerCase(), "total")|| isContain(s.toLowerCase(), "roral")){
                line = s.split(" ");
                mCurrentCost = line[1];
                mCostEdit.setText(mCurrentCost);
                //find the something from the receipt
                findValuable = true;
            }
        }

        if(!findValuable){
            //notify the user cannot find any value form the image
            Context context = getApplicationContext();
            CharSequence text = "Cannot find Cost";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
    }

    private static boolean isContain(String source, String subItem){
        String pattern = "\\b"+subItem+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(source);
        return m.find();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateEdit.setText(sdf.format(mCalendar.getTime()));
    }

    public void updateTransactionCategoryList(List<TransactionCategory> transactionCategories) {
        mTransactionCategories = transactionCategories;
        ArrayAdapter<TransactionCategory> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, mTransactionCategories);
        mDropdown.setAdapter(adapter);
        mDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position,
                                    long arg3) {
                mCategoryIndex = position;
            }
        });
    }
}