package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Model.Group;
import cs446.budgetme.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    protected Button login;
    protected Button register;
    protected EditText name;
    protected EditText password;
    private static final String TAG = LoginActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        login.setEnabled(false);

        register = findViewById(R.id.register);

        name = findViewById(R.id.username_input);
        password = findViewById(R.id.editText3);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                boolean isReady = name.getText().toString().length() > 0;
                login.setEnabled(isReady);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAuthen(name.getText().toString(), password.getText().toString());
            }
        });

        //Start Register Activity
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
        });

    }

    private void sendAuthen(String email, String password){
        //TODO: need to hash passoword
        APIUtils apicall = new APIUtils();
        String hashedP = hashPass(password);
        if(hashedP != "") password = hashedP;
        apicall.getApiInterface().getUser(email, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    startDashboardActivity(response.body());
                }
                else{
                    loginFailed();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });

    }

    private void startDashboardActivity(User user) {
        Intent i = new Intent(this, DashboardActivity.class);
       // String username = name.getText().toString();
      //  User muser = createFakeUser();
        i.putExtra("user", user);
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
    private void loginFailed(){
        Context context = getApplicationContext();
        CharSequence text = "Password Incorrect, Please Try Again";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    private User createFakeUser(){
        User muser = new User("USER 1");
        muser.setUserAuthToken("5d30ff4e6397c4000427fabe");
        muser.setDefaultGroupId("5d30ff4e6397c4000427fabd");
        ArrayList<Group> mygrouList = new ArrayList<>();
        ArrayList<String> userinGroup = new ArrayList<>();
        userinGroup.add("weq");
        mygrouList.add(new Group("5d30ff4e6397c4000427fabd", "personal", userinGroup));
        mygrouList.add(new Group("232","332",userinGroup));
        muser.setGrouList(mygrouList);
        return muser;
    }
}
