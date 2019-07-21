package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    protected EditText username;
    protected EditText password;
    protected EditText emailAccount;
    protected Button register;
    private static final String TAG = RegistrationActivity.class.getName();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailAccount = findViewById(R.id.emailAccount);
        password = findViewById(R.id.password);
        username = findViewById(R.id.user_name);
        register = findViewById(R.id.register);
        register.setEnabled(false);

        mProgressDialog = new ProgressDialog(this);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                boolean isReady = password.getText().toString().length() > 0;
                register.setEnabled(isReady);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                String e = emailAccount.getText().toString();
                if(u == "" || p =="" || e ==""){
                    Context context = getApplicationContext();
                    CharSequence text = "Please fill in All the required text";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }else{
                    mProgressDialog.setMessage("Registering...");
                    mProgressDialog.show();
                    APIUtils.getInstance().postRegisterUser(u, e, p, new APIUtils.APIUtilsCallback<ResponseBody>() {
                        @Override
                        public void onResponseSuccess(ResponseBody responseBody) {
                            mProgressDialog.dismiss();
                            startLogin();
                        }

                        @Override
                        public void onResponseFailure() {
                            mProgressDialog.dismiss();
                            registerFailed();
                        }
                    });
                }

            }
        });
    }

    private void registerFailed(){
        Context context = getApplicationContext();
        CharSequence text = "Cannot Register Email Already Exist";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }
    private void startLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
