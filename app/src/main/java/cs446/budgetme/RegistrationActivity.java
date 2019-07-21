package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailAccount = findViewById(R.id.emailAccount);
        password = findViewById(R.id.password);
        username = findViewById(R.id.user_name);
        register = findViewById(R.id.register);
        register.setEnabled(false);
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
                    String hashedP = hashPass(p);
                    if(hashedP != "") p = hashedP;
                    RegisterRequest mRegist = new RegisterRequest(u,e,p);
                    postUser(mRegist);
                }

            }
        });
    }


    private void postUser(RegisterRequest request){
        APIUtils apicall = new APIUtils();
        apicall.getApiInterface().registerAccount(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    startLogin();
                }
                else{
                    RegisterFailed();
                    startLogin();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    private void RegisterFailed(){
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

    private String hashPass(String pass){
        try {
        MessageDigest md = MessageDigest.getInstance( "SHA-256" );
        // Change this to UTF-16 if needed
        md.update( pass.getBytes( StandardCharsets.UTF_8 ) );
        byte[] digest = md.digest();
        String hex = String.format( "%064x", new BigInteger( 1, digest ) );
        return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public class RegisterRequest{
        private String username;
        private String email;
        private String password;

        public RegisterRequest(String username, String email, String password){
            this.username= username;
            this.email= email;
            this.password= password;
        }


    }
}
