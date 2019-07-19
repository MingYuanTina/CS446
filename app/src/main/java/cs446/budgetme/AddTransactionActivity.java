package cs446.budgetme;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
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
    private int mCategoryIndex;
    private Button importImage;
    private APIUtils apicall;
    private int GET_FROM_IMAGE = 3;
    private ArrayList<Transaction> currTrans;
    final String USER_TOKEN= "5d30ff4e6397c4000427fabe";
    final String groupID = "5d30ff4e6397c4000427fabd";
    private static final String TAG = AddTransactionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the current transation list
        Intent intent = getIntent();

        //TODO: check if need to update the list
        currTrans = intent.getParcelableArrayListExtra("transactionList");
        //setup API client
        apicall = new APIUtils();

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

        mNoteEdit = findViewById(R.id.add_transaction_note);

        mButton = findViewById(R.id.add_transaction_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Required Fields
                    String cleanString = mCostEdit.getText().toString().replaceAll("[$,]", "");
                    double parsedCost = Double.parseDouble(cleanString);
                    Date date = mCalendar.getTime();
                    TransactionCategory transactionCategory = transactionCategories.get(mCategoryIndex);
                    Transaction.TransactionBuilder builder = new Transaction.TransactionBuilder(parsedCost, date, transactionCategory);

                    // Optional Fields
                    if (!mNoteEdit.getText().toString().isEmpty()) {
                        builder.setNote(mNoteEdit.getText().toString());
                    }else
                        builder.setNote("");

                    Transaction transaction = builder.build();
                    postTrans(transaction);

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
    }

    private void completePost(){
        Intent i = new Intent();
//                    i.putExtra("transaction", transaction);
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

    public void postTrans(Transaction tran) {

        apicall.getApiInterface().addTransaction(tran, USER_TOKEN, groupID).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.isSuccessful()) {
                    completePost();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }
}