package cs446.budgetme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {
    protected Button login;
    protected EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        login.setEnabled(false);

        name = findViewById(R.id.username_input);
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
                startdashboardActivity();
            }
        });
    }

    private void startdashboardActivity() {
        Intent i = new Intent(this, DashboardActivity.class);
        String username = name.getText().toString();
        i.putExtra("username", username);
        startActivity(i);
    }
}
